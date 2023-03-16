package com.chatserver.dgyim;

import java.io.BufferedReader;
import java.io.IOException;

public class LoginProcess {
    private final int tryCount;
    private final LoginManager loginManager;
    private int failCount = 0;

    public LoginProcess(int tryCount, LoginManager loginManager) {
        this.tryCount = tryCount;
        this.loginManager = loginManager;
    }

    public boolean tryLogin(String loginLine) {
        if (!hasMoreTry()) {
            return false;
        }

        String[] lineElement = loginLine.split("[ ]+");
        if (lineElement.length != 2) {
            failCount++;
            return false;
        }

        String username = lineElement[0];
        String password = lineElement[1];

        boolean isAuthentication = loginManager.authentication(username, password);
        if (!isAuthentication) {
            failCount++;
        }
        return isAuthentication;
    }

    public boolean hasMoreTry() {
        return failCount <= tryCount;
    }
}
