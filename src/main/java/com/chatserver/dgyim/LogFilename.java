package com.chatserver.dgyim;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogFilename {
    //    유저네임에 의해서 식별됨
//    시간을 받아 로그 파일에 대한 스트림을 생성
    private static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String username;
    private final LocalDate localDate;

    public LogFilename(String username, LocalDate localDate) {
        this.username = username;
        this.localDate = localDate;
    }

//    createLogWriter가 util 함수에 더 가까 운 특성이라면, 결국 writer를 만들기위한 filename은 어떻게 넘겨줘야 할까?
//    개념으로서 접근을 해보자
//    filename을 생성하는 건 LogFilename이 담당하는 게 맞다.
//    return을 String으로 하는 건 좋은 선택인가?
//    만약에 String으로 넘겨주면 객체 끼리의 message passing 느낌이라기 보다는
//    LogFilename을 넘겨주는 Writer를 받는 쪽(?)에서 알아서 돌아가게 끔 하는 게 그림이 좋다
//    쓰는 작업은 누가해? Logger가 하는 거지 Logger는 그런 역할인 거야
//    그러면 Logger에서 writer를 생성해야 한다.

//    위와 같은 고민을 해보았지만, 결국에는 createLogWriter의 기능은 Logger로 옮기고 filename을 logFilename에서 toString()으로 만드는 것으로 결정
//    public Writer createLogWriter() {
//        String filename = createFilename();
//        try {
//            FileOutputStream fos = new FileOutputStream(filename, true);
//            BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);
//
//            return new OutputStreamWriter(bos, StandardCharsets.UTF_8);
//        } catch (FileNotFoundException fileNotFoundException) {
//            throw new RuntimeException(fileNotFoundException);
//        }
//    }

    private String createFilename() {
        String stringDate = YYYYMMDD_FORMATTER.format(localDate);

        return MessageFormat.format("{0}_{1}.log", username, stringDate);
    }

    public boolean isNotSameDate(LocalDate nowDate) {
        return !this.localDate.isEqual(nowDate);
    }

    public LogFilename changeDate(LocalDate localDate) {
        return new LogFilename(this.username, localDate);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}_{1}.log", username, YYYYMMDD_FORMATTER.format(localDate));
    }
}
