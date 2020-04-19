package com.example.demo.controller.onlinetest;

import com.example.demo.execute.StringSourceCompiler;
import com.example.demo.service.onlinetest.OnlineTestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OnlineTestController {

    @Resource
    OnlineTestService onlineTestService;

    @RequestMapping(value = "/submit/code", method = RequestMethod.POST)
    public Map<String, String> getCode(@RequestParam("code") String code, @RequestParam("param") String param){
        //输出code
        Map<String, String> runResultMap = new HashMap<>();
        runResultMap =  onlineTestService.getCode(code, param);
        return runResultMap;
    }

}
