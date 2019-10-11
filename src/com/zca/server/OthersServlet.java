package com.zca.server;

/**
 * @author Altria
 * Date: 11/10/2019 下午 3:46
 */
public class OthersServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.print("其他页面");
    }
}
