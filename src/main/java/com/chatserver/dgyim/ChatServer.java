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

//    chatServer는 server가 본체이다
//    chatServer는 client의 연결을 관리한다.
//    chatServer는 server로서의 기능의 on-off 역할을 한다.
//    chatServer는 server를 정상적으로 구동하고 정상적으로 종료하는 역할을 수행한다.
public class ChatServer {
    public static final int PORT = 7777;

    // client와 연결과 로그인을 담당하는 while
    // 로그인 성공 시에 클라이언트의 메세지를 수신을 담당하는 while
    // 서버의 챗을 전송하고 로그에 남기는 while
    public void run() {
        HashMap<String, Socket> loginUserSockets = new HashMap<>();

        ClientListener clientListener = ClientListener.createByPort(PORT);

        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register("dong", "123");

        while (true) {
            Socket clientSocket = clientListener.listen();

            Thread loginProcessThread = new Thread(()->{
                LoginProcess loginProcess = new LoginProcess(2, loginManager);

                boolean isLogin = false;
                String loginUsername = null;

                try {
                    BufferedReader reader = IoUtils.toBufferedReader(clientSocket.getInputStream());
                    while (loginProcess.hasMoreTry()) {
                        String loginLine = reader.readLine();
                        String[] lineElements = loginLine.split("[ ]+");
                        if (lineElements.length != 2) {
                            continue;
                        }
                        String username = lineElements[0];
                        String password = lineElements[1];

                        isLogin = loginProcess.tryLogin(username, password);
                        if (isLogin) {
                            loginUsername = username;
                            loginUserSockets.put(username, clientSocket);
                            break;
                        }
                    }

                    //client와 chat을 연다. 클라이언트의 챗을 로그로 남긴다.
                    if (isLogin) {
                        try (Logger logger = Logger.createByUserLog(new UserLogFilename(loginUsername, LocalDate.now()))) {
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
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            loginProcessThread.start();

            //server 명령어 기능 구현
            //line 받기
            //parsing하기
            //command에 따라 메세지 전송 선택하기
            //ServerShell
            //명령어(?)
            //몰라 일단 잘 모르겠으니 통째로 구현!!
            BufferedReader reader = IoUtils.toBufferedReader(System.in);
            while (true) {
                try {
                    //line에 뒷 요소가 메세지라는 건 공통의 요소!
                    String line = reader.readLine();
                    int messageIndex = line.indexOf(' ');
                    if (messageIndex == -1) {
                        continue;
                    }
                    String targetUser = line.substring(0, messageIndex);
                    String message = line.substring(messageIndex + 1);

                    List<String> targetUsernames = findTargetUsername(loginUserSockets, targetUser);

                    for (String targerUsername : targetUsernames) {
                        Socket targetSocket = loginUserSockets.get(targerUsername);
                        Writer writer = IoUtils.toWriter(targetSocket.getOutputStream());
                        writer.write(message);
                        writer.write('\n');
                        writer.flush();
                        try (Logger logger = Logger.createByUserLog(new UserLogFilename(targerUsername, LocalDate.now()))) {
                            logger.log(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private List<String> findTargetUsername(HashMap<String, Socket> loginUserSockets, String targetUser) {
        List<String> targetUsernames = new ArrayList<>();
        if ("all".equals(targetUser)) {
            targetUsernames.addAll(loginUserSockets.keySet());
        }

        if (loginUserSockets.containsKey(targetUser)) {
            targetUsernames.add(targetUser);
        }
        return targetUsernames;
    }

    public void close() {

    }
}
