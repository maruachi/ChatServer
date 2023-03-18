package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

//로그인 프로세스는 try count만큼의 로그인 시도에 대해서 성공 여부를 알려준다.
//trycount를 초과했을 때
//올바른 input값이 주어졌을 때 로그인이 성공하는가?
class LoginProcessTest {
    @ParameterizedTest
    @CsvSource({
            "dong, 123, dong 123, true",
            "dong, 123, gyu 123, false"
    })
    void 계정_등록_여부에_따른_로그인_테스트(String registeredUsername, String registeredPassword, String loginLine, boolean expect) {
        //given
        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register(registeredUsername, registeredPassword);
        LoginProcess loginProcess = new LoginProcess(2, loginManager);

        //when
        boolean actual = loginProcess.tryLogin(loginLine);

        //then
        Assertions.assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 허용된_시도횟수_이상의_로그인_시도를_할_때_로그인이_거부되는지_테스트() {
        //given
        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register("dong", "123");
        int limitTryCount = 2;
        LoginProcess loginProcess = new LoginProcess(2, loginManager);
        String loginLine = "gyu 123";

        //when
        int tryCount = 0;
        while (tryCount < limitTryCount) {
            loginProcess.tryLogin(loginLine);
            tryCount++;
        }
        boolean actual = loginProcess.tryLogin(loginLine);

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void 허용된_시도횟수_이하일_때_추가로_시도할_수_있는지_테스트() {
        //given
        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register("dong", "123");
        int limitTryCount = 2;
        LoginProcess loginProcess = new LoginProcess(2, loginManager);

        boolean actual = loginProcess.hasMoreTry();

        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void 허용된_시도횟수_이상일_때_추가로_시도할_수_없는지_테스트() {
        //given
        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register("dong", "123");
        int limitTryCount = 2;
        LoginProcess loginProcess = new LoginProcess(2, loginManager);
        String loginLine = "gyu 123";

        //when
        int tryCount = 0;
        while (tryCount <= limitTryCount) {
            loginProcess.tryLogin(loginLine);
            tryCount++;
        }
        boolean actual = loginProcess.hasMoreTry();

        //then
        Assertions.assertThat(actual).isFalse();
    }
}