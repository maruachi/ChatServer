package com.chatserver.dgyim;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class LoggerTest {
    @Test
    void LogTest() {
//        given
        UserLog userLog = new UserLog("donggyu", LocalDate.of(1993, 8, 2));

//        when
        try (Logger logger = Logger.createByUserLog(userLog);){
            logger.log("message");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        actual을 얻기 위해서 file을 읽는 작업을 수행해야 한다.
//        fos를 메모리로 돌리는 방법을 고민해봤다. console 스트림을 메모리로 돌리는 것처럼 말이다.
//        그런데 딱히 방법이 보이지 않고 그냥 inputstream 열어서 읽어야할 것 같다.
//        먼가 이건 아니다 싶은데 떠올르는 방법이 이것밖에 없으니 일단 이렇게 해보자
//        아래에 코드가 커서 테스트하려고하는 게 명확히 보이지 않고 아래의 코드도 테스트해야할 것 같은 느낌??
//        이런 건 어떻게 해결해야 하나,,
//        그렇다고 함수로 감싸서 이 코드를 테스트하고 사용하기에는 "테스트를 위해 함수를 새로 만들지 마라"의 원칙이 깨진다.
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(userLog.toFilename());
             BufferedInputStream bis = new BufferedInputStream(inputStream, 8192);
             Reader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);) {
            while (true) {
                int len = reader.read(buffer);
                if (len == -1) {
                    break;
                }
                stringBuilder.append(buffer, 0, len);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

//        then
//        결과가 테스트 실패로 나오는데 그 이유를 모르겠다... ㅠ
        Assertions.assertThat(stringBuilder.toString()).isEqualTo("message");
    }

//    logger 테스트해야할 사항들 정리!!
//    날짜가 바뀌면 다른 파일에 로그를 쓰는가?
//    날짜가 동일하면 같은 파일에 로그를 쓰는가?
//    위에 테스트를 진행하는 게 좋아 보인다. 그런데 문제가 좀 어려워보인다.
//    log() 함수가 같은 파일에 쓰는지 어떻게 검사하지?
//    만약에 log(A) log(B) 이렇게 적은 다음에 같은 파일에 AB가 있는지 각각의 파일에 A, B가 있는지 검사하는 식으로 해야 하나?
}
