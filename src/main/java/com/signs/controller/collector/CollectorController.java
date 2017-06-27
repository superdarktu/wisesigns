package com.signs.controller.collector;

import com.signs.dto.Collector.CollectorExcel;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.collector.CollectorService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.BigExcelUtil;
import com.signs.util.BigSheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/collector")
public class CollectorController {

    @Resource
    private CollectorService service;

    @Resource
    private WatermeterService watermeterService;

    /**
     * 添加采集器
     *
     * @param collectorNumber
     * @param collectorName
     * @return
     */
    @PostMapping("/addCollectorNews")
    public Result addCollectorNews(String collectorNumber, String collectorName) {
        Result result = new Result();

        Collector collector = new Collector();
        collector.setCode(collectorNumber);
        collector.setName(collectorName);

        try {
            if (service.insert(collector))
                result.setResult(0);
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询采集器信息
     *
     * @param id
     * @return
     */
    @PostMapping("/gainCollectorNews")
    public Result gainCollectorNews(String id) {
        Result result = new Result();
        try {
            Collector collector = service.query(id);
            Assert.notNull(collector, "该采集器不存在");

            Map<String, String> map = new HashMap<>();
            map.put("collectorNumber", collector.getCode());
            map.put("collectorName", collector.getName());
            result.setData(map);
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }


    /**
     * 修改采集器
     *
     * @param id
     * @param newCollectorNumber
     * @param newCollectorName
     * @return
     */
    @PostMapping("/reviseCollectorNews")
    public Result reviseCollectorNews(String id, String newCollectorNumber, String newCollectorName) {
        Result result = new Result();

        Collector collector = new Collector();
        collector.setCode(newCollectorNumber);
        collector.setName(newCollectorName);
        collector.setId(id);

        try {
            if (service.update(collector))
                result.setResult(0);
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }


    /**
     * 分页查询
     *
     * @param param
     * @param deviceStatus
     * @param tableNumber
     * @return
     */
    @PostMapping("/allEquipmentStatus")
    public Result allEquipmentStatus(PageParam param, Integer deviceStatus, String tableNumber) {
        Result result = new Result();
        try {
            Collector collector = new Collector();
            collector.setStatus(deviceStatus);
            collector.setName(tableNumber);
            result.setData(service.page(param, collector));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询链接表编号
     *
     * @param
     * @return
     */
    @PostMapping("/linkNumber")
    public Result linkNumber(String collectorNumber) {
        Result result = new Result();
        try {
            result.setData(watermeterService.queryByCollector(collectorNumber));
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }

    /**
     * 删除采集器
     *
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
        String TITLES[] = {"时间", "类型 | 收支流水号 ", "金额(元)", "支付渠道 | 单号"};
        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow titleRow = sheet.createRow(0);
        for (int k = 0; k < TITLES.length; k++) {
            XSSFCell titleCell = titleRow.createCell(k);
            titleCell.setCellValue(TITLES[k]);
        }
        OutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("订单导出.xlsx".getBytes("UTF-8"), "ISO-8859-1"));
        workbook.write(output);
        output.close();
    }

    /**
     * 上传excel
     * @param file
     * @return
     */
    @PostMapping("/leadingExcel")
    public Result excel(@RequestParam("file") MultipartFile file) {

        Result result = new Result();
        List<Integer> errorList = new ArrayList<>();
        try {
            String name = file.getOriginalFilename();
            if (!name.endsWith(".xls") && !name.endsWith(".xlsx")) {
                result.setError("请上传excel文件");
                return result;
            }
            new BigExcelUtil(file.getInputStream()).setHandler(new BigSheetContentsHandler(CollectorExcel.class){
                @Override
                public void endRow(int i) {
                    try {
                        if (flag) {
                            errorList.add(i);
                        } else if (i > 0) {
                            CollectorExcel collectorExcel = (CollectorExcel) model;
                            if (service.isHaveCode(collectorExcel.getCode())) {
                                errorList.add(i);
                            } else {
                                Collector collector = new Collector();
                                collector.setName(collectorExcel.getName());
                                collector.setCode(collectorExcel.getCode());
                                service.insert(collector);
                            }
                        }
                    }catch (Exception e){
                        errorList.add(i);
                    }
                }

            }).parse();
        }catch (Exception e){
            e.printStackTrace();
        }
        result.setData(errorList);
        return result;
    }

    /**
     * 表编号唯一确认
     * @param code
     * @return
     */
    @RequestMapping("/isHaveCode")
    public Result isHaveCode(String code){

        Result result  = new Result();
        try{

            if(service.isHaveCode(code))
                result.setResult(1);
            else
                result.setResult(0);
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }


}
