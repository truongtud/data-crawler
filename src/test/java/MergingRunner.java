import com.tu.darmstadt.merger.MergingProcessManager;

public class MergingRunner {
  public static void main(String[] args) {

    MergingProcessManager mergingProcessManager = new MergingProcessManager();
    mergingProcessManager.mergeContentFilesInDirectory(
        "S:/../german_medical_netdoktor_cleaned", null, null);
    mergingProcessManager.mergeContentFilesInDirectory(
        "S:/../german_medical_flexikon_doccheck_cleaned", null, null);
    mergingProcessManager.mergeBigContentFilesInDirectory(
        "S:/../german_medical_apotheken_umschau_cleaned", null, null);
    mergingProcessManager.mergeBigContentFilesInDirectory(
        "S:\\PretrainedDE.wiki\\german_medical_gesundheit_cleaned", null, null);
    mergingProcessManager.mergeBigContentFilesInDirectory(
        "S:/../german_medical_data_merged", "S:/../german_medical_data_merged", "all_merged");
  }
}
