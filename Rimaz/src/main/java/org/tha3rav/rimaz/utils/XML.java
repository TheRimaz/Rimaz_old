package org.tha3rav.rimaz.utils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class XML
{
    public static final class XMLUtils {

        public static String documentToString(Document doc) {
            try {
                StringWriter       sw          = new StringWriter();
                TransformerFactory tf          = TransformerFactory.newInstance();
                Transformer        transformer = tf.newTransformer();
                transformer.setOutputProperty("omit-xml-declaration", "no");
                transformer.setOutputProperty("method", "xml");
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("encoding", "UTF-8");
                transformer.transform(new DOMSource(doc), new StreamResult(sw));
                return sw.toString();
            } catch (Exception var4) {
                throw new RuntimeException("Error converting to String", var4);
            }
        }

        public static List<Node> nodeListToListConverter(NodeList nodeList) {
            List<Node> nodes = new ArrayList();

            for(int i = 0; i < nodeList.getLength(); ++i) {
                nodes.add(nodeList.item(i));
            }

            return nodes;
        }

        public static List<Node> namedNodeMapToListConverter(NamedNodeMap attributes) {
            List<Node> nodes = new ArrayList();

            for(int i = 0; i < attributes.getLength(); ++i) {
                nodes.add(attributes.item(i));
            }

            return nodes;
        }
    }
}
