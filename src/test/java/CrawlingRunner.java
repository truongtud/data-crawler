import com.tu.darmstadt.application.CrawlerApplication;

public class CrawlingRunner {
  public static void main(String[] args) {
    CrawlerApplication application = new CrawlerApplication();
    application.crawl("https://www.neurologen-und-psychiater-im-netz.org/startseite/", "");
  }
}
