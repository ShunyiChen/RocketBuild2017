package com.rocket.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.codehaus.jettison.json.JSONObject;

import ch.qos.logback.core.encoder.Encoder;

public class Test {

	public static void main(String[] args) throws IOException {
		String name = "file2.jpg";
		try {
            URL url = new URL("http://172.16.52.78:10020/files");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");

            JSONObject postObj = new JSONObject();
            postObj.put("filename", name);
            //转换为字节数组
            byte[] data = (postObj.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            
            conn.setDoOutput(true);
            // 开始连接请求
            conn.connect();

            OutputStream o = conn.getOutputStream();
            // 写入请求的字符串
            o.write((postObj.toString()).getBytes());
            o.flush();
            o.close();

            InputStream inputStream = conn.getInputStream();
            BufferedInputStream bin = new BufferedInputStream(inputStream);

            OutputStream out = new FileOutputStream("C:\\CCProxy\\"+name);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                // System.out.println("下载了-------> " + len * 100 / fileLength +
            }
            out.flush();
            bin.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}   
}
