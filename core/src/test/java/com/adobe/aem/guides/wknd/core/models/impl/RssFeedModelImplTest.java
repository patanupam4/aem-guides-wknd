package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.RssFeedBean;
import com.adobe.aem.guides.wknd.core.services.FeedFetchService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class RssFeedModelImplTest {

    private final AemContext aemContext = new AemContext();
    private RssFeedModelImpl rssFeedModel;

    @Mock FeedFetchService feedFetchService;

    @BeforeEach void setUp() {
        aemContext.addModelsForClasses(RssFeedModelImpl.class);
        aemContext.load().json("/com/mysite/core/models/impl/rssfeed.json", "/content");
        aemContext.currentResource("/content");
        feedFetchService = aemContext.registerService(FeedFetchService.class, feedFetchService);
        rssFeedModel = aemContext.request().adaptTo(RssFeedModelImpl.class);
    }

    @Test void getExportedType() {
        assertEquals(RssFeedModelImpl.RESOURCE_TYPE, rssFeedModel.getExportedType());
    }

    @Test void getFeedData() {
        lenient().when(feedFetchService.getFeedData(anyString(), anyInt())).thenReturn(getRssFeedBean());
        assertEquals(1, rssFeedModel.getFeedData().size());
    }

    @Test void getFeedDataWithOptions() {
        assertEquals(3, rssFeedModel.getFeedData().size());
    }

    @Test void getFeedDataGetTitle() {
        assertEquals("Title 1", rssFeedModel.getFeedData().get(0).getTitle());
    }

    @Test void getFeedDataGetDescription() {
        assertEquals("Description1", rssFeedModel.getFeedData().get(0).getDescription());
    }

    @Test void getFeedDataGetLink() {
        assertEquals("https://www.google.com", rssFeedModel.getFeedData().get(0).getLink());
    }

    @Test void getFeedDataGetDisplayPublishedDate() {
        assertEquals("2022-08-24", rssFeedModel.getFeedData().get(0).getDisplayPublishedDate());
    }

    private List<RssFeedBean>  getRssFeedBean() {
        List<RssFeedBean> rssFeedBeans = new ArrayList<>();
        RssFeedBean rssFeedBean = new RssFeedBean();
        rssFeedBean.setTitle("title");
        rssFeedBean.setDescription("description");
        rssFeedBean.setDate(new Date());
        rssFeedBean.setLink("https://www.google.com");
        rssFeedBeans.add(rssFeedBean);
        return rssFeedBeans;
    }
}
