package com.zca.server.basic.servlet;

import com.zca.server.basic.Person;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Altria
 * Date: 10/10/2019 下午 5:45
 */
public class XmlTest02 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // SAX解析
        // 1. 获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2. 从解析工厂获取解析器
        SAXParser parser = factory.newSAXParser();
        // 3. 加载文档Docu注册处理器
        WebHandler webHandler = new WebHandler();
        // 4.解析
        parser.parse(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("com/zca/server/basic/servlet/web.xml"), webHandler);

        // 获取数据
        WebContext webContext = new WebContext(webHandler.getEntities(), webHandler.getMappings());
        // 这里通过动态输入变量跳转到不同的页面
        String className = webContext.getClz("/login");
        Class clz = Class.forName(className);
        Servlet servlet = (Servlet) clz.getDeclaredConstructor().newInstance();
        servlet.Servlet();

    }
}

class WebHandler extends DefaultHandler{
    private List<Entity> entities;
    private List<Mapping> mappings;
    private Entity entity;
    private Mapping mapping;
    private String tag;
    // 添加标识
    private boolean isMapping = false;

    /**
     * 这个方法不是必须的
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("------解析开始------");
        entities = new ArrayList<>();
        mappings = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (null != qName){
            tag = qName;
            if (tag.equals("servlet")){
                entity = new Entity();
                isMapping = false;
            }else if (tag.equals("servlet-mapping")){
                mapping = new Mapping();
                // 这里是在操作mapping了, 把标识设为true
                isMapping = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String count = new String(ch, start, length);
        if (null != tag){
            if (isMapping){ // 操作mapping
                if (tag.equals("servlet-name")){
                    mapping.setName(count);
                }else if (tag.equals("url-pattern")){
                    if (count.length()>0){
                        mapping.addPattern(count);
                    }
                }
            }else{ // 操作entity
                if (tag.equals("servlet-name")){
                    entity.setName(count);
                }else if (tag.equals("servlet-class")){
                    entity.setClz(count);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("servlet")){
            entities.add(entity);
        }else if (qName.equals("servlet-mapping")){
            mappings.add(mapping);
        }
        tag = null;
    }

    /**
     * 这个方法不是必须的
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("------解析结束------");
    }


    public List<Entity> getEntities() {
        return entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }
}
