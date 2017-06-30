package com.signs.controller.aiarmHistory;


import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.aiarm.AiarmService;
import com.signs.service.aiarmHistory.AiarmHistoryService;
import com.signs.service.waterFountains.WaterFountainsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/historyAlert")
public class AiarmHistoryController {

    @Resource
    private AiarmHistoryService service;



    /**
     * 分页查询
     */
    @PostMapping("/svnStatus")
    public Result svnStatus(PageParam param,SearchVO searchVO,String id) {
        Result result = new Result();

        try {
            result.setData(service.page(param,searchVO.getDate1(),searchVO.getDate2(),searchVO.getValue(),id));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
    /**
     * 根据传过来的一串id删除
     */
    @PostMapping("/delete")
    public Result delete(String id) {
        Result dto = new Result();
        try {
            service.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            dto.setResult(1);
        }
        return dto;
    }

}
