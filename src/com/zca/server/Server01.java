package com.zca.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Altria
 * Date: 10/10/2019 下午 9:39
 */
public class Server01 {

    ServerSocket socket;

    public static void main(String[] args) {
        Server01 server = new Server01();
        server.start();
    }

    /**
     * 启动服务
     */
    public void start(){
        // 创建服务器端
        try {
            socket = new ServerSocket(8888);
            System.out.println("-----服务器启动-----");
            receive();
        } catch (IOException e) {
            System.out.println("创建服务器失败");
            e.printStackTrace();
        }
    }

    /**
     * 接受连接处理
     */
    public void receive(){
        try {
            // 阻塞式的等待连接
            Socket client = socket.accept();
            System.out.println("一个客户端建立连接");

            // 获取请求协议
            Request request = new Request(client);
            // 获取响应协议
            Response response = new Response(client);

            Servlet servlet = null;
            if (request.getUrl().equals("login")){
                servlet = new LoginServlet();
            }else if (request.getUrl().equals("reg")){
                servlet = new RegisterServlet();
            }
            servlet.service(request, response);
            // 关注了状态码
            response.pushToBrowser(200);
        } catch (IOException e) {
            System.out.println("连接失败");
            e.printStackTrace();
        }
    }

    /**
     * 停止服务
     */
    public void stop(){

    }
}

