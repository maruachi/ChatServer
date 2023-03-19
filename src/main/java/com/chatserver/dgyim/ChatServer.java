package com.chatserver.dgyim;

import javax.naming.ldap.SortKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketPermission;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatServer {
    public static final int PORT = 7777;
//    chatServer는 server가 본체이다
//    chatServer는 client의 연결을 관리한다.
//    chatServer는 server로서의 기능의 on-off 역할을 한다.
//    chatServer는 server를 정상적으로 구동하고 정상적으로 종료하는 역할을 수행한다.

    public void run() {
        HashMap<String, Socket> loginUserSockets = new HashMap<>();
//        listener를 수행한다. listener는 클라이언트와의 연결을 담당한다.
        ClientListener clientListener = ClientListener.createByPort(PORT);

        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register("dong", "123");

        while (true) {
//        listen이 성공했을 때는 clientSocket을 return한다.
            Socket clientSocket = clientListener.listen();

            LoginProcess loginProcess = new LoginProcess(2, loginManager);

            //여러 클라이언트가 동시 접속하는 경우에 한명의 클라이언트를 처리해야한 진행됨
            //동시 처리를 위해 쓰레드로 감쌀 필요가 있다.
            //이후에 변경 예정
            boolean isLogin = false;
            String loginUsername = null;
            try {
                BufferedReader reader = IoUtils.toBufferedReader(clientSocket.getInputStream());
                while (loginProcess.hasMoreTry()) {
                    //client와 chat을 여는 부분에서 username이 필요해짐
                    //parseLoginLine을 통해 처리하는 방향이 맞는가??
                    String loginLine = reader.readLine();
                    String[] lineElements = loginLine.split("[ ]+");
                    if (lineElements.length != 2) {
                        continue;
                    }
                    String username = lineElements[0];
                    isLogin = loginProcess.tryLogin(reader.readLine());
                    if (isLogin) {
                        loginUsername = username;
                        loginUserSockets.put(username, clientSocket);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (isLogin) {
                //client와 chat을 연다. 클라이언트의 챗을 로그로 남긴다.
                try (Logger logger = Logger.createByUserLog(new UserLogFilename(loginUsername, LocalDate.now()))) {
                    BufferedReader reader = IoUtils.toBufferedReader(clientSocket.getInputStream());
                    while (true) {
                        String chatLine = reader.readLine();
                        if (chatLine == null) {
                            break;
                        }
                        logger.log(chatLine);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            //server 명령어 기능 구현
            //line 받기
            //parsing하기
            //command에 따라 메세지 전송 선택하기
            //ServerShell
            //명령어(?)
            //몰라 일단 잘 모르겠으니 통째로 구현!!
            while (true) {
                BufferedReader reader = IoUtils.toBufferedReader(System.in);
                try {
                    //line에 뒷 요소가 메세지라는 건 공통의 요소!
                    String line = reader.readLine();
                    int messageIndex = line.indexOf(' ');
                    if (messageIndex == -1) {
                        continue;
                    }
                    String message = line.substring(messageIndex + 1);
                    String targetUsername = line.substring(0, messageIndex);

                    List<Socket> targetSockets = new ArrayList<>();

                    if ("all".equals(targetUsername)) {
                        targetSockets.addAll(loginUserSockets.values());
                    }

                    if (loginUserSockets.containsKey(targetUsername)) {
                        Socket targetSocket = loginUserSockets.get(targetUsername);
                        targetSockets.add(targetSocket);
                    }

                    for (Socket targetSocket : targetSockets) {
                        Writer writer = IoUtils.toWriter(targetSocket.getOutputStream());
                        writer.write(message);
                        writer.write('\n');
                        writer.flush();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

//        chatHandler: chat을 연결한다. 연결된 chat은 계속해서 log에 쌓인다. chatLogger도 고민 중
//        그런데 client로 부터 요청을 handdling하는 게 핵심이라 handler가 나을 꺼 같음

//        chatShell: shell의 chat 명령어를 담당한다.
//        broadcast: shell 명령어 중 하나이다. all, send 커맨드를 처리한다.
    }

    public void close() {

    }
}
