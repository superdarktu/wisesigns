package com.signs.controller.waterFountain;

import com.signs.dto.waterFountain.WaterFountainExcel;
import com.signs.dto.watermeter.WatermeterExcel;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.waterFountains.WaterFountains;
import com.signs.service.waterFountains.WaterFountainsService;
import com.signs.util.BigExcelUtil;
import com.signs.util.BigSheetContentsHandler;
import com.signs.util.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/water")
public class WaterFountainController {

    @Resource
    private WaterFountainsService service;

    /**
     * 添加饮水机
     */

    @PostMapping("/addDispenser")
    public Result addDispenser(String waterPosition, String tableNumber, Integer waterType, Float longitude, Float latitude) {
        Result dto = new Result();
        try {
            boolean b = service.createFountains(waterPosition, tableNumber, waterType, longitude, latitude);
            String content = b ? "0" : "1";
            dto.setData(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改用户前获取信息
     */
    @PostMapping("/gainDispenser")
    public Result gain(String id) {
        Result dto = new Result();
        try {
            dto.setData(service.gain(id));
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }


    /**
     * 修改饮水机
     */
    @PostMapping("/reviseDispenser")
    public String updateWaterFountains(String id, String newWaterNumber, String newWaterPosition, String newTableNumber, Integer newWaterType, Float newLongitude, Float newLatitude) {
        try {
            if (StringUtil.isEmpty(id)) return "2";
            WaterFountains save = service.save(id, newWaterNumber, newWaterPosition, newTableNumber, newWaterType, newLongitude, newLatitude);
            if (save == null) return "1";
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    /**
     * 根据传过来的一串id删除
     */
    @PostMapping("/deleteWater")
    public Result deleteWaterFountains(String id) {
        Result dto = new Result();
        try {
            service.delete(id);
            dto.setData("0");
        } catch (Exception e) {
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 查询饮水机
     */
    @PostMapping("/queryType")
    public Result pageWaterFountains(PageParam param, String type, String value) {
        Result result=new Result();
        try {
            result.setData(service.page(param, type, value));
        } catch (Exception e) {
            e.printStackTrace();
            result.setData("1");
        }
        return result;
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
            new BigExcelUtil(file.getInputStream()).setHandler(new BigSheetContentsHandler(WaterFountainExcel.class){
                @Override
                public void endRow(int i) {
                    try {
                        if (flag) {
                            errorList.add(i);
                        } else if (i > 0) {
                            WaterFountainExcel waterFountainExcel = (WaterFountainExcel) model;
                            if (service.selectCode(waterFountainExcel.getCode())) {
                                errorList.add(i);
                            } else {
                                service.createFountains(waterFountainExcel.getPosition(),waterFountainExcel.getTableCode()
                                        ,waterFountainExcel.getType(),waterFountainExcel.getLongitude(),waterFountainExcel.getLatitude());
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
     * excel下载
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downExcel")
    public void down(HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        XSSFWorkbook workbook = new XSSFWorkbook();
        String TITLES[] = {"饮水机序号", "饮水机位置","表编号", "类型", "安装时间","水价","成本占比"};
        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow titleRow = sheet.createRow(0);
        for (int k = 0; k < TITLES.length; k++) {
            XSSFCell titleCell = titleRow.createCell(k);
            titleCell.setCellValue(TITLES[k]);
        }
        List<WaterFountains> list  = service.page(null,null,null).getList();

        XSSFCell cell = null;
        for(int i=1;i<=list.size();i++){
            WaterFountains waterFountains = list.get(i-1);
            XSSFRow row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(waterFountains.getCode());
            cell = row.createCell(1);
            cell.setCellValue(waterFountains.getPlace());
            cell = row.createCell(2);
            cell.setCellValue(waterFountains.getTableCode());
            cell = row.createCell(3);
            cell.setCellValue(waterFountains.getType()==0?"公用":"私用");
            cell = row.createCell(4);
            cell.setCellValue(DateUtils.dateToStr(waterFountains.getCtime()));
            cell = row.createCell(5);
            cell.setCellValue(waterFountains.getWaterPrice());
            cell = row.createCell(6);
            cell.setCellValue(waterFountains.getCostScale());
        }

        OutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("饮水机导出.xlsx".getBytes("UTF-8"), "ISO-8859-1"));
        workbook.write(output);
        output.close();
    }
}
