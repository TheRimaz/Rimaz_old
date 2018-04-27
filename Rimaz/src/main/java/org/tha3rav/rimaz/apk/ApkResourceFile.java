package org.tha3rav.rimaz.apk;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.tha3rav.rimaz.utils.XML.XMLUtils.nodeListToListConverter;

public class ApkResourceFile extends ApkEntry
{
    private Document xmlDocument;

    public ApkResourceFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException
    {
        super(binaryXmlFile.getName());
        this.xmlDocument = binaryXmlFile.parse();
    }

    public List<Node> getAllElements()
    {
        List<Node> nodeList = this.getChildElementsRecursively(this.xmlDocument.getFirstChild());
        return nodeList;
    }

    private List<Node> getChildElementsRecursively(Node node)
    {
        List<Node> childNodes = new ArrayList();
        childNodes.add(node);
        if (!node.hasChildNodes())
        {
            return childNodes;
        }
        else
        {
            nodeListToListConverter(node.getChildNodes()).forEach((n) ->
            {
                childNodes.addAll(this.getChildElementsRecursively(n));
            });
            return childNodes;
        }
    }

    /**
     * @return Document object represented by the current resource file.
     */
    public Document getXmlDocument()
    {
        return xmlDocument;
    }
}
