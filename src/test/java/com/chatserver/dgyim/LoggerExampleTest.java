package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoggerExampleTest {
    private static final PrintStream STANDARD_OUT = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


//    테스트가 실행되기 전에 beforeEach annotation을 가진 코드가 실행된다.
//    test를 하기 위해서 outputStream을 메모리로 돌려서 test 코드가 접근 가능하게 하기 위함이다.
    @BeforeEach
    void setUpConsoleStreamToByteArrayStream() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

//    테스트가 종료된 후에 afterEach annotation을 가진 코드가 실행된다.
//    테스트가 끝나고 스트림을 원상복귀 시켜놓는다.
    @AfterEach
    void rollbackConsoleStream() {
        System.setOut(STANDARD_OUT);
    }

    @Test
    void testSomething() {
//        given
        String consoleMsg = "Hello world";

//        when
        System.out.println(consoleMsg);

//        then
        String expect = outputStreamCaptor.toString().trim();
        Assertions.assertThat(consoleMsg).isEqualTo(expect);
    }
}
