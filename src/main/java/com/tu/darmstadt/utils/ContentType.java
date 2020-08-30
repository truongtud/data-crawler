package com.tu.darmstadt.utils;

public enum ContentType {
  TEXT_HTML("text/html"),
  TEXT_HTML_UTF_8("text/html;charset=utf-8"),
  PDF("application/pdf");

  private final String type;

  ContentType(String type) {
    this.type = type;
  }

  public static ContentType getHelpContentType(String type) {
    return ContentType.valueOf(type);
  }

  public String getType() {
    return type;
  }
}
