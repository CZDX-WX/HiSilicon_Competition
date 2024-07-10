package com.czdxwx.comptition.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ConvertUtil {
    public static void convertFormat(String inputFilePath, String outputFilePath, Runnable onComplete) throws IOException, InterruptedException {
        String[] cmd = {
                "ffmpeg",
                "-f", "s16le",        // input format: signed 16-bit little-endian PCM
                "-ar", "16000",       // audio sample rate: 16kHz
                "-i", inputFilePath,
                "-c:a", "aac",        // AAC audio codec
                "-b:a", "48k",        // audio bitrate
                outputFilePath,
                "-y"

        };

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 创建线程处理标准输出流
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }).start();

            // 创建线程处理错误输出流
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }).start();

            int exitCode = process.waitFor();
            log.info("************** FFmpeg process exited with code ******************* {}", exitCode);

            // 执行完格式转换后调用回调函数
            onComplete.run();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
