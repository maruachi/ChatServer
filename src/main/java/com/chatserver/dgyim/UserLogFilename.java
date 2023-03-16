package com.chatserver.dgyim;

import java.io.Writer;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 유저네임에 의해 식별되고 유저네임에 대한 로그와 관련이 깊다.
// 그렇기 때문에 LogFilename보나는 UserLog가 더 적합한 개념이다.
// LogUsername LogUser도 생각해봤는데, LogSomething 이런 순서로 오게되면 Something에 관한 로그가 이미 담겨 있을 것 같아서 순서를 반대로 했다.
// 예를들어 LogUsername이면 username을 모아놓은 클래스 같다.
// UserTimeLog도 생각해봤는데 이미 Log라는 거 자체가 time의 속성을 가진다.그래서 깔끔하게 UserLog가 적당하다.
public class UserLogFilename {
    //    유저네임에 의해서 식별됨
//    시간을 받아 로그 파일에 대한 스트림을 생성
    private static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String username;
    private final LocalDate localDate;

    public UserLogFilename(String username, LocalDate localDate) {
        this.username = username;
        this.localDate = localDate;
    }

//    userFilename, userLogFilename, logFilename
//    log에 해당하는 거니깐 log filename이 적합 userfilename이라고 하면 log가 아닌 정체성도 가질 수 있다.
//    user보다 log가 중요하므로 log를 남긴다.
//    userlogFilename하면 되지만 userLogFilename -> UserLogFilename 이런 모양새라 이게 string으로 변환되는지는 불분명하다
//    그러면 아예 string이라고 표기하는 건 어떨까?

//    stub -> 원하는 결과
//    mocking -> 객체를 감싸서 가상 환경
    public String getValue() {
        String stringDate = YYYYMMDD_FORMATTER.format(localDate);

        return MessageFormat.format("{0}_{1}.log", username, stringDate);
    }

    public Writer createWriter() {
        return IoUtils.createFileWriter(getValue());
    }

    public boolean isNotSameDate(LocalDate nowDate) {
        return !this.localDate.isEqual(nowDate);
    }

    public UserLogFilename changeDate(LocalDate localDate) {
        return new UserLogFilename(this.username, localDate);
    }
}
