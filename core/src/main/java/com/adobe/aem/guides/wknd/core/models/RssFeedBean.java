package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = { Resource.class} ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RssFeedBean {

    @ValueMapValue
    String title;

    @ValueMapValue
    String description;

    @ValueMapValue
    String link;

    @ValueMapValue Date date;

    private String displayPublishedDate;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisplayPublishedDate() {
        return displayPublishedDate;
    }

    public void setDisplayPublishedDate(String displayPublishedDate) {
        this.displayPublishedDate = displayPublishedDate;
    }

    /**
     * In case Date is null, it falls back to current date
     */
    @PostConstruct
    protected void initModel() {
        if (null != date) {
            displayPublishedDate = publishDateFormat(date);
        } else {
            displayPublishedDate = publishDateFormat(new Date());
        }
    }

    /**
     * Returns the Input date in format "yyyy-MM-dd"
     * @param inputDate
     * @return displayPublishedDate
     */
    private String publishDateFormat(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }
}
