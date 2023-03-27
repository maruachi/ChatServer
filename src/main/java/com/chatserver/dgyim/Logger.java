package com.chatserver.dgyim;

import java.io.*;
import java.time.LocalDate;

public class Logger implements Closeable{
    private UserLogFilename userLogFilename;
    private Writer writer;

    public Logger(UserLogFilename userLogFilename, Writer writer) {
        this.userLogFilename = userLogFilename;
        this.writer = writer;
    }

//    createByUsername에서 createByLogFilename으로 변경
//    username은 logfilename 내부 데이터에 해당한다. logger가 알필요 없는 내용이다.
    public static Logger createByUserLog(UserLogFilename userLogFilename) {
//        return new Logger(userLogFilename, IoUtils.createFileWriter(userLogFilename.getValue()));
        return new Logger(userLogFilename, userLogFilename.createWriter());
    }

    public void log(String message) {
//        nowtime 읽고
//        noewtime이 기존에 적힌 now타임 비교했을 때 변했냐 안변했냐

        LocalDate localDate = LocalDate.now();
//        안변했다면 기존에 있던 스트림 날라주고
//        변했다면 새로은 스트림 생성 하고 프럴쉬 나우타임 새로설정
        if (userLogFilename.isNotSameDate(localDate)) {
//            로그파일 재성성
//            플러시
            userLogFilename = userLogFilename.changeDate(localDate);
            try (Writer writer = this.writer) {
                writer.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

//            아래보다 위쪽 구조가 좋다. 그 이유는 writer를 생성 이라는 기능은 logger와 logFilename 중에 logger에게 더 가깝기 때문이다.
//            createLogWriter는 그런 이유로 logger의 static 함수로 뺐다.
//            writer = IoUtils.createFileWriter(userLogFilename.getValue());
//            writer = logFilename.createLogWriter();
            writer = userLogFilename.createWriter();

//            파일이름 생성
//            플러시
//            인스턴스 스트림 새로 생성
        }

        try {
            writer.write(message);
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        메세지 작성
//        인스턴스 가지고 있는 라잇작업
    }

    @Override
    public void close() throws IOException {
//        인스턴스 스트림을 클로즈
        writer.close();
    }
}
