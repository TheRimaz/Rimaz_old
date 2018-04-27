package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.IDataBoundUIElement;
import org.tha3rav.rimaz.exceptions.InvalidLayoutAttributeException;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.tha3rav.rimaz.utils.LAYOUT.LayoutFileConstants.UI_ELEMENT_DATABINDING_ATTRIBUTE;
import static org.tha3rav.rimaz.utils.XML.XMLUtils.namedNodeMapToListConverter;

public class LayoutFileUIElement implements IDataBoundUIElement
{
    private boolean                       hasChildren;
    private String                        name;
    private Optional<LayoutFileAttribute> id;
    private List<LayoutFileAttribute>     attributes;

    public LayoutFileUIElement(Node node) throws InvalidLayoutAttributeException
    {
        if (node.getNodeType() == 1) {
            this.name = node.getNodeName();
            this.hasChildren = node.hasChildNodes();
            this.attributes = this.getAttributes(node);
            this.id = this.findId();
        } else {
            throw new InvalidLayoutAttributeException("Invalid Attribute !");
        }
    }

    private List<LayoutFileAttribute> getAttributes(Node node)
    {
        return namedNodeMapToListConverter(node.getAttributes())
                .stream()
                .map((n) -> new LayoutFileAttribute(n.getNodeName(), n.getNodeValue()))
                .collect(Collectors.toList());
    }

    private Optional<LayoutFileAttribute> findId() {
        Optional<LayoutFileAttribute> id = null;
        if (this.attributes != null && this.attributes.size() > 0) {
            id = this.attributes.stream().filter((attribute) -> {
                return attribute.getName().toLowerCase().equals("id");
            }).findFirst();
        }

        return id;
    }

    public boolean isHasChildren() {
        return this.hasChildren;
    }

    public String getName() {
        return this.name;
    }

    public List<LayoutFileAttribute> getAttributes() {
        return this.attributes;
    }

    public Optional<LayoutFileAttribute> getId() {
        return this.id;
    }

    @Override
    public boolean isDataBoundUIElement()
    {
        return getDataBindingAttribute().isPresent();
    }

    @Override
    public Optional<LayoutFileAttribute> getDataBindingAttribute()
    {
        return attributes.stream()
                         .filter(layoutFileAttribute -> layoutFileAttribute.getName().equals(UI_ELEMENT_DATABINDING_ATTRIBUTE))
                         .findAny();
    }
}
