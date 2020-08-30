package com.tu.darmstadt.utils;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PropertyReader {

  private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);
  private Properties properties;
  private String environment;

  private String propertiesFile;

  public PropertyReader(String environment, String propertiesFile) {
    readPropertiesFile(environment, propertiesFile);
  }

  public void readPropertiesFile(String envKey, @Nullable String propertiesFile) {
    Optional<String> env = Optional.ofNullable(System.getenv(envKey));
    try {
      InputStream inputStream =
          env.isPresent()
              ? Files.newInputStream(Paths.get(env.get().trim()))
              : getClass().getClassLoader().getResourceAsStream(propertiesFile);
      properties = new Properties();
      properties.load(inputStream);
      logger.info(properties.toString());
    } catch (Exception e) {
      logger.error("cannot read properties file because of {}", e.getMessage());
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getPropertiesFile() {
    return propertiesFile;
  }

  public void setPropertiesFile(String propertiesFile) {
    this.propertiesFile = propertiesFile;
  }

  public int getIntegerProperty(String key) {
    return Integer.valueOf(getProperty(key)).intValue();
  }

  public List<String> splitPropertyValues(String inPropertyValue, String delimiter) {
    List<String> outPropertyValues = new ArrayList<>();
    StringTokenizer tokenizer = new StringTokenizer(inPropertyValue, delimiter);
    while (tokenizer.hasMoreTokens()) {
      outPropertyValues.add(tokenizer.nextToken());
    }
    return outPropertyValues;
  }

  public List<String> getValuesByKeyAndDelimiter(String key, String delimiter) {
    return splitPropertyValues(getProperty(key), delimiter);
  }
}
