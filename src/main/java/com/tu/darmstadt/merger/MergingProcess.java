package com.tu.darmstadt.merger;

import com.tu.darmstadt.writer.MergedContentWriter;
import com.tu.darmstadt.writer.Writer;
import com.tu.darmstadt.writer.WriterFactory;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MergingProcess implements IMergingProcess {
  private final String DEFAULT_NAME = "_merged";
  private final String NEWLINE = System.getProperty("line.separator");

  @Override
  public void mergeContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName)
      throws IOException {
    Path path = Paths.get(inDirectory);
    if (Files.isDirectory(path)) {
      StringBuffer allContents = new StringBuffer();
      String outDir = Optional.ofNullable(outDirectory).orElse(inDirectory + DEFAULT_NAME);
      String outFN = Optional.ofNullable(outFileName).orElse(DEFAULT_NAME);
      Writer writer =
          WriterFactory.getWriterFactory().createWriter(MergedContentWriter.class, outDir).get();
      Files.list(path)
          .forEach(
              pathToFile -> {
                try {
                  InputStream inputStream = Files.newInputStream(pathToFile);
                  String content = IOUtils.toString(inputStream, Charset.defaultCharset());
                  if (content.length() > 50)
                    allContents.append(content).append(NEWLINE).append(NEWLINE);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
      WriterFactory.getWriterFactory().write(writer, outFN, allContents.toString());
    }
  }

  @Override
  public void mergeBigContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName)
      throws IOException {
    Path path = Paths.get(inDirectory);
    if (Files.isDirectory(path)) {
      String outDir = Optional.ofNullable(outDirectory).orElse(inDirectory + DEFAULT_NAME);
      String outFN = Optional.ofNullable(outFileName).orElse(DEFAULT_NAME);
      Writer writer =
          WriterFactory.getWriterFactory().createWriter(MergedContentWriter.class, outDir).get();
      Files.list(path)
          .forEach(
              pathToFile -> {
                try {
                  StringBuffer content = new StringBuffer();
                  // InputStreamReader inputStreamReader= new
                  // InputStreamReader(Files.newInputStream(pathToFile));
                  BufferedReader bufferedReader =
                      new BufferedReader(new FileReader(pathToFile.toFile()));
                  String line;
                  while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                    content.append(NEWLINE);
                  }
                  content.append(NEWLINE);
                  WriterFactory.getWriterFactory().write(writer, outFN, content.toString());
                  bufferedReader.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
    }
  }

  @Override
  public void mergeContentFilesInDirectories(
      List<String> inDirectories, @Nullable String outDirectory, @Nullable String outFileName) {}
}
