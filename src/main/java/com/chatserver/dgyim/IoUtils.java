package com.chatserver.dgyim;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IoUtils {
//    UserLogFilename -> String으로 받는 파라미터를 바꿨다.
//    더 범용적으로 사용할 수 있는 FileWriter가 등장했다.
//    String으로 주는 게 나을까 outputStream으로 주는 게 나을까?
    public static Writer createFileWriter(String filename) {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream = new FileOutputStream(filename, true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return toWriter(outputStream);
    }
//    private static DateTimeFormatter HHMMSS_FORMMATER = DateTimeFormatter.ofPattern("hh:mm:ss");
//    처음에는 append 기능 때문에 logger에 종속시킬려고 했지만 append는 logger가 가지는 핵심 개념은 아니다.
//    단순 시스템 처리의 기능의 하나로서 이해하는 게 더 자연스럽다.
//    그래서 logger에 있더는 IO 메소드를 유틸로 뺐다.
    public static Writer createWriter(UserLogFilename logFilename) {

//        프로그램을 유지시키기 위해 빈 stream을 리턴하도록 함. 기능은 동작 안 할테지만 서비스는 계속 유지가 된다.
//        기본값을 null로 설정하고 catch에서 빈 스트림을 생성할려고 했지만, 초반에 빈스트림으로 설정해두는 게 더 안정적인 코딩!
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            String filename = logFilename.getValue();
            outputStream = new FileOutputStream(filename, true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
//            writer가 생성이 제대로 안 됐다는 건 log를 적지 못한다는 상황이다.
//            이런 경우에는 시스템에 상황을 알려주는 게 좋다. Log가 쌓이지 않는 상황이라고.
//            그리고 log가 쌓이지 않는 것이 시스템에서 치명적인 장애가 아니기 때문에 운영은 유지하는 것이 좋다.

//            logger에서 빠지면서 log 개념을 잃어버렸으니 아래와 같은 문구는 적합하지 않다. 그래서 코드를 삭제
//            String nowTime = HHMMSS_FORMMATER.format(LocalTime.now());
//            System.out.println(MessageFormat.format("[{0}] {1} 로그 작성이 불가합니다.", nowTime, filename));
        }

        return toWriter(outputStream);
    }

    private static Writer toWriter(OutputStream outputStream) {
        OutputStream bos = new BufferedOutputStream(outputStream, 8192);
        return new OutputStreamWriter(bos, StandardCharsets.UTF_8);
    }

    public static BufferedReader toBufferedReader(InputStream inputStream) {
        BufferedInputStream bis = new BufferedInputStream(inputStream, 8192);
        InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
        return new BufferedReader(reader, 8192);
    }
}
