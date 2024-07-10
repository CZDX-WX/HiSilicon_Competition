package com.czdxwx.comptition.controller;


import com.czdxwx.comptition.entity.*;
import com.czdxwx.comptition.model.Polyline;
import com.czdxwx.comptition.service.OSSService;
import com.czdxwx.comptition.service.PolylineService;
import com.czdxwx.comptition.service.RestTemplateService;
import com.czdxwx.comptition.utils.CoordinateTransformUtil;
import com.czdxwx.comptition.utils.IfasrUtil;

import com.czdxwx.comptition.utils.WebTtsWsUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;


import static com.czdxwx.comptition.utils.ConvertUtil.convertFormat;

@Slf4j
@RestController
@RequestMapping("/Audio")
public class AudioController {

    //! http服务
    @Autowired
    private RestTemplateService restTemplateService;

    //! oss服务
    @Autowired
    private OSSService ossService;
    @Autowired
    private PolylineService polylineService;

    @PostMapping("/getRouteByAudio")
    public String processAudio(@RequestBody AudioBase64AndOrigin audioBase64AndOrigin) throws Exception {
        // 解码的文件路径
        String AUDIO_PATH = "input_file.aac";

        // 将Base64编码的音频数据解码为字节数组
        byte[] audioData = Base64.getDecoder().decode(audioBase64AndOrigin.getAudioBase64());

        try {
            FileOutputStream fos = new FileOutputStream(AUDIO_PATH);
            fos.write(audioData);
            fos.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 获取语音识别结果
        String res = IfasrUtil.upload(AUDIO_PATH);
        // 获取任务id
        String oderID = res.substring(res.indexOf("\"orderId\":") + 11, res.indexOf("\",\"taskEstimateTime\"", 1));
        // 获取任务返回结果
        String result = IfasrUtil.getResult(oderID);


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result);
        // 将 orderResult 字符串再次解析为 JSON 对象
        JsonNode orderResultNode = objectMapper.readTree(rootNode.path("content").path("orderResult").asText());
        // 获取 content 节点下的 lattice2 数组
        JsonNode lattice2Array = orderResultNode.path("lattice2");
        // 用于存放拼接后的字符串
        StringBuilder sb = new StringBuilder();
        // 遍历 lattice2 数组 获取 单词 数组并拼接成完整句子
        for (JsonNode lattice2Node : lattice2Array) {
            // 获取每个 lattice2 节点下的 ws 数组
            JsonNode wsArray = lattice2Node.path("json_1best").path("st").path("rt").get(0).path("ws");
            // 遍历 ws 数组中的每个 cw 对象
            for (JsonNode wsNode : wsArray) {
                JsonNode cwArray = wsNode.path("cw");
                for (JsonNode cwNode : cwArray) {
                    //? 获取 cw 对象中的 w 字段的值并追加到 StringBuilder
                    String wValue = cwNode.path("w").asText();
                    sb.append(wValue);
                }
            }
        }

        // 打印识别的句子是什么
        log.info("语音识别的结果为：{}", sb.toString());
        String ttttt=restTemplateService.getWgs84(audioBase64AndOrigin.getOrigin()).getLocations();
        log.info("语音识别的结果为：{}", ttttt);
        // 调用 高德关键字搜索api 获取准确的地点名
        TipInfo tipInfo = restTemplateService.getTipInfoJson(sb.toString(), ttttt);
        log.info("关键字搜索api的结果为：{}", tipInfo.toString());
        // 调用 高德周边搜索api 获取最近的符合条件地点
        Destination destination = restTemplateService.getDestinationJson(ttttt, tipInfo.getTips().get(0).getName());
        log.info("目的地搜索api的结果为：{}",destination.toString());
        Route route = restTemplateService.getRouteJson(ttttt, destination.getPois().get(0).getLocation());
        List<Step> steps = route.getRoute().getPaths().get(0).getSteps();

        // 获取授权后的WebSocket URL，并将https替换为wss
        String wsUrl = WebTtsWsUtil.getAuthUrl().replace("https://", "wss://");

        //第0段语音
        FileOutputStream pcm0 = new FileOutputStream("path0.pcm");
        WebTtsWsUtil.websocketWork(wsUrl,pcm0, "path0.pcm","您当前要去的地方是"+destination.getPois().get(0).getName()+"吗? 距离"+destination.getPois().get(0).getDistance()+"米。", () -> {
            // 执行文件格式转换，并在转换完成后执行上传操作
            try {
                convertFormat("path0.pcm", "path0.aac", () -> {
                    // 文件格式转换完成后执行上传操作
                    ossService.uploadAudio(new File("path0.aac"),"route/" + audioBase64AndOrigin.getTime() + "/path0");
                });
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            for (int i = 1; i <= steps.size(); i++) {
                //pcm文件保存路径
                String PCM_PATH = "path" + i + ".pcm";
                //OSS保存路径
                String OSS_PATH = "route/" + audioBase64AndOrigin.getTime() + "/path" + i;
                //acc文件保存路径
                String AAC_PATH = "path" + i + ".aac";

                final int index = i;

                try{
                    FileOutputStream os = new FileOutputStream(PCM_PATH);
                    // 使用CountDownLatch来等待WebSocket处理完成
                    CountDownLatch latch = new CountDownLatch(1);
                    // 在WebSocket处理中设置标志和释放CountDownLatch
                    Thread webSocketThread = new Thread(() -> {
                        try {
                            // 执行WebSocket工作
                            WebTtsWsUtil.websocketWork(wsUrl, os, PCM_PATH, "   "+steps.get(index-1).getInstruction(), () -> {
                                // 执行文件格式转换，并在转换完成后执行上传操作
                                try {
                                    convertFormat(PCM_PATH, AAC_PATH, () -> {
                                        // 文件格式转换完成后执行上传操作
                                        ossService.uploadAudio(new File(AAC_PATH), OSS_PATH);
                                    });
                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } finally {
                            // WebSocket处理完成后释放Latch
                            latch.countDown();
                        }
                    });
                    webSocketThread.start();
                    // 等待WebSocket处理完成
                    try {
                        latch.await(); // 等待WebSocket处理线程完成
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error(e.getMessage());
                    }

                    String[] polys = steps.get(index-1).getPolyline().split(";");

                    polylineService.save(new Polyline(OSS_PATH,CoordinateTransformUtil.gcj02ToWgs84(polys[0])+"-"+CoordinateTransformUtil.gcj02ToWgs84(polys[polys.length - 1])));

                    // 执行上传操作
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
            }
            return "pathNum:"+steps.size();
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}

