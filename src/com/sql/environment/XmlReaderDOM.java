package com.sql.environment;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @Description: 
 * @author wutp 2016年9月29日
 * @version 1.0
 */
public class XmlReaderDOM {

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

	
	/**
	 * @Description:
	 * @auther: wutp 2016年9月29日
	 * @param databaseId
	 * @return void
	 */
	public static void initXmlBean(int databaseId) {
		String databaseid = String.valueOf(databaseId);
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse("src/resources/database.xml");

			NodeList databaselist = document.getElementsByTagName("database");

			System.out.println("共有" + databaselist.getLength() + "数据库信息");

			for (int i = 0; i < databaselist.getLength(); i++) {
				Node db = databaselist.item(i);
				NamedNodeMap dbids = db.getAttributes();
				System.out.println("NamedNodeMap共有" + dbids.getLength() + "节点");
						
				for (int j = 0; j < dbids.getLength(); j++) {
					Node bean = dbids.item(j);
					if (databaseid.equals(bean.getNodeValue())) {
						NodeList database = db.getChildNodes();
						for (int k = 0; k < database.getLength(); k++) {
							if (database.item(k).getNodeType() == Node.ELEMENT_NODE) {
								if ("driver".equals(database.item(k).getNodeName())) {
									driver = database.item(k).getFirstChild().getNodeValue();
									// System.out.println("driver: "+driver);
								}
								if ("url".equals(database.item(k).getNodeName())) {
									url = database.item(k).getFirstChild().getNodeValue();
									// System.out.println("url: "+url);
								}
								if ("name".equals(database.item(k).getNodeName())) {
									name = database.item(k).getFirstChild().getNodeValue();
									// System.out.println("name: "+name);
								}
								if ("pwd".equals(database.item(k).getNodeName())) {
									pwd = database.item(k).getFirstChild().getNodeValue();
									// System.out.println("pwd： "+pwd);
								}
							}
						}						
						/*Element base =(Element)dbids.item(j);
						String attrValue=base.getAttribute("name");
						System.out.println("databasename: "+attrValue);
						*/
					}
				}

			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
