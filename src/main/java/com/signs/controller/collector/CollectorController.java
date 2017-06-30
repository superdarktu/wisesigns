package com.signs.controller.collector;

import com.signs.dto.Collector.CollectorExcel;
import com.signs.dto.Collector.CollectorVO;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.collector.CollectorService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.BigExcelUtil;
import com.signs.util.BigSheetContentsHandler;
import com.signs.util.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


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
    public Result allEquipmentStatus(PageParam param, Integer deviceStatus, String tableNumber,HttpSession session) {
        Result result = new Result();
        try {
            String id = null;
            if (session.getAttribute("type").toString().equals("2"))
                id = session.getAttribute("id").toString();
            Collector collector = new Collector();
            collector.setStatus(deviceStatus);
            collector.setName(tableNumber);
            collector.setPropertyId(id);
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

    /**
     * excel下载
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downExcel")
    public void down(HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        XSSFWorkbook workbook = new XSSFWorkbook();
        String TITLES[] = {"采集器编号", "采集器名称", "设备状态 ", "所属物业", "推广方", "投资方", "链接注册时间", "链接表个数"};
        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow titleRow = sheet.createRow(0);
        for (int k = 0; k < TITLES.length; k++) {
            XSSFCell titleCell = titleRow.createCell(k);
            titleCell.setCellValue(TITLES[k]);
        }
        List<CollectorVO> list = service.page(null, null).getList();

        XSSFCell cell = null;
        for (int i = 1; i <= list.size(); i++) {
            CollectorVO collectorVO = list.get(i - 1);
            XSSFRow row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(collectorVO.getCode());
            cell = row.createCell(1);
            cell.setCellValue(collectorVO.getName());
            cell = row.createCell(2);
            cell.setCellValue(collectorVO.getStatus() == 1 ? "关" : "开");
            cell = row.createCell(3);
            cell.setCellValue(collectorVO.getPropertyName());
            cell = row.createCell(4);
            cell.setCellValue(collectorVO.getTuiguan());
            cell = row.createCell(5);
            cell.setCellValue(collectorVO.getTouzi());
            cell = row.createCell(6);
            cell.setCellValue(DateUtils.dateToStr(collectorVO.getCtime()));
            cell = row.createCell(7);
            cell.setCellValue(collectorVO.getTablenum());
        }

        OutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("采集器导出-" + DateUtils.dateToStr(new Date(), "only") + ".xlsx").getBytes("UTF-8"), "ISO-8859-1"));
        workbook.write(output);
        output.close();
    }

    /**
     * 上传excel
     *
     * @param file
     * @return
     */
    @PostMapping("/leadingExcel")
    public Result excel(@RequestParam("file") MultipartFile file) {

        Result result = new Result();
        List<Integer> errorList = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        try {
            String name = file.getOriginalFilename();
            if (!name.endsWith(".xls") && !name.endsWith(".xlsx")) {
                result.setError("请上传excel文件");
                return result;
            }
            new BigExcelUtil(file.getInputStream()).setHandler(new BigSheetContentsHandler(CollectorExcel.class) {
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
                                if (service.insert(collector))
                                    temp.add(i);
                                else
                                    errorList.add(i);
                            }
                        }
                    } catch (Exception e) {
                        errorList.add(i);
                    }
                }

            }).parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(errorList);
        result.setInfo(temp.size() + "");
        return result;
    }

    /**
     * 表编号唯一确认
     *
     * @param code
     * @return
     */
    @RequestMapping("/isHaveCode")
    public Result isHaveCode(String code) {

        Result result = new Result();
        try {

            if (service.isHaveCode(code))
                result.setResult(1);
            else
                result.setResult(0);
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }


}
