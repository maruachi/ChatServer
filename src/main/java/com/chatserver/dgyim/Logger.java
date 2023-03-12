package com.chatserver.dgyim;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Logger implements Closeable{
    private static DateTimeFormatter HHMMSS_FORMMATER = DateTimeFormatter.ofPattern("hh:mm:ss");

    private LogFilename logFilename;
    private Writer writer;

    public Logger(LogFilename logFilename, Writer writer) {
        this.logFilename = logFilename;
        this.writer = writer;
    }

    public static Logger createByUsername(LogFilename logFilename) {
        return new Logger(logFilename, createLogWriter(logFilename));
    }
    //    날짜 변경점으로 파일 출력 지점을 시간에 따라 맞춰가야 함 -> 시간을 가지고 있으면 좋을듯
//    여러 유저에 대해서 파일이 여러개 생기는데 이것도 관리필요
//    유저와 시간에 따라서 파일이 여러개임 이거 관리해야 함 -> static filepool or userpool time에따라 그때 필요한 파일을 선택하는 식?
//    flush 모아서할 수 있도록
//    충돌 문제 고려해서 하기

    //    user와 message는 외부에 주어져야 함
    public void log(String message) {
//        nowtime 읽고
//        noewtime이 기존에 적힌 now타임 비교했을 때 변했냐 안변했냐

        LocalDate localDate = LocalDate.now();
//        안변했다면 기존에 있던 스트림 날라주고
//        변했다면 새로은 스트림 생성 하고 프럴쉬 나우타임 새로설정
        if (logFilename.isNotSameDate(localDate)) {
//            로그파일 재성성
//            플러시
            logFilename = logFilename.changeDate(localDate);
            try (Writer writer = this.writer) {
                writer.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

//            아래보다 위쪽 구조가 좋다. 그 이유는 writer를 생성 이라는 기능은 logger와 logFilename 중에 logger에게 더 가깝기 때문이다.
//            createLogWriter는 그런 이유로 logger의 static 함수로 뺐다.
            writer = createLogWriter(logFilename);
//            writer = logFilename.createLogWriter();

//            파일이름 생성
//            플러시
//            인스턴스 스트림 새로 생성
        }

        try {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        메세지 작성
//        인스턴스 가지고 있는 라잇작업
    }

//    이건 여기보다 util함수가 진짜 낫겠는데?
//    결국 하는 게 String filename가지고 file만들어주는 거다. 이런 시스템 처리에 가까운 녀석들은 utils로 모두의 공통함수로 만들어놓으면 좋다.
//    만들어 놓으면 다른 기능들에서도 쓰일테니 말이다.
//    아 그런데 생각해보니깐 FileOutputStream 생성할 때 append가 true로 설정되어 있는 것으로 로그를 남기는 특성과 연관이 되어 있다.
//    이런 경우라면 같은 파일 내에서 같은 클래스 내에서 관리하는 것이 좋다.
    private static Writer createLogWriter(LogFilename logFilename) {
//        toString을 써서 filename을 받는 게 적합한지??
        String filename = logFilename.toString();
        try {
            FileOutputStream fis = new FileOutputStream(filename, true);
            BufferedOutputStream bos = new BufferedOutputStream(fis, 8192);
            return new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            ioException.printStackTrace();
//            writer가 생성이 제대로 안 됐다는 건 log를 적지 못한다는 상황이다.
//            이런 경우에는 시스템에 상황을 알려주는 게 좋다. Log가 쌓이지 않는 상황이라고.
//            그리고 log가 쌓이지 않는 것이 시스템에서 치명적인 장애가 아니기 때문에 운영은 유지하는 것이 좋다.
            String nowTime = HHMMSS_FORMMATER.format(LocalTime.now());
            System.out.println(MessageFormat.format("[{0}] {1} 로그 작성이 불가합니다.", nowTime, filename));
//            프로그램을 유지하고 싶지만 일단 어떻게 처리해야할지 몰라 null 리턴하는 걸로 나둠
            return null;
        }
    }

    @Override
    public void close() throws IOException {
//        인스턴스 스트림을 클로즈
        writer.close();
    }
}
