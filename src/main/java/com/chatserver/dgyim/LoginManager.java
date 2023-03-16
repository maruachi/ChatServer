package com.chatserver.dgyim;

import java.util.HashMap;

// client의 로그인을 담당한다.
// 계정이 올바른지 확인하는 authentication
// 입력된 계정정보를 받아오고 login 성공여부를 알려주는 login
// 로그인이 성공했을 때 "서버에 입장하셨습니다" 멘트 발송 -> 이건 loginManager의 역할인가?
// "로그인에 성공하셨습니다" 라는 멘트면 loginManager의 역할인 거 같은데, 서버에 입장하셨습니다는 뭔가 느낌이 아닌 것 같다,,

// 계정 등록하기
// 인증하기

public class LoginManager {
    private final HashMap<String, String> accounts;

    public LoginManager(HashMap<String, String> accounts) {
        this.accounts = accounts;
    }

    public static LoginManager createEmpty() {
        return new LoginManager(new HashMap<>());
    }

    public void register(String username, String password) {
        if (accounts.containsKey(username)) {
            return;
        }
        accounts.put(username, password);
    }

    public boolean authentication(String username, String password) {
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }
}