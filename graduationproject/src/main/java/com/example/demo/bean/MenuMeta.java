package com.example.demo.bean;

import java.io.Serializable;

public class MenuMeta implements Serializable {

    private boolean requireAuth;
    private boolean keepAlive;

    public boolean isRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
