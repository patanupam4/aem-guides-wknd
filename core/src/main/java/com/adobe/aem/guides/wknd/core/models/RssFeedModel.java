package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.export.json.ComponentExporter;

import java.util.List;

public interface RssFeedModel extends ComponentExporter {
    List<RssFeedBean> getFeedData();
}
