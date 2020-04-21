package com.example.demo.controller.schedule;

import com.example.demo.service.schedule.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduledController {


    @Autowired
    ScheduledService scheduledService;

    @Scheduled(cron = "0 0 0 * * *")
    @RequestMapping(value = "/deleteuselessinfo", method = RequestMethod.POST)
    public void deleteUselessInfo(){

        scheduledService.deleteUselessInfo();

    }

}
