package com.tu.darmstadt.cleaner;

import com.tu.darmstadt.utils.CleanerPropertyKeys;
import com.tu.darmstadt.utils.PropertyReader;
import com.tu.darmstadt.writer.CleanedContentWriter;
import com.tu.darmstadt.writer.Writer;
import com.tu.darmstadt.writer.WriterFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CleansingProcessManager {

  private PropertyReader propertiesReader;

  public CleansingProcessManager(PropertyReader propertiesReader) {
    this.propertiesReader = propertiesReader;
  }

  public void clean() {
    List<String> directories =
        this.propertiesReader.getValuesByKeyAndDelimiter(
            CleanerPropertyKeys.CLEANED_DIRECTORIES, ";");
    directories.stream()
        .forEach(
            inDirectory -> {
              Path path = Paths.get(inDirectory);
              System.out.println(Files.isDirectory(path));
              String outputDirectory = inDirectory + "_cleaned";
              Writer writer =
                  WriterFactory.getWriterFactory()
                      .createWriter(CleanedContentWriter.class, outputDirectory)
                      .get();
              ICleansingProcess cleansingProcess = new CleansingProcess(propertiesReader, writer);
              if (Files.isDirectory(path)) {
                try {
                  Files.list(path)
                      .forEach(
                          p -> {
                            System.out.println(p.getFileName());
                            cleansingProcess.clean(inDirectory, p.getFileName().toString());
                          });
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });
  }
}
