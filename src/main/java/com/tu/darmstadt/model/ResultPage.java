package com.tu.darmstadt.model;

import java.util.Set;

public class ResultPage {
  private String baseUri;
  private int currentDepth;

  private Set<String> subLinks;

  private String content;

  private String fullHtmlPath;

  public ResultPage(
      String baseUri, int currentDepth, Set<String> subLinks, String content, String fullHtmlPath) {
    this.baseUri = baseUri;
    this.currentDepth = currentDepth;
    this.subLinks = subLinks;
    this.content = content;
    this.fullHtmlPath = fullHtmlPath;
  }

  public String getBaseUri() {
    return baseUri;
  }

  public int getCurrentDepth() {
    return currentDepth;
  }

  public Set<String> getSubLinks() {
    return subLinks;
  }

  public String getContent() {
    return content;
  }

  public String getFullHtmlPath() {
    return fullHtmlPath;
  }
}
