package com.czdxwx.comptition.utils;



import com.czdxwx.comptition.sign.LfasrSignature;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.security.SignatureException;
import java.util.HashMap;

@Slf4j
public class IfasrUtil {

    private static final String HOST = "https://raasr.xfyun.cn";
    private static final String appid="17d4db7a";
    private static final String keySecret="1f3b11ba930ffd9fc336e4d2718cfcdd";
    private static final Gson gson = new Gson();

    //上传语音任务
    public static String upload(String AUDIO_FILE_PATH) throws SignatureException, IOException, UnsupportedAudioFileException {

        HashMap<String, Object> map = new HashMap<>(16);
        File audio = new File(AUDIO_FILE_PATH);
        String fileName = audio.getName();
        long fileSize = audio.length();
        map.put("appId", appid);
        map.put("fileSize", fileSize);
        map.put("fileName", fileName);
        map.put("duration", 5);
        LfasrSignature lfasrSignature = new LfasrSignature(appid, keySecret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());

        String paramString = HttpUtil.parseMapToPathParam(map);
        log.info("upload paramString:{}", paramString);

        String url = HOST + "/v2/api/upload" + "?" + paramString;
        log.info("upload_url:{}", url);
        String response = HttpUtil.iflyrecUpload(url, new FileInputStream(audio));

        log.info("upload response:{}", response);
        return response;
    }

    public static String getResult(String orderId) throws SignatureException, InterruptedException, IOException {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("orderId", orderId);
        LfasrSignature lfasrSignature = new LfasrSignature(appid, keySecret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());
        map.put("appId", appid);
        map.put("resultType", "transfer,predict");
        String paramString = HttpUtil.parseMapToPathParam(map);
        String url = HOST + "/v2/api/getResult" + "?" + paramString;

        log.info("\nget_result_url:{}", url);

        while (true) {
            String response = HttpUtil.iflyrecGet(url);
            JsonParse jsonParse = gson.fromJson(response, JsonParse.class);
            if (jsonParse.content.orderInfo.status == 4 || jsonParse.content.orderInfo.status == -1) {
                log.info("订单完成:{}", response);

                write(response);
                return response;
            } else {
                log.info("进行中...，状态为:{}", jsonParse.content.orderInfo.status);
                //建议使用回调的方式查询结果，查询接口有请求频率限制
                Thread.sleep(7000);
            }
        }
    }

    public static void write(String resp) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
        String ss = resp;
        bw.write(ss);
        //关闭流
        bw.close();
        log.info("写入txt成功");
    }

    class JsonParse {
        Content content;
    }

    class Content {
        OrderInfo orderInfo;
    }

    class OrderInfo {
        Integer status;
    }

}
