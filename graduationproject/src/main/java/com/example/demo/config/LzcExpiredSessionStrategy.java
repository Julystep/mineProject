package com.example.demo.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LzcExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        // 这里也可以根据需要返回html页面或者json数据
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "已经另一台机器登录，您被迫下线。" + event.getSessionInformation().getLastRequest());
        map.put("redirect", "/");
        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write(objectMapper.writeValueAsString(map));

    }
}

