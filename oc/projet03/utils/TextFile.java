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

public class TextFile {
    private File f;
    private DocumentBuilderFactory fc;
    private DocumentBuilder builder;
    private Document doc;
    public TextFile() throws ParserConfigurationException, IOException, SAXException {
        f = new File("texts.xml");
        fc = DocumentBuilderFactory.newInstance();
        builder = fc.newDocumentBuilder();
        doc = builder.parse(f);
    }
    public String getText(String name){
        NodeList ls = doc.getElementsByTagName("texts");
        Element e = (Element) ls.item(0);
        return e.getElementsByTagName(name).item(0).getTextContent().replace("%n", "\n")
                .replace("%t", "\t");
    }
}
