package com.chatserver.dgyim;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class LoginManager {
    private static final HashMap<String, String> accounts = new HashMap<>();

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public LoginManager(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    {
        accounts.put("gyu", "123");
        accounts.put("dong", "123");
    }

    public static LoginManager createByClientSocket(Socket clientSocket) {
        try {
            return new LoginManager(clientSocket.getInputStream(), clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginManager(new ByteArrayInputStream(), new ByteArrayOutputStream());
        }
    }

    public boolean login() {
        BufferedReader reader = IoUtils.toBufferedReader(inputStream);
        int failCount = 0;
        while (failCount < 2) {
            try {
                String line = reader.readLine();
                String[] lineElements = line.split("[ ]+");

                if (lineElements.length != 2) {
                    failCount += 1;
                    continue;
                }

                String username = lineElements[0];
                String password = lineElements[1];

                if (!authenticate(username, password)) {
                    failCount += 1;
                    continue;
                }

                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                failCount += 1;
            }
        }

        return false;
    }

    private boolean authenticate(String username, String password) {
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }
}
