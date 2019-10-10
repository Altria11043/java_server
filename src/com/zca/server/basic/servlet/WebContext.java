package com.zca.server.basic.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Altria
 * Date: 10/10/2019 下午 6:58
 */
public class WebContext {
    private List<Entity> entities = null;
    private List<Mapping> mappings = null;

    // key: servlet-name  value: servlet-class
    private Map<String, String> entityMap = new HashMap<>();

    // 因为键不能重名, 所以在有多个url-pattern的情况下servlet-name不能做key
    // key: url-pattern   value: servlet-name
    private Map<String, String> mappingMap = new HashMap<>();
    public WebContext(List<Entity> entities, List<Mapping> mappings) {
        this.entities = entities;
        this.mappings = mappings;
        // 这里将一对多转成一对一

        for (Entity entity:entities){
            entityMap.put(entity.getName(), entity.getClz());
        }
        for (Mapping mapping:mappings){
            for (String pattern:mapping.getPatterns()){
                mappingMap.put(pattern, mapping.getName());
            }
        }
    }

    public String getClz(String pattern){
        // 通过url-pattern查找servlet-name
        String name = mappingMap.get(pattern);
        // 通过servlet-name找到servlet-class
        return entityMap.get(name);
    }
}
