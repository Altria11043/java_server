package com.zca.server;

/**
 * @author Altria
 * Date: 10/10/2019 下午 7:17
 */
public class LoginServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.print("<html>");
        response.print("<head>");
        response.print("<title>");
        response.print("服务器响应成功");
        response.print("</title>");
        response.print("</head>");
        response.print("<body>");
        response.print("这里是正文......" + request.getParameterValue("uname"));
        response.print("</body>");
        response.print("</html>");
    }
}
