package com.zca.server.basic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Altria
 * Date: 10/10/2019 下午 3:10
 */
public class XmlTest01 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        //SAX解析
        // 1. 获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2. 从解析工厂获取解析器
        SAXParser parser = factory.newSAXParser();
        // 3. 编写处理器
        // 4. 加载文档Docu注册处理器
        PHandler handler = new PHandler();
        // 5. 解析
        parser.parse(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("com/zca/server/basic/p.xml")
                , handler);
        // 获取数据
        List<Person> personList = handler.getPersons();
        for (Person person:personList){
            System.out.println(person.getName() + "-->" + person.getAge());
        }

    }
}

class PHandler extends DefaultHandler{
    private List<Person> persons;
    private Person person;
    // 存储当前的元素名或者标签名
    private String tag;

    @Override
    public void startDocument() throws SAXException {
        // 解析开始时
        persons = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName!=null){
            tag = qName;
            if (tag.equals("person")){
                person = new Person();
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contents = new String(ch, start, length);
        // 判断是否为空
        if (null!= tag){
            if (tag.equals("name")){
                person.setName(contents);
            }else if (tag.equals("age")){
                if (contents.length()>0){
                    person.setAge(Integer.valueOf(contents));
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName!=null){
            if (qName.equals("person")){
                persons.add(person);
            }
        }
        tag = null;
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("--------解析结束--------");
    }

    public List<Person> getPersons() {
        return persons;
    }
}
