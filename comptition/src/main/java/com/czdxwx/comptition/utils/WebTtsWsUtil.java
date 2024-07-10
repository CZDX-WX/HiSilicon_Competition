package com.czdxwx.comptition.utils;

import cn.hutool.cron.Scheduler;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 语音合成流式 WebAPI 接口调用示例 接口文档（必看）：https://www.xfyun.cn/doc/tts/online_tts/API.html
 * 发音人使用方式：登陆开放平台https://www.xfyun.cn/后，到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 小语种需要传输小语种文本、使用小语种发音人vcn、tte=unicode以及修改文本编码方式
 * @author xlliu24
 */

@Slf4j
public class WebTtsWsUtil {

    // 地址与鉴权信息
    private static final String HOST_URL = "https://tts-api.xfyun.cn/v2/tts";
    private static final String APPID = "隐秘不展示";
    private static final String API_SECRET = "隐秘不展示";
    private static final String API_KEY = "隐秘不展示";
    private static final String TTE = "UTF8";
    private static final String VCN = "aisxping";
    private static final Gson GSON = new Gson();

    private static boolean outputStreamClosed = false;
//    public static boolean wsCloseFlag = false;

    public static void websocketWork(String wsUrl, OutputStream outputStream, String outputFilePath, String text,Runnable onComplete) {
        try {
            URI uri = new URI(wsUrl);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("ws建立连接成功...");
                }

                @Override
                public void onMessage(String message) {
                    JsonParse myJsonParse = GSON.fromJson(message, JsonParse.class);
                    if (myJsonParse.code != 0) {
                        log.info("发生错误，错误码为：{}", myJsonParse.code);
                        log.info("本次请求的sid为：{}", myJsonParse.sid);
                    }

                    if (myJsonParse.data != null) {
                        try {
                            byte[] textBase64Decode = Base64.getDecoder().decode(myJsonParse.data.audio);
                            outputStream.write(textBase64Decode);
                            outputStream.flush();
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        if (myJsonParse.data.status == 2) {
                            try {
                                if (!outputStreamClosed) {
                                    outputStream.close();
                                    outputStreamClosed = true;
                                }
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                            log.info("本次请求的sid==>{}", myJsonParse.sid);
                            log.info("合成成功，文件保存路径为==>{}", outputFilePath);
//                            // 可以关闭连接，释放资源
//                            wsCloseFlag = true;
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    // 执行完格式转换后调用回调函数
                    onComplete.run();
                    log.info("ws链接已关闭，本次请求完成...");
                }

                @Override
                public void onError(Exception e) {
                    log.info("发生错误 {}", e.getMessage());
                }
            };
            log.info("连接情况：{}",webSocketClient.connectBlocking() ?"连接还在":"连接关闭");
            while (!webSocketClient.getReadyState().equals(ReadyState.OPEN)) {
                log.info("正在连接...");
                Thread.sleep(100);
            }
            MyThread webSocketThread = new MyThread(webSocketClient, text);
            webSocketThread.start();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    public static String getAuthUrl() throws Exception {
        URL url = new URL(HOST_URL);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        String preStr = "host: " + url.getHost() + "\n" + "date: " + date + "\n" + "GET " + url.getPath() + " HTTP/1.1";
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(API_SECRET.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", API_KEY, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();
        return httpUrl.toString();
    }

    private static class MyThread extends Thread {
        private final WebSocketClient webSocketClient;
        private final String text;

        public MyThread(WebSocketClient webSocketClient, String text) {
            this.webSocketClient = webSocketClient;
            this.text = text;
        }

        @Override
        public void run() {
            log.info("需要合成的语音为：{}", text);
            try {
                String requestJson = "{\n" +
                        "  \"common\": {\n" +
                        "    \"app_id\": \"" + APPID + "\"\n" +
                        "  },\n" +
                        "  \"business\": {\n" +
                        "    \"aue\": \"raw\",\n" +
                        "    \"tte\": \"" + TTE + "\",\n" +
                        "    \"ent\": \"intp65\",\n" +
                        "    \"vcn\": \"" + VCN + "\",\n" +
                        "    \"pitch\": 50,\n" +
                        "    \"speed\": 50,\n" +
                        "    \"auf\": \"audio/L16;rate=16000\"\n" +
                        "  },\n" +
                        "  \"data\": {\n" +
                        "    \"status\": 2,\n" +
                        "    \"text\": \"" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)) + "\"\n" +
                        "  }\n" +
                        "}";

                //! 发送消息
                webSocketClient.send(requestJson);
                Timer timer = new Timer();
                // 创建一个定时任务，延迟3秒后执行
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        webSocketClient.close();
                    }
                };
                // 安排任务执行，延迟60秒后开始执行，然后每隔5秒执行一次
                timer.schedule(task, 70000);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private static class JsonParse {
        int code;
        String sid;
        Data data;
    }

    private static class Data {
        int status;
        String audio;
    }
}

