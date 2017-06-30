package com.signs.controller.error;

import com.signs.model.commons.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/6/27.
 */
@RestController
@RequestMapping("/api/error")
public class ErrorController {

    @RequestMapping("/error")
    public Result error() {
        Result result = new Result();
        result.setResult(5);
        return result;
    }
}
