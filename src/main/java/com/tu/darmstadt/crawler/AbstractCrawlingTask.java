package com.tu.darmstadt.crawler;

import com.tu.darmstadt.model.ResultPage;
import com.tu.darmstadt.utils.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractCrawlingTask implements Callable<ResultPage> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractCrawlingTask.class);
  private String baseUri;
  private String source;
  private int currentDepth;
  private Set<String> subLinks;
  private String content;

  // private PropertiesReader propertiesReader;
  private String fullHtmlPath;
  private int timeout;
  private List<String> blackList;

  // private ParsingTasksManager taskManager;
  private List<String> mustHave;

  public AbstractCrawlingTask(
      String baseUri,
      String source,
      int currentDepth,
      int timeout,
      List<String> blackList,
      List<String> mustHave) {
    this.baseUri = baseUri;
    this.source = source;
    this.currentDepth = currentDepth;
    this.timeout = timeout;
    this.blackList = blackList;
    this.mustHave = mustHave;
  }

  @Override
  public ResultPage call() {
    parse();
    return new ResultPage(
        getBaseUri(), getCurrentDepth(), getSubLinks(), getContent(), getFullHtmlPath());
  }

  /**
   * extract plain text of document
   *
   * @param document
   * @return
   */
  public void extractSubLinksAndPlainText(Document document) {
    this.subLinks =
        Stream.concat(
                document.select("a[href]").stream().map(element -> element.absUrl("href").trim()),
                document.select("frame").stream().map(element -> element.attr("src").trim()))
            .collect(Collectors.toSet());
    // adds more filters to eliminate  unneeded tags or
    document.select("script,noscript").remove();
    document = new Cleaner(Whitelist.basic()).clean(document);
    this.content = Utils.eliminateStopWords(document.text());
  }

  public abstract void parse();

  public int getCurrentDepth() {
    return currentDepth;
  }

  public Set<String> getSubLinks() {
    return subLinks;
  }

  public String getContent() {
    return content;
  }

  public String getBaseUri() {
    return baseUri;
  }

  public String getFullHtmlPath() {
    return fullHtmlPath;
  }

  public void setFullHtmlPath(String fullHtmlPath) {
    this.fullHtmlPath = fullHtmlPath;
  }

  public String getSource() {
    return source;
  }

  public int getTimeout() {
    return timeout;
  }

  public List<String> getBlackList() {
    return blackList;
  }

  public List<String> getMustHave() {
    return mustHave;
  }
}
