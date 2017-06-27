package com.signs.controller.watermeter;

import com.alibaba.fastjson.JSONObject;
import com.signs.dto.watermeter.WatermeterExcel;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.BigExcelUtil;
import com.signs.util.BigSheetContentsHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/outside")
public class WatermeterController {

    @Resource
    private WatermeterService service;


    /**
     * 阀门控制
     *
     * @param id
     * @return
     */
    @RequestMapping("/control")
    public Result control(String id) {
        Result result = new Result();
        result.setResult(1);
        if (service.changeTap(id)) result.setResult(0);
        return result;
    }

    /**
     * 添加表信息
     *
     * @param outsideNumber
     * @param tableNumber
     * @param collecotrNumber
     * @return
     */
    @RequestMapping("/addTableNews")
    public Result control(String outsideNumber, String tableNumber, String collecotrNumber) {
        Result result = new Result();
        result.setResult(1);

        Watermeter watermeter = new Watermeter();
        watermeter.setCollectorCode(collecotrNumber);
        watermeter.setCode(outsideNumber);
        watermeter.setTotalCode(tableNumber);

        if (service.insert(watermeter)) result.setResult(0);
        return result;
    }

    /**
     * 修改表信息
     *
     * @param id
     * @param newOutsideNumber
     * @param newTableNumber
     * @param newCollecotrNumber
     * @return
     */
    @RequestMapping("/reviseTableNews")
    public Result reviseTableNews(String id, String newOutsideNumber, String newTableNumber, String newCollecotrNumber) {
        Result result = new Result();
        result.setResult(1);

        Watermeter watermeter = new Watermeter();
        watermeter.setId(id);
        watermeter.setCollectorCode(newCollecotrNumber);
        watermeter.setCode(newOutsideNumber);
        watermeter.setTotalCode(newTableNumber);

        if (service.update(watermeter)) result.setResult(0);
        return result;
    }

    /**
     * 修改获取表信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/gainTableNews")
    public Result gainTableNews(String id) {

        Result result = new Result();
        Watermeter watermeter = service.query(id);

        JSONObject object = new JSONObject();
        object.put("outsideNumber", watermeter.getCode());
        object.put("tableNumber", watermeter.getTotalCode());
        object.put("collecotrNumber", watermeter.getCollectorCode());
        result.setData(object);
        return result;
    }

    /**
     * 删除表信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteNetstat")
    public Result deleteNetstat(String id) {
        Result result = new Result();
        result.setResult(1);
        if (service.delete(id)) result.setResult(0);
        return result;
    }

    /**
     * 分页条件查询
     *
     * @param param
     * @param deviceStatus
     * @param valveOperation
     * @param tableNumber
     * @return
     */
    @RequestMapping("/allEquipmentStatus")
    public Result allEquipmentStatus(PageParam param, String deviceStatus, String valveOperation, String tableNumber) {
        Result result = new Result();
        result.setResult(1);
        Map<String, Object> map = new HashMap<>();

        map.put("status", deviceStatus);
        map.put("tapStatus", valveOperation);
        map.put("code", tableNumber);

        result.setData(service.page(param, map));
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
            new BigExcelUtil(file.getInputStream()).setHandler(new BigSheetContentsHandler(WatermeterExcel.class){
                @Override
                public void endRow(int i) {
                    try {
                        if (flag) {
                            errorList.add(i);
                        } else if (i > 0) {
                            WatermeterExcel watermeterExcel = (WatermeterExcel) model;
                            if (service.isHaveCode(watermeterExcel.getCode())) {
                                errorList.add(i);
                            } else {
                                Watermeter watermeter = new Watermeter();
                                watermeter.setCode(watermeterExcel.getCode());
                                watermeter.setCollectorCode(watermeterExcel.getCollectorCode());
                                watermeter.setTotalCode(watermeterExcel.getTotalCode());
                                service.insert(watermeter);
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

}
