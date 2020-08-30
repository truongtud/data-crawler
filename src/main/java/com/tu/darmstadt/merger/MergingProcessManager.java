package com.tu.darmstadt.merger;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class MergingProcessManager {

  public void mergeContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName) {
    IMergingProcess mergingProcess = new MergingProcess();
    try {
      mergingProcess.mergeContentFilesInDirectory(inDirectory, outDirectory, outFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void mergeBigContentFilesInDirectory(
      String inDirectory, @Nullable String outDirectory, @Nullable String outFileName) {
    IMergingProcess mergingProcess = new MergingProcess();
    try {
      mergingProcess.mergeBigContentFilesInDirectory(inDirectory, outDirectory, outFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void mergeContentFilesInDirectories(
      List<String> inDirectories, @Nullable String outDirectory, @Nullable String outFileName) {}
}
