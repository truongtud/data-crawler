package com.tu.darmstadt.utils;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;

public class Utils {

  public static String eliminateStopWords(String plainText) {
    // TODO
    return plainText;
  }

  public static boolean isValidUrl(List<String> blackList, List<String> mustHave, String url) {
    return UrlValidator.getInstance().isValid(url)
        && isNotInBlackList(url, blackList)
        && mustHave(url, mustHave);
  }

  private static boolean isNotInBlackList(String url, List<String> blackList) {
    return blackList.stream().allMatch(e -> !url.contains(e));
  }

  private static boolean mustHave(String url, List<String> mustHave) {
    return mustHave.stream().anyMatch(e -> url.contains(e));
  }
}
