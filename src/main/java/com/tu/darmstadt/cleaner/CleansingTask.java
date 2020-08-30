package com.tu.darmstadt.cleaner;

import java.util.List;

public class CleansingTask {

  private static final String EMPTY = " ";

  public static String removedByMatchingPatterns(String input, List<String> patterns) {
    String out = input;
    for (String pattern : patterns) {
      out = out.replaceAll(pattern, EMPTY);
    }
    return out.trim();
  }

  protected static String remove(
      String input, List<String> texts, List<String> textPatterns, List<String> characterPatterns) {
    return removedByMatchingPatterns(
            removedByMatchingPatterns(removedByMatchingPatterns(input, textPatterns), texts),
            characterPatterns)
        .trim();
  }
}
