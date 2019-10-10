package com.zca.server;

import com.zca.server.basic.servlet.Servlet;

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
            InputStream is = new BufferedInputStream(client.getInputStream());
            byte[] datas = new byte[1024*10];
            int len = is.read(datas);
            String requestInfo = new String(datas, 0, len);
            System.out.println(requestInfo);

            Response response = new Response(client);
            response.print("<html>");
            response.print("<head>");
            response.print("<title>");
            response.print("服务器响应成功");
            response.print("</title>");
            response.print("</head>");
            response.print("<body>");
            response.print("这里是正文......");
            response.print("</body>");
            response.print("</html>");
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

