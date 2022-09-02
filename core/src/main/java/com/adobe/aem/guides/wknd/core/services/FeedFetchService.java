package com.adobe.aem.guides.wknd.core.services;

import com.adobe.aem.guides.wknd.core.models.RssFeedBean;

import java.util.List;

public interface FeedFetchService {

    /**
     * Gets the feed data.
     *
     * @param feedPath the feed path
     * @param limit the limit
     * @return the feed data
     */
    List<RssFeedBean> getFeedData(String feedPath, int limit);
}
