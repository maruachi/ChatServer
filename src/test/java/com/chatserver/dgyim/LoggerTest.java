package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;

public class LoggerTest {
    @Test
    void testLog() {
        LogFilename logFilename = new LogFilename("donggyu", LocalDate.of(1993, 8, 2));
        Logger logger = Logger.createByUsername(logFilename);
        ByteArrayOutputStream logMessage = new ByteArrayOutputStream();

//        이부분이 잘 동작하지 않는 것 같음... 흠,,, file io를 테스트를 위해 메모리로 돌려야 함...
        try {
            System.setOut(new PrintStream(new FileOutputStream(logFilename.toString(), true)));
            logger.log("message");
            String stringLogFilename = new String(java.nio.file.Files.readAllBytes(Paths.get(logFilename.toString())));
            boolean actual = "message".equals(stringLogFilename);

//        테스트 중에 중간에 System.out으로 데이터 값 확인하는 방법??
//        System.out.println(logMessage.toString());
            Assertions.assertThat(actual).isTrue();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
