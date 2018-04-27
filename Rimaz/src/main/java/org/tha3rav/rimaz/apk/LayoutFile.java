package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.IDataBoundUIElement;
import org.tha3rav.rimaz.exceptions.InvalidLayoutAttributeException;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class LayoutFile extends ApkResourceFile
{
    public LayoutFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException
    {
        super(binaryXmlFile);
    }

    public List<LayoutFileUIElement> getUIElements()
    {
        List<Node> elements = this.getAllElements();
        return elements.stream()
                       .map((node) -> {
                                        LayoutFileUIElement layoutFileUIElement = null;
                                        try
                                        {
                                            layoutFileUIElement = new LayoutFileUIElement(node);
                                        }
                                        catch (InvalidLayoutAttributeException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        return layoutFileUIElement;
                                    })
                       .collect(Collectors.toList());
    }

    public List<LayoutFileUIElement> getUIElementsHavingIDs()
    {
        return this.getUIElements().stream()
                                   .filter(lfuie -> lfuie.getId().isPresent())
                                   .collect(Collectors.toList());
    }

    public List<IDataBoundUIElement> getDataBoundUIElements()
    {
        List<LayoutFileUIElement> allUIElements = getUIElements();
        return allUIElements.stream()
                            .filter(lfuie -> lfuie.isDataBoundUIElement())
                            .collect(Collectors.toList());
    }
}