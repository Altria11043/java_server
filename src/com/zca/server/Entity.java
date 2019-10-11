package com.zca.server;

/**
 * <servlet>
 *     <servlet-name>login</servlet-name>
 *     <servlet-class>com.zca.LoginServlet</servlet-class>
 * </servlet>
 * @author Altria
 * Date: 10/10/2019 下午 5:39
 */
public class Entity {
    private String name;
    private String clz;

    public Entity() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }
}
