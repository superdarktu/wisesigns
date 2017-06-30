package com.signs.controller.aiarm;


import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.aiarm.AiarmService;
import com.signs.service.waterFountains.WaterFountainsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/alert")
public class AiarmController {

    @Resource
    private AiarmService service;

    @Resource
    private WaterFountainsService waterFountainsService;


    /**
     * 查询公用饮水机
     *
     * @return
     */
    @PostMapping("/information")
    public Result information(HttpSession session) {
        Result result = new Result();

        try {
            String id = null;
            if (session.getAttribute("type").toString().equals("2"))
                id = session.getAttribute("id").toString();
            result.setData(waterFountainsService.getPublicWaterFountains(id));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

    @PostMapping("/detailed")
    public Result detailed(HttpSession session) {
        Result result = new Result();

        try {
            String id = null;
            if (session.getAttribute("type").toString().equals("2"))
                id = session.getAttribute("id").toString();
            result.setData(service.detailed(id));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

    /**
     * 分页查询
     *
     * @param param
     * @param searchVO
     * @return
     */
    @PostMapping("/svnStatus")
    public Result svnStatus(PageParam param, SearchVO searchVO, HttpSession session) {
        Result result = new Result();

        try {
            String id = null;
            if (session.getAttribute("type").toString().equals("2"))
                id = session.getAttribute("id").toString();
            result.setData(service.page(param, searchVO.getDate1(), searchVO.getDate2(), searchVO.getValue(), id));
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询饮水机
     *
     * @param id
     * @return
     */
    @PostMapping("/query")
    public Result svnStatus(String id) {
        Result result = new Result();

        try {

            result.setData(waterFountainsService.getSingleWaterFountains(id));
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }

}
