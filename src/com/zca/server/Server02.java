package com.zca.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Altria
 * Date: 10/10/2019 下午 9:39
 */
public class Server02 {

    private ServerSocket socket;
    private boolean isRunning = false;

    public static void main(String[] args) {
        Server02 server = new Server02();
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
            isRunning = true;
            receive();
        } catch (IOException e) {
            System.out.println("创建服务器失败");
            stop();
            e.printStackTrace();
        }
    }

    /**
     * 接受连接处理
     */
    public void receive(){
        while(isRunning){
            try {
                // 阻塞式的等待连接
                Socket client = socket.accept();
                System.out.println("一个客户端建立连接");
                new Thread(new Dospatcher(client)).start();

            } catch (IOException e) {
                System.out.println("连接失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止服务
     */
    public void stop(){
        isRunning = false;
        try {
            this.socket.close();
            System.out.println("服务器已停止");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

