package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LoginManagerTest {
    @Test
    void testUnregisteredUsername() {
//        given
        LoginManager loginManager = LoginManager.createEmpty();
        String username = "dong";
        String password = "123";

//        when
        loginManager.register(username, password);

//        then
        boolean actual = loginManager.authentication(username, password);
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void 유저가_이미_등록됬을_때_등록이_안_되는지를_테스트() {
        // given
        LoginManager loginManager = LoginManager.createEmpty();
        String username = "dong";
        String password = "123";
        String otherPassword = password + 1;
        loginManager.register(username, password);

//        when
        loginManager.register(username, otherPassword);

//        then
        boolean actual = loginManager.authentication(username, otherPassword);
        Assertions.assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "dong, 123, dong, 123, true",
            "dong, 123, Dong, 123, false",
            "dong, 123, dong, 124, false",
            "dong, 123, Dong, 124, false",
    })
    void 이미_등록된_유저가_있을_때_입력에_따른_입력값을_테스트(String registerdUsername, String registerdPassword,
                                         String username, String passwoord, boolean expectAuthenticate) {
        //given
        LoginManager loginManager = LoginManager.createEmpty();
        loginManager.register(registerdUsername, registerdPassword);

        //when
        boolean actual = loginManager.authentication(username, passwoord);

        //then
        Assertions.assertThat(actual).isEqualTo(expectAuthenticate);
    }
}