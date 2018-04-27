package org.tha3rav.rimaz.apk;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.tha3rav.rimaz.axmlParser.CompressedXmlParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

public class ApkBinaryXmlFile
{
    private InputStream inputStream;
    private String      name;

    public ApkBinaryXmlFile(InputStream inputStream, String name)
    {
        this.name = name;
        this.inputStream = inputStream;
    }

    public Document parse() throws IOException, ParserConfigurationException
    {
        Document xmlDocument = null;
        try
        {
            xmlDocument = (new CompressedXmlParser()).parseDOM(inputStream);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            //Ignore exception
        }
        catch (NegativeArraySizeException e)
        {
            //Ignore exception
        }
        catch (DOMException e)
        {
            //Ignore exception
        }





        return xmlDocument;
    }

    public String getName()
    {
        return name;
    }
}
