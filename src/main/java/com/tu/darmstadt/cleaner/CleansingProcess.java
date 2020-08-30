package com.tu.darmstadt.cleaner;

import com.tu.darmstadt.utils.CleanerPropertyKeys;
import com.tu.darmstadt.utils.PropertyReader;
import com.tu.darmstadt.writer.Writer;
import com.tu.darmstadt.writer.WriterFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CleansingProcess implements ICleansingProcess {

  private PropertyReader propertiesReader;

  private Writer writer;

  public CleansingProcess(PropertyReader propertiesReader, Writer writer) {
    this.propertiesReader = propertiesReader;
    this.writer = writer;
  }

  @Override
  public void clean(String parent, String fileName) {
    try {
      InputStream inputStream = Files.newInputStream(Paths.get(parent, fileName));
      String content = IOUtils.toString(inputStream, Charset.defaultCharset());
      String cleanedContent =
          removeDefaultSpecialCharacters(
              CleansingTask.remove(
                  content,
                  this.getRemovedTexts(),
                  this.getRemovedTextPatterns(),
                  this.getRemovedCharacterPatterns()));
      WriterFactory.getWriterFactory().write(writer, fileName, cleanedContent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> getRemovedTexts() {
    return propertiesReader.getValuesByKeyAndDelimiter(CleanerPropertyKeys.CLEANED_TEXTS, "||");
  }

  private List<String> getRemovedCharacters() {
    return propertiesReader.getValuesByKeyAndDelimiter(
        CleanerPropertyKeys.CLEANED_CHARACTERS, "||");
  }

  private List<String> getRemovedTextPatterns() {
    return propertiesReader.getValuesByKeyAndDelimiter(
        CleanerPropertyKeys.CLEANED_TEXT_PATTERNS, "||");
  }

  private List<String> getRemovedCharacterPatterns() {
    return propertiesReader.getValuesByKeyAndDelimiter(
        CleanerPropertyKeys.CLEANED_CHARACTER_PATTERNS, "||");
  }

  protected List<String> getDirectories() {
    return propertiesReader.getValuesByKeyAndDelimiter(
        CleanerPropertyKeys.CLEANED_DIRECTORIES, ";");
  }

  private String removeDefaultSpecialCharacters(String content) {
    String cleanedContent = content.trim();
    List<String> defaultSpecialCharacters =
        Arrays.asList("\\|", "\\?", "\\u00a9", "\\b", "\\u00A9", "\\n");
    for (String c : defaultSpecialCharacters) {
      cleanedContent = cleanedContent.replaceAll(c, "");
    }
    return cleanedContent;
  }
}
