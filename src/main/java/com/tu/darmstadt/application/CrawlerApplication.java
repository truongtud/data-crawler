package com.tu.darmstadt.application;

import com.tu.darmstadt.crawler.CrawlingTasksManager;
import com.tu.darmstadt.utils.PropertyReader;

public class CrawlerApplication {

  private final String ENV = "CRSP_MEDICAL_DATA_SCRAPER";
  private final String propertiesFile = "crawl.properties";
  private CrawlingTasksManager parserTasksManager;
  private PropertyReader propertiesReader;

  public CrawlerApplication() {
    this.propertiesReader = new PropertyReader(ENV, propertiesFile);
    this.parserTasksManager = new CrawlingTasksManager(this.propertiesReader);
  }

  public void crawl(String baseUri, String source) {
    this.parserTasksManager.start(baseUri, source);
    this.parserTasksManager.close();
  }
}
