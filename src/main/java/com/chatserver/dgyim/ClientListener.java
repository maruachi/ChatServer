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

//    listen()은 항상 유효한 return만 준다는 설정
//    만약에 while이 없다면 예외 처리를 어떻게 하는 게 좋을까?
    public Socket listen() {
        while (true) {
//            try {
//                return serverSocket.accept();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                return serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
//                exception처리로 상위에서 관리하도록 하는 게 좋음
//                코드의 의도가 잘 안 보임
            }
        }
    }
}
