package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.apk.ApkBinaryXmlFile;
import org.tha3rav.rimaz.apk.ApkResourceFile;
import org.tha3rav.rimaz.exceptions.ErroneousManifestFileException;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import static org.tha3rav.rimaz.utils.APK.APKConstants.MANIFEST_PACKAGE_ATTRIBUTE;
import static org.tha3rav.rimaz.utils.APK.APKConstants.MANIFEST_TOP_ELEMENT_NAME;
import static org.tha3rav.rimaz.utils.EXCEPTIONS.ExceptionMessages
        .NO_PACKAGE_ATTRIBUTE_IN_MANIFEST_ERROR_MESSAGE;

public class ManifestFile extends ApkResourceFile
{
    public ManifestFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException
    {
        super(binaryXmlFile);
    }

    public String getPackageName() throws ErroneousManifestFileException
    {
        List<Node>              elements    = getAllElements();
        Optional<String> packageName = elements.stream()
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .filter(node -> node.getNodeName().equals(MANIFEST_TOP_ELEMENT_NAME))
                .map(node -> node.getAttributes().getNamedItem(MANIFEST_PACKAGE_ATTRIBUTE).getNodeValue())
                .findFirst();

        if (packageName.isPresent())
        {
            return packageName.get();
        }
        else
        {
            throw new ErroneousManifestFileException(NO_PACKAGE_ATTRIBUTE_IN_MANIFEST_ERROR_MESSAGE);
        }
    }
}
