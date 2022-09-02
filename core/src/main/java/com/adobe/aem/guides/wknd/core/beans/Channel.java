package com.adobe.aem.guides.wknd.core.beans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="channel")
public class Channel {

    String title;

    String link;

    String description;

    List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
