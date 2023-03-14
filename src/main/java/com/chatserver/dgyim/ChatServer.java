package com.chatserver.dgyim;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static final int PORT = 7777;
//    chatServer는 server가 본체이다
//    chatServer는 client의 연결을 관리한다.
//    chatServer는 server로서의 기능의 on-off 역할을 한다.
//    chatServer는 server를 정상적으로 구동하고 정상적으로 종료하는 역할을 수행한다.

    public void run() {
//        listener를 수행한다. listener는 클라이언트와의 연결을 담당한다.
        ClientListener clientListener = ClientListener.createByPort(PORT);

        while (true) {
//        listen이 성공했을 때는 clientSocket을 return한다.
            Socket clientSocket = clientListener.listen();

//        loginManager: client의 로그인을 담당한다.
            LoginManager loginManager = LoginManager.createByClientSocket(clientSocket);
            boolean isLogin = loginManager.login();


        }

//        chatHandler: chat을 연결한다. 연결된 chat은 계속해서 log에 쌓인다. chatLogger도 고민 중
//        그런데 client로 부터 요청을 handdling하는 게 핵심이라 handler가 나을 꺼 같음

//        chatShell: shell의 chat 명령어를 담당한다.
//        broadcast: shell 명령어 중 하나이다. all, send 커맨드를 처리한다.
    }

    public void close() {

    }
}