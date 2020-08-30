package com.tu.darmstadt.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CrawledContentWriter extends Writer {

  public CrawledContentWriter(String directory) {
    super(directory);
  }

  @Override
  public void write(String fileName, String content) {
    try {
      File file = new File(getOutputDirectory() + File.separator + fileName + ".txt");
      OutputStream out = new FileOutputStream(file);
      out.write(content.getBytes());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
