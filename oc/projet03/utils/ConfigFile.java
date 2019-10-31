package oc.projet03.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ConfigFile {
    private File f;
    private DocumentBuilderFactory fc;
    private DocumentBuilder builder;
    private Document doc;
    public ConfigFile() throws ParserConfigurationException, IOException, SAXException {
        f = new File("config.xml");
        fc = DocumentBuilderFactory.newInstance();
        builder = fc.newDocumentBuilder();
        doc = builder.parse(f);
    }
    public boolean devMode() {
        NodeList ls =  doc.getElementsByTagName("config");
        Element e = (Element) ls.item(0);
        String s = e.getElementsByTagName("devmode").item(0).getTextContent();
        if(s.equalsIgnoreCase("false"))return false;
        else return true;
    }
    public int maxTry() throws NumberFormatException {
        NodeList ls =  doc.getElementsByTagName("config");
        Element e = (Element) ls.item(0);
        String s = e.getElementsByTagName("maxtry").item(0).getTextContent();
        return Integer.parseInt(s);
    }
    public int keySize() throws NumberFormatException {
        NodeList ls =  doc.getElementsByTagName("config");
        Element e = (Element) ls.item(0);
        String s = e.getElementsByTagName("keysize").item(0).getTextContent();
        return Integer.parseInt(s);
    }

}
