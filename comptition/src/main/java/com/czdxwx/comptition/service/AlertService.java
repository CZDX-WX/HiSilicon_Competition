package com.czdxwx.comptition.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.czdxwx.comptition.utils.PushMsg;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    public void pushAlert(String alert) {
        String master_secret = PushMsg.MASTER_SECRET;
        String app_key = PushMsg.APP_KEY;
        JPushClient jpushClient = new JPushClient(master_secret, app_key, null, ClientConfig.getInstance());
        PushPayload payload = PushMsg.buildPushObject_all_all_alert("消息推送");
        //PushPayload payload=PhicommPush.buildPushObject_android_tag_alertWithTitle("tag1", "123", "123", null);

        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            System.out.print("Connection error, should retry later " + e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            System.out.println("根据返回的错误信息核查请求是否正确" + e);
            System.out.println("HTTP 状态信息码: " + e.getStatus());
            System.out.println("JPush返回的错误码: " + e.getErrorCode());
            System.out.println("JPush返回的错误信息: " + e.getErrorMessage());
        }
    }
}
