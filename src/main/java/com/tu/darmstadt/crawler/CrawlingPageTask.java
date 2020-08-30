package com.tu.darmstadt.crawler;

import com.tu.darmstadt.utils.ContentType;
import com.tu.darmstadt.utils.SSLSecurity;
import com.tu.darmstadt.utils.Utils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CrawlingPageTask extends AbstractCrawlingTask {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CrawlingPageTask.class);

  private URL url;

  public CrawlingPageTask(
      String baseUri,
      String source,
      int currentDepth,
      int timeout,
      List<String> blackList,
      List<String> mustHave) {
    super(baseUri, source, currentDepth, timeout, blackList, mustHave);
    try {
      url = new URL(new URL(baseUri), source);

      this.setFullHtmlPath(url.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void parse() {
    Long t = System.currentTimeMillis();
    int retries = 0;
    if (!this.getFullHtmlPath().equals("")
        && Utils.isValidUrl(getBlackList(), getMustHave(), this.getFullHtmlPath())) {
      while (retries < 3) {
        try {
          SSLSecurity.enableSSLSocket();
          Connection.Response response =
              Jsoup.connect(this.getFullHtmlPath()).timeout(getTimeout()).execute();
          if (response.statusCode() == 200) {
            String contentType = response.contentType().replaceAll(" ", "").toLowerCase();
            if (contentType.equals(ContentType.TEXT_HTML.getType())
                || contentType.equals(ContentType.TEXT_HTML_UTF_8.getType())) {
              Document document = response.parse();
              extractSubLinksAndPlainText(document);
            }
            break;
          }
        } catch (Exception e) {
          // logger.error("exception: {} --- {}",e.getMessage(), getFullHtmlPath());
        }
        retries++;
      }
    }
  }
}
