package com.tu.darmstadt.merger;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public interface IMergingProcess {
  void mergeContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName)
      throws IOException;

  void mergeContentFilesInDirectories(
      List<String> inDirectories, @Nullable String outDirectory, @Nullable String outFileName)
      throws IOException;

  void mergeBigContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName)
      throws IOException;
}
