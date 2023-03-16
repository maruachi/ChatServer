package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserLogFilenameTest {
    @Test
    void testIsNotSameDate() {
        UserLogFilename logFilename = new UserLogFilename("donggyu", LocalDate.now());
        LocalDate localDate = LocalDate.of(1993, 8, 2);
        boolean actual = logFilename.isNotSameDate(localDate);

        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void testChangeDate() {
        UserLogFilename logFilename = new UserLogFilename("donggyu", LocalDate.now());
        LocalDate localDate = LocalDate.of(1993, 8, 2);

//        actual과 expect -> 테스트할 값이 actual -> 메소드를 통해서 나온 값
//        expect는 정답 데이터를 내가 생성
//        동일한 이름의 파일경로에 파일에 대한 스트림을 생성하는 경우에 여러개가 생성 되나??
        UserLogFilename newLogFilename = logFilename.changeDate(localDate);

        boolean actual = newLogFilename.isNotSameDate(localDate);

        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void testToFilename() {
        UserLogFilename userLogFilename = new UserLogFilename("donggyu", LocalDate.of(1993,8,2));

        boolean actual = "donggyu_19930802.log".equals(userLogFilename.getValue());

        Assertions.assertThat(actual).isTrue();
    }
}