package com.zca.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author Altria
 * Date: 11/10/2019 下午 4:00
 */
public class Dospatcher implements Runnable {
    private Socket client;
    private Request request;
    private Response response;
    public Dospatcher(Socket client) {
        this.client = client;
        try {
            // 获取请求协议
            request = new Request(client);
            // 获取响应协议
            response = new Response(client);
        } catch (IOException e) {
            release();
        }
    }

    @Override
    public void run() {
        try {
            Servlet servlet = new WebApp().getServletFromUrl(request.getUrl());
            if (null == request.getUrl() || request.getUrl().equals("")){
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
                response.print(new String(is.readAllBytes()));
                response.pushToBrowser(200);
                is.close();
            }
            if (null!=servlet){
                // 关注了状态码
                servlet.service(request, response);
                response.pushToBrowser(200);
            }else{
                // 错误页面
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
                response.print(new String(is.readAllBytes()));
                response.pushToBrowser(404);
                is.close();
            }
        } catch (IOException e) {
            try {
                response.print("服务器错误");
                response.pushToBrowser(505);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        release();
    }

    private void release(){
        try {
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
