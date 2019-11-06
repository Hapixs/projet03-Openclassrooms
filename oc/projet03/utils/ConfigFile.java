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
    private String getValue(String field) {
        NodeList ls = doc.getElementsByTagName("config");
        Element e = (Element) ls.item(0);
        return e.getElementsByTagName(field).item(0).getTextContent();
    }
    public boolean devMode() {
        if(getValue("devmode").equalsIgnoreCase("false"))return false;
        else return true;
    }
    public int maxTry() throws NumberFormatException {
        return Integer.parseInt(getValue("maxtry"));
    }
    public int keySize() throws NumberFormatException {
        return Integer.parseInt(getValue("keysize"));
    }
    public String getText(String name){
        return getValue(name).replace("%n", "\n")
                .replace("%t", "\t");
    }
}
