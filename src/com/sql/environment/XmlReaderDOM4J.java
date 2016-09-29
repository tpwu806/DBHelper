package com.sql.environment;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlReaderDOM4J {

	private static String driver;
	private static String url;
	private static String name;
	private static String pwd;

	public static void main(String[] args) {
		initXmlBean(1);

		System.out.println("driver: " + driver);
		System.out.println("url: " + url);
		System.out.println("name: " + name);
		System.out.println("pwd: " + pwd);
	}

	public static void initXmlBean(int databaseId){
		 String databaseid = String.valueOf(databaseId);
		 File file = new File("src/resources/datasource.xml");    
         SAXReader reader = new SAXReader();    
         Document doc;
        
		try {
			doc = reader.read(file);
			 parseDOM4J(doc);// 解析XML文档 
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	private static void parseDOM4J(Document doc) {
		Element root = doc.getRootElement();
		System.out.println(root.asXML());
		for (Iterator<Element> iter = root.elementIterator(); iter.hasNext();) {
			Element DBConnection = (Element) iter.next();
			System.out.println("&&&&&&&&&&&&&&&&&&&&&"+DBConnection.asXML());
			/*for (Iterator<Element> iter2 = DBConnection.elementIterator(); iter2.hasNext();) {
				Element DataSources = (Element) iter2.next();
				for (Iterator<Element> iter3 = DataSources.elementIterator(); iter3.hasNext();) {
					Element DataSource = (Element) iter3.next();
					//System.out.println(DataSource.asXML());
					
					Attribute lidAttr = DataSource.attribute("no");// 获取<line>元素的属性    
		            String lid = lidAttr.getValue();// 获取<line>元素的属性值   
		            if("88ab".equals(lid)){
		            	Attribute numAttr = DataSource.attribute("name");// 获取<line>元素的属性    
			            String num = numAttr.getValue();// 获取<line>元素的属性值    
			        
			            //String id = element.elementText("id");// 获取<line>元素下<id></id>的值    
			        
			            System.out.println("==lid:" + lid);    
			            System.out.println("==num:" + num);
		            }
		             
				}
				
			}
			*/
		}
		
		
		// 遍历line结点的所有子节点,也可以使用root.elementIterator("DataSource")
		//但是必须一级一级
  /*      for (Iterator<Element> iter = root.elementIterator(); iter.hasNext();) {
        	    
            Element element = (Element) iter.next();
            System.out.println(element.asXML());
        
            Attribute lidAttr = element.attribute("no");// 获取<line>元素的属性    
            String lid = lidAttr.getValue();// 获取<line>元素的属性值    
            Attribute numAttr = element.attribute("name");// 获取<line>元素的属性    
            String num = numAttr.getValue();// 获取<line>元素的属性值    
        
            //String id = element.elementText("id");// 获取<line>元素下<id></id>的值    
        
            System.out.println("==lid:" + lid);    
            System.out.println("==num:" + num);    
            //System.out.println("==路线id:" + id);    
        
            for (Iterator iterInner = element.elementIterator("station"); iterInner.hasNext();) { // 遍历station结点的所有子节点    
                Element elementInner = (Element) iterInner.next();    
        
                String sid = elementInner.elementText("sid");// 获取<station>元素下<sid></sid>的值    
                String sname = elementInner.elementText("sname");// 获取<station>元素下<sname></sname>的值    
        
                System.out.println("----站--sid:" + sid);    
                System.out.println("----站--sname:" + sname);    
            }    
        
        }   */ 
		
	}
	
	// 将XML文档转换为String    
    private static String getStringFromXML(Document doc) {    
        return doc.asXML();    
    }    
        
    // 将String转换为XML文档    
    private static Document getXMLDocFromString(String str) {    
        try {    
            return DocumentHelper.parseText(str);    
        } catch (DocumentException e) {    
            e.printStackTrace();    
            return null;    
        } 
    }
}
