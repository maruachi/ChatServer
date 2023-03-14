package com.chatserver.dgyim;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener {
    private final ServerSocket serverSocket;

    private ClientListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static ClientListener createByPort(int port) {
        try {
            return new ClientListener(new ServerSocket(port));
        } catch (IOException e) {
//            listener가 실행이 안 되면 아예 프로그램이 도는 게 의미없기 때문에
//            프로그램을 터트리는 게 좋은 방향
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Socket listen() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                return socket;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
