package com.adobe.aem.guides.wknd.core.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rss")
public class Rss {

    Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
