package com.signs.controller.collector;

import com.signs.model.collector.Collector;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.collector.CollectorService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.BigExcelUtil;
import com.signs.util.test;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/collector")
public class collectorController {

    @Resource
    private CollectorService service;

    @Resource
    private WatermeterService watermeterService;

    /**
     * 添加采集器
     * @param collectorNumber
     * @param collectorName
     * @return
     */
    @PostMapping("/addCollectorNews")
    public Result addCollectorNews(String collectorNumber,String collectorName){
        Result result = new Result();

        Collector collector = new Collector();
        collector.setCode(collectorNumber);
        collector.setName(collectorName);

        try{
            if(service.insert(collector))
                result.setResult(0);
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询采集器信息
     * @param id
     * @return
     */
    @PostMapping("/gainCollectorNews")
    public Result gainCollectorNews(String id){
        Result result = new Result();
        try{
            Collector collector = service.query(id);
            Assert.notNull(collector,"该采集器不存在");

            Map<String,String> map = new HashMap<>();
            map.put("collectorNumber",collector.getCode());
            map.put("collectorName",collector.getName());
            result.setData(map);
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }


    /**
     * 修改采集器
     * @param id
     * @param newCollectorNumber
     * @param newCollectorName
     * @return
     */
    @PostMapping("/reviseCollectorNews")
    public Result reviseCollectorNews(String id,String newCollectorNumber,String newCollectorName){
        Result result = new Result();

        Collector collector = new Collector();
        collector.setCode(newCollectorNumber);
        collector.setName(newCollectorName);
        collector.setId(id);

        try{
            if(service.update(collector))
                result.setResult(0);
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }


    /**
     * 分页查询
     * @param param
     * @param deviceStatus
     * @param tableNumber
     * @return
     */
    @PostMapping("/allEquipmentStatus")
    public Result allEquipmentStatus(PageParam param,Integer deviceStatus, String tableNumber){
        Result result = new Result();
        try{
            Collector collector = new Collector();
            collector.setStatus(deviceStatus);
            collector.setName(tableNumber);
            result.setData(service.page(param,collector));
        }catch (Exception e){
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询链接表编号
     * @param
     * @return
     */
    @PostMapping("/linkNumber")
    public Result linkNumber(String collectorNumber){
        Result result = new Result();
        try{
            result.setData(watermeterService.queryByCollector(collectorNumber));
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }

    /**
     * 删除采集器
     * @param id
     * @return
     */
    @PostMapping("/deleteNetstat")
    public Result deleteNetstat(String id) {
        Result dto = new Result();
        try {
            service.delete(id);
            dto.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            dto.setResult(1);
        }
        return dto;
    }

    @GetMapping("/down")
    public void down(HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        XSSFWorkbook workbook = new XSSFWorkbook();
        String TITLES[] = { "时间",  "类型 | 收支流水号 ", "金额(元)", "支付渠道 | 单号" };
        XSSFSheet sheet = workbook.createSheet("第1页");
        XSSFRow titleRow = sheet.createRow(0);
        for (int k = 0; k < TITLES.length; k++) {
            XSSFCell titleCell = titleRow.createCell(k);
            titleCell.setCellValue(TITLES[k]);
        }
        OutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition","attachment;filename=" + new String("订单导出.xlsx".getBytes("UTF-8"), "ISO-8859-1"));
        workbook.write(output);
        output.close();
    }

    @PostMapping("/excel")
    public Result excel(){

        Result result = new Result();
        return result;
    }
}
