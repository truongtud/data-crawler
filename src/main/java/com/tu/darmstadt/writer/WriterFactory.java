package com.tu.darmstadt.writer;

import java.util.Optional;

public class WriterFactory {

  private static Optional<WriterFactory> instance = Optional.empty();

  public static WriterFactory getWriterFactory() {
    if (!instance.isPresent()) {
      instance = Optional.of(new WriterFactory());
    }
    return instance.get();
  }

  public void write(Writer writer, String fileName, String content) {
    writer.write(fileName, content);
  }

  public Optional<Writer> createWriter(Class clazz, String directory) {
    if (clazz.getSimpleName().equals(CleanedContentWriter.class.getSimpleName()))
      return Optional.of(new CleanedContentWriter(directory));
    else if (clazz.getSimpleName().equals(CrawledContentWriter.class.getSimpleName()))
      return Optional.of(new CrawledContentWriter(directory));
    else if (clazz.getSimpleName().equals(MergedContentWriter.class.getSimpleName()))
      return Optional.of(new MergedContentWriter(directory));
    else return Optional.empty();
  }
}
