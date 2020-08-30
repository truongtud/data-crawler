package com.tu.darmstadt.cleaner;

import java.util.List;
import java.util.regex.Pattern;

public interface ICleansingTask {

  String removedByTextPatterns(String input, List<Pattern> textPatterns);

  String removeTexts(String input, List<String> texts);

  String removedByCharacterPatterns(String input, List<Pattern> characterPatterns);

  String removeCharacters(String input, List<Character> characters);

  default String remove(
      String input,
      List<String> texts,
      List<Pattern> textPatterns,
      List<Pattern> characterPatterns,
      List<Character> characters) {
    return removeCharacters(
        removedByCharacterPatterns(
            removeTexts(removedByTextPatterns(input, textPatterns), texts), characterPatterns),
        characters);
  }
}
