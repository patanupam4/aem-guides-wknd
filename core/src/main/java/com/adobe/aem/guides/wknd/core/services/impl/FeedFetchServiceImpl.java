package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.beans.Rss;
import com.adobe.aem.guides.wknd.core.models.RssFeedBean;
import com.adobe.aem.guides.wknd.core.services.FeedFetchService;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = FeedFetchService.class, immediate = true)
public class FeedFetchServiceImpl implements FeedFetchService {

    private static final Logger log = LoggerFactory.getLogger(FeedFetchServiceImpl.class);

    @Override public List<RssFeedBean> getFeedData(String feedPath, int limit) {
        return getFeed(feedPath, limit);
    }

    /**
     * Gets the feed.
     *
     * @param feedPath the feed path
     * @param limit    the limit
     * @return the feed
     */
    private List<RssFeedBean> getFeed(String feedPath, int limit) {
        List<RssFeedBean> rssFeedBeans = new ArrayList<>();
        if (!feedPath.isEmpty() && limit > 0) {
            log.debug("Feed path is {}", feedPath);
            log.debug("limit {}", limit);
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                URL feedUrl = new URL(feedPath);
                if (Boolean.TRUE.equals(isValidFeedUrl(feedUrl))) {
                    Rss rss = (Rss) unmarshaller.unmarshal(feedUrl);
                    rss.getChannel().getItem().stream().limit(limit).collect(Collectors.toList()).forEach(item -> {
                        RssFeedBean rssFeedBean = new RssFeedBean();
                        rssFeedBean.setTitle(item.getTitle());
                        rssFeedBean.setDescription(item.getDescription());
                        rssFeedBean.setLink(item.getLink());
                        try {
                            rssFeedBean.setDisplayPublishedDate(dateFormat(item.getPublishedDate()));
                        } catch (ParseException e) {
                            log.error("Parsing exception while dateFormat - String to Date at : ", e);
                        }
                        rssFeedBeans.add(rssFeedBean);
                    });
                    return rssFeedBeans;
                }
            }  catch (JAXBException | MalformedURLException e) {
                log.error("JAXBException at : ", e);
            }

        }
        return rssFeedBeans;
    }

    /**
     * Checks if the feed URL is invalid
     *
     * @param feedUrl
     * @return
     */
    private Boolean isValidFeedUrl(URL feedUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) feedUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            log.error("IOException while validating URL : ", e);
            return false;
        }
    }

    /**
     * Formats the XML date to yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String dateFormat(String date) throws ParseException {
        Date dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return new SimpleDateFormat("yyyy-MM-dd").format(dateFormat);
    }
}
