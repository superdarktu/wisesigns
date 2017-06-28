package com.signs.controller.waterCard;

import com.signs.dto.waterCard.WaterCardExcel;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.waterCard.WaterCard;
import com.signs.service.waterCard.WaterCardService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/waterCard")
public class WaterCardController {

    @Resource
    private WaterCardService service;

    /**
     * 添加水卡
     */
    @PostMapping("/addWaterCard")
    public Result addWaterCard(String cardNumberi, String password, Integer type) {

        Result dto = new Result();
        try {
            boolean b = service.createCard(cardNumberi, password, type);
            String content=b?"0":"1";
            dto.setData(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setResult(1);
        }
        return dto;
    }

    /**
     * 根据传过来的一串id删除
     */
    @PostMapping("/deleteCard")
    public Result deleteCard(String id) {
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
     * 查询水卡
     */
    @PostMapping("/queryType")
    public Result pageCard(PageParam param, String type,String status, String value) {
        Result result=new Result();
        try {
        result.setData(service.page(param,type,status,value));
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
        List<Integer> temp = new ArrayList<>();
        try {
            String name = file.getOriginalFilename();
            if (!name.endsWith(".xls") && !name.endsWith(".xlsx")) {
                result.setError("请上传excel文件");
                return result;
            }
            new BigExcelUtil(file.getInputStream()).setHandler(new BigSheetContentsHandler(WaterCardExcel.class){
                @Override
                public void endRow(int i) {
                    try {
                        if (flag) {
                            errorList.add(i);
                        } else if (i > 0) {
                            WaterCardExcel waterCardExcel = (WaterCardExcel) model;
                            if (service.selectCode(waterCardExcel.getCode())) {
                                errorList.add(i);
                            } else {
                                int password = (int)Math.random()*1000000;

                                int type = -1;

                                if(waterCardExcel.getType().equals("公用"))
                                    type = 0;
                                if(waterCardExcel.getType().equals("私用"))
                                    type = 1;
                                if(type > -1) {
                                    if(service.createCard(waterCardExcel.getCode(), password + "", type))
                                        temp.add(i);
                                    else
                                        errorList.add(i);
                                }else{
                                    errorList.add(i);}
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
        result.setInfo(temp.size()+"");
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
        String TITLES[] = {"卡号", "密码","水卡类型", "状态", "关联手机","添加时间"};
        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow titleRow = sheet.createRow(0);
        for (int k = 0; k < TITLES.length; k++) {
            XSSFCell titleCell = titleRow.createCell(k);
            titleCell.setCellValue(TITLES[k]);
        }
        List<WaterCard> list  = service.page(null,null,null,null).getList();

        XSSFCell cell = null;
        for(int i=1;i<=list.size();i++){
            WaterCard waterCard = list.get(i-1);
            XSSFRow row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(waterCard.getCode());
            cell = row.createCell(1);
            cell.setCellValue(waterCard.getPassword());
            cell = row.createCell(2);
            cell.setCellValue(waterCard.getType()==0?"公用":"私用");
            cell = row.createCell(3);
            cell.setCellValue(waterCard.getStatus()==0?"未激活":"已激活");
            cell = row.createCell(4);
            cell.setCellValue(waterCard.getPhone());
            cell = row.createCell(5);
            cell.setCellValue(DateUtils.dateToStr(waterCard.getCtime()));
        }

        OutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("水卡导出-"+DateUtils.dateToStr(new Date(),"Date")+".xlsx").getBytes("UTF-8"), "ISO-8859-1"));
        workbook.write(output);
        output.close();
    }
}
