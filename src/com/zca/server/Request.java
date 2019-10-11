package com.zca.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.*;

/**
 * 封装请求协议: 获取method uri以及请求参数
 * @author Altria
 * Date: 11/10/2019 上午 11:03
 */
public class Request {
    // 协议信息
    private String requestInfo;
    // 请求方式
    private String method;
    // 请求URL
    private String url;
    // 请求参数
    private String queryStr;

    // 存储参数
    private Map<String, List<String>> parameterMap;

    private final String BLANK = " ";
    private final String CRLF = "\r\n";

    public Request() {
    }

    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    public Request(InputStream is) {
        parameterMap = new HashMap<>();
        byte[] datas = new byte[1024*1024];
        int len;
        try {
            len = is.read(datas);
            this.requestInfo = new String(datas, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //  分解字符串
        parseRequestInfo();
    }

    // 解析字符串
    private void parseRequestInfo(){
        System.out.println("------分解开始------");
        // 获取请求方式
        // 找到第一个"/"
        System.out.println("-------请求头-------");
        this.method = this.requestInfo.substring(0, this.requestInfo.indexOf("/")).trim();
        System.out.println(this.method);
        // 解析URL
        System.out.println("-------获取URL-------");
        int startldx = this.requestInfo.indexOf("/")+1;
        int endldx = this.requestInfo.indexOf("HTTP/");
        this.url = this.requestInfo.substring(startldx,endldx).trim();
        // 这里需要将url和请求参数分离
        int queryldx = this.url.indexOf("?");
        if (queryldx >= 0){// 表示有请求参数, 这就直接将url和请求参数一分为二
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            this.queryStr = urlArray[1];
        }
        System.out.println(this.url);
        System.out.println("-------获取参数-------");
        // 上面的是GET请求, 下面是POST请求获取
        if (method.equals("POST")){
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            if (null == queryStr){
                queryStr = qStr;
            }else{
                queryStr += "&" + qStr;
            }
        }
        // 这里再次判断一些queryStr是否为null
        queryStr = null==queryStr?"":queryStr;
        System.out.println(method + "-->" + url + "-->" + queryStr);
        // 将参数转成Map
        convertMap();
    }

    // 处理请求参数为Map
    private void convertMap(){
        // 示例: uname=zhou&pwd=123456&age=&id=1001
        String[] keyValues = this.queryStr.split("&");
        for (String query:keyValues){
            String[] keyValue = query.split("=");
            // 为了避免参数只有key没有value的情况
            keyValue = Arrays.copyOf(keyValue, 2);// 这个方法保证keyValue必定会有两个元素不会出现第二个元素为null的情况
            String key = keyValue[0];
            String value = keyValue[1]==null?null:decode(keyValue[1], "UTF-8");
            // 存储到Map中
            if (!parameterMap.containsKey(key)){// 判断是否有这个key
                parameterMap.put(key, new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
        }
    }

    /**
     * 处理中文, 这里单独拿出来处理也是为了方便处理异常
     * @return
     */
    private String decode(String value, String enc){
        try {
            return java.net.URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取多个值
     * @param key
     * @return
     */
    public String[] getParameterValues(String key){
        List<String> values = this.parameterMap.get(key);
        if (null == values || values.size()<1){
            return null;
        }
        // toArray(): 如果里面什么都不添加的话,返回值是Object()
        // 为了返回String[]类型, 官方给出的方法就是这个:toArray(new String[0])
        // String[] stringArray = Arrays.copyOf(values, values.length, String[].class);
        // 否则就使用上面的方法进行转型, 总归还是比较麻烦
       return values.toArray(new String[0]);
    }

    /**
     * 获取一个值
     * @param key
     * @return
     */
    public String getParameterValue(String key){
        String[] values = getParameterValues(key);
        return values == null?null:values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }

}
