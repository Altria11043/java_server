package com.zca.server;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Altria
 * Date: 11/10/2019 下午 2:54
 */
public class WebApp {
    private static WebContext webContext;
    static{
        try {
            // SAX解析
            // 1. 获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 2. 从解析工厂获取解析器
            SAXParser parser = factory.newSAXParser();
            // 3. 加载文档Docu注册处理器
            WebHandler handler = new WebHandler();
            // 4.解析
            parser.parse(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("web.xml"), handler);

            webContext = new WebContext(handler.getEntities(), handler.getMappings());
        }catch (Exception e){
            System.out.println("文件解析错误");
        }
    }

    /**
     * 通过配置文件获取配置文件对应的servlet
     * @param url
     * @return
     */
    public static Servlet getServletFromUrl(String url){
        String className = webContext.getClz("/" + url);
        if (null!=className){
            Class clz = null;
            try {
                clz = Class.forName(className);
                Servlet servlet = (Servlet) clz.getDeclaredConstructor().newInstance();
                return servlet;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
