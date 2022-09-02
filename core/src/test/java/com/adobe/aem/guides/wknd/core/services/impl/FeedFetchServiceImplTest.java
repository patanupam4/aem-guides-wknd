package com.adobe.aem.guides.wknd.core.services.impl;


import com.adobe.aem.guides.wknd.core.beans.Channel;
import com.adobe.aem.guides.wknd.core.beans.Item;
import com.adobe.aem.guides.wknd.core.beans.Rss;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class }) class FeedFetchServiceImplTest {

    protected final AemContext aemContext = new AemContext();

    public MockSlingHttpServletRequest request;

    protected ResourceResolver resourceResolver;

    protected BundleContext bundleContext;

    FeedFetchServiceImpl feedFetchService = new FeedFetchServiceImpl();

    Rss rss;
    JAXBContext jaxbContext;
    Unmarshaller unmarshaller;
    Channel channel;
    Item item;

    @BeforeEach void setUp() {
        request = aemContext.request();
        resourceResolver = aemContext.resourceResolver();
        bundleContext = aemContext.bundleContext();
        rss = mock(Rss.class);
        jaxbContext = mock(JAXBContext.class);
        unmarshaller = mock(Unmarshaller.class);
        channel = mock(Channel.class);
        item = mock(Item.class);
    }

    @Test void getFeedData() throws NoSuchFieldException {
        boolean isEnabled = true;
        PrivateAccessor.setField(this.feedFetchService, "isEnabled", isEnabled);
        try (MockedStatic<JAXBContext> utilities = Mockito.mockStatic(JAXBContext.class)) {
            utilities.when(() -> JAXBContext.newInstance(Rss.class)).thenReturn(jaxbContext);
            when(jaxbContext.createUnmarshaller()).thenReturn(unmarshaller);
            when((Rss) unmarshaller.unmarshal(new URL("https://sports.ndtv.com/rss/football"))).thenReturn(rss);
            when(rss.getChannel()).thenReturn(channel);
            List<Item> items = setList();
            when(channel.getItem()).thenReturn(items);
            feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10);
            assertEquals(1, feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10).size());
        } catch (JAXBException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test void getFeedDataWithParseException() throws NoSuchFieldException {
        boolean isEnabled = true;
        PrivateAccessor.setField(this.feedFetchService, "isEnabled", isEnabled);
        try (MockedStatic<JAXBContext> utilities = Mockito.mockStatic(JAXBContext.class)) {
            utilities.when(() -> JAXBContext.newInstance(Rss.class)).thenReturn(jaxbContext);
            when(jaxbContext.createUnmarshaller()).thenReturn(unmarshaller);
            when((Rss) unmarshaller.unmarshal(new URL("https://sports.ndtv.com/rss/football"))).thenReturn(rss);
            when(rss.getChannel()).thenReturn(channel);
            List<Item> items = setListforParseException();
            when(channel.getItem()).thenReturn(items);
            feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10);
            assertEquals(1, feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10).size());
        } catch (JAXBException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test void getFeedDataWIthJaxbException() throws NoSuchFieldException {
        boolean isEnabled = true;
        PrivateAccessor.setField(this.feedFetchService, "isEnabled", isEnabled);
        try (MockedStatic<JAXBContext> utilities = Mockito.mockStatic(JAXBContext.class)) {
            utilities.when(() -> JAXBContext.newInstance(Rss.class)).thenReturn(jaxbContext);
            when(jaxbContext.createUnmarshaller()).thenReturn(unmarshaller);
            when((Rss) unmarshaller.unmarshal(new URL("https://sports.ndtv.com/rss/football"))).thenThrow(
                    new JAXBException("Jaxb Exception thrown"));
            feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10);
            assertEquals(0, feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10).size());
        } catch (JAXBException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test void getFeedDataWhenConfigDisabled() throws NoSuchFieldException {
        boolean isEnabled = false;
        PrivateAccessor.setField(this.feedFetchService, "isEnabled", isEnabled);
        feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10);
        assertEquals(0, feedFetchService.getFeedData("https://sports.ndtv.com/rss/football", 10).size());
    }

    private List<Item> setList() {
        List<Item> mockList = new ArrayList<>();
        Item item1 = new Item();
        item1.setTitle("Title");
        item1.setDescription("description");
        item1.setLink("https://www.google.com");
        item1.setPublishedDate("2022-05-05");
        mockList.add(item1);
        return mockList;
    }

    private List<Item> setListforParseException() {
        List<Item> mockList = new ArrayList<>();
        Item item1 = new Item();
        item1.setTitle("Title");
        item1.setDescription("description");
        item1.setLink("https://www.google.com");
        item1.setPublishedDate("2022-05/0");
        mockList.add(item1);
        return mockList;
    }
}
