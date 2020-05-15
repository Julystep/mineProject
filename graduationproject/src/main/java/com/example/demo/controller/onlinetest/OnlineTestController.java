package com.example.demo.controller.onlinetest;

import com.example.demo.execute.StringSourceCompiler;
import com.example.demo.service.onlinetest.OnlineTestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OnlineTestController {

    @Resource
    OnlineTestService onlineTestService;

    @Async("taskExecutor1")
    @RequestMapping(value = "/submit/code", method = RequestMethod.POST)
    public Map<String, String> getCode(@RequestParam("code") String code, @RequestParam("param") String param){
        //输出code
        Map<String, String> runResultMap = new HashMap<>();
        runResultMap =  onlineTestService.getCode(code, param);
        return runResultMap;
    }

    @Async("taskExecutor2")
    @RequestMapping("/downloadcode")
    public String downloadcode(@RequestParam("code") String code,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {

        return code;

        /*return onlineTestService.downloadcode(code, request, response);*/

    }

}
