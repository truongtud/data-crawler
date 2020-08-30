package com.tu.darmstadt.application;

import com.tu.darmstadt.cleaner.CleansingProcessManager;
import com.tu.darmstadt.utils.PropertyReader;

public class CleanerApplication {
  private final String ENV = "CRSP_MEDICAL_DATA_CLEANER";
  private final String propertiesFile = "clean.properties";
  private PropertyReader propertiesReader;
  private CleansingProcessManager cleansingProcessManager;

  public CleanerApplication() {
    this.propertiesReader = new PropertyReader(ENV, propertiesFile);
    this.cleansingProcessManager = new CleansingProcessManager(propertiesReader);
  }

  public void clean() {
    this.cleansingProcessManager.clean();
  }
}
