package com.zca.server;

/**
 * @author Altria
 * Date: 10/10/2019 下午 7:18
 */
public class RegisterServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
            response.print("登入成功");
    }
}
