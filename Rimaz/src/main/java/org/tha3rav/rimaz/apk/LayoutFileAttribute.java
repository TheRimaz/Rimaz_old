package org.tha3rav.rimaz.apk;

public class LayoutFileAttribute
{
    private String name;
    private String value;

    public LayoutFileAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
