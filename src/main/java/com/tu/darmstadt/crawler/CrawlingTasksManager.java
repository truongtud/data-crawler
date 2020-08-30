package com.tu.darmstadt.crawler;

import com.tu.darmstadt.model.ResultPage;
import com.tu.darmstadt.utils.CrawlerPropertyKeys;
import com.tu.darmstadt.utils.PropertyReader;
import com.tu.darmstadt.utils.Utils;
import com.tu.darmstadt.writer.CrawledContentWriter;
import com.tu.darmstadt.writer.Writer;
import com.tu.darmstadt.writer.WriterFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class CrawlingTasksManager {

  private static final Logger logger = LoggerFactory.getLogger(CrawlingTasksManager.class);

  private ExecutorService executorService;

  private PropertyReader propertiesReader;

  private Writer writer;

  public CrawlingTasksManager(PropertyReader propertiesReader) {
    this.propertiesReader = propertiesReader;
    this.executorService =
        Executors.newFixedThreadPool(
            propertiesReader.getIntegerProperty(CrawlerPropertyKeys.NUMBER_THREADS));
    this.writer =
        WriterFactory.getWriterFactory()
            .createWriter(
                CrawledContentWriter.class,
                propertiesReader.getProperty(CrawlerPropertyKeys.STORE_CONTENT_DIRECTORY))
            .get();
  }

  public void start(String baseUri, String source) {
    new InternalTaskManager().start(baseUri, source);
  }

  public void close() {
    this.executorService.shutdown();
  }

  private class InternalTaskManager {

    private ConcurrentLinkedQueue<Future<ResultPage>> results;

    private Set<String> visitedPages;

    private Set<String> allPages;

    private Map<String, String> linkAndContent;

    private List<String> mustHave;

    private List<String> blackList;

    private int timeout;

    InternalTaskManager() {
      this.results = new ConcurrentLinkedQueue<>();
      this.visitedPages = new LinkedHashSet<>();
      this.allPages = new HashSet<>();
      this.linkAndContent = new ConcurrentHashMap<>();
      this.timeout = propertiesReader.getIntegerProperty(CrawlerPropertyKeys.TIME_OUT);
      this.mustHave =
          propertiesReader.getValuesByKeyAndDelimiter(CrawlerPropertyKeys.MUST_HAVE, "||");
      this.blackList =
          propertiesReader.getValuesByKeyAndDelimiter(CrawlerPropertyKeys.BLACK_LIST, "||");
      System.out.println(mustHave);
    }

    public void start(String baseUri, String source) {
      if (Objects.nonNull(baseUri) && !baseUri.trim().isEmpty()) {
        addParsingTask(baseUri, source, 0);
        Future<ResultPage> future;
        while ((future = results.poll()) != null) {
          logger.info("number of results: {}", results.size());
          // Future<ResultPage> future = results.poll();
          handleResultPage(future);
        }
        logger.info("ending parsing {}", results.isEmpty());
      }
    }

    /**
     * @param baseUri
     * @param source
     * @param currentDepth
     */
    public void addParsingTask(@NotNull String baseUri, String source, int currentDepth) {
      AbstractCrawlingTask parserTask =
          (baseUri.contains("http:") || baseUri.contains("https:"))
              ? new CrawlingPageTask(baseUri, source, currentDepth, timeout, blackList, mustHave)
              : null;
      String fullPath = parserTask.getFullHtmlPath();
      if (Utils.isValidUrl(blackList, mustHave, fullPath)) {
        this.allPages.add(fullPath);
        this.visitedPages.add(fullPath);
        Future<ResultPage> future = executorService.submit(parserTask);
        results.add(future);
      }
    }

    /** @param result */
    public void handleResultPage(Future<ResultPage> result) {
      logger.info("size of blocking queue: {}", executorService);
      if (result != null) {
        try {
          ResultPage page = result.get();
          logger.info("done: {}", page.getFullHtmlPath());
          if (result.isDone()) {
            Set<String> subLinks = page.getSubLinks();
            // write crawled content
            if (page.getContent() != null) {
              UUID uuid =
                  UUID.nameUUIDFromBytes(page.getFullHtmlPath().getBytes(StandardCharsets.UTF_8));
              WriterFactory.getWriterFactory().write(writer, uuid.toString(), page.getContent());
            }
            if (Optional.ofNullable(subLinks).isPresent()) {
              subLinks
                  .parallelStream()
                  .filter(
                      sl ->
                          !sl.trim().isEmpty()
                              && !this.visitedPages.contains(sl)
                              && !this.allPages.contains(sl)
                              && page.getCurrentDepth()
                                  < propertiesReader.getIntegerProperty(
                                      CrawlerPropertyKeys.MAX_DEPTH))
                  .forEach(
                      link -> addParsingTask(page.getBaseUri(), link, page.getCurrentDepth() + 1));
            }
          }
        } catch (InterruptedException | ExecutionException e) {
          logger.error("exception: {} at task {}", e.getMessage(), result.getClass());
        }
      }
    }
  }
}
