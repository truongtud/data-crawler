package com.tu.darmstadt.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Writer {
  private String outputDirectory;

  public Writer(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    createDir();
  }

  private void createDir() {
    try {
      Files.createDirectories(Paths.get(outputDirectory));
    } catch (IOException e) {

    }
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public abstract void write(String fileName, String content);
}
