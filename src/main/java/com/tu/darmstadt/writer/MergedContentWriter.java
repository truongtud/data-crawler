package com.tu.darmstadt.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MergedContentWriter extends Writer {

  protected MergedContentWriter(String outputDirectory) {
    super(outputDirectory);
  }

  @Override
  public void write(String fileName, String content) {
    try {
      // write big text using buffer
      File file = new File(getOutputDirectory() + File.separator + fileName);
      BufferedWriter bufferedWriter =
          new BufferedWriter(
              new FileWriter(getOutputDirectory() + File.separator + fileName, true));
      bufferedWriter.write(content);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
