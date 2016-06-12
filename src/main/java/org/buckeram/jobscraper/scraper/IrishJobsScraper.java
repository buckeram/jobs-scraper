package org.buckeram.jobscraper.scraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.buckeram.jobscraper.Configuration;
import org.buckeram.jobscraper.domain.JobSpec;
import org.buckeram.jobscraper.domain.SearchResults;
import org.buckeram.jobscraper.parser.IrishJobsJobSpecParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 */
@RequiredArgsConstructor
public class IrishJobsScraper
{
    @NonNull
    private Configuration configuration;

    public void scrape() throws IOException
    {
        final Set<URL> jobLinks = getJobLinks(this.configuration.getUrl(), this.configuration.getTimeout());
        final Set<JobSpec> jobSpecs = getJobSpecs(jobLinks);
    }

    private Set<URL> getJobLinks(final URL url, final int timeout) throws IOException
    {
        Optional<URL> next = Optional.of(url);
        final Set<URL> result = new HashSet<>();
        while (next.isPresent())
        {
            final Document doc = Jsoup.parse(next.get(), timeout);
            final SearchResults searchResults = parseSearchResults(doc);
            result.addAll(searchResults.getJobLinks());
            next = searchResults.getNextPage();
        }

        return result;
    }

    // Visible for testing
    SearchResults parseSearchResults(final Document doc) throws MalformedURLException
    {
        final SearchResults results = new SearchResults();

        final Elements links = doc.select("a.show-more");
        for (final Element link: links)
        {
            results.addJobLink(new URL(link.attr("abs:href")));
        }

        final Element linkToNext = doc.select("ul#pagination > li > a:contains(>)").first();
        if (linkToNext != null)
        {
            results.setNextPage(new URL(linkToNext.attr("href")));
        }

        return results;
    }

    private Set<JobSpec> getJobSpecs(final Set<URL> jobLinks)
    {
        final Set<JobSpec> result = new HashSet<>();

        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final List<Future<JobSpec>> futures = new ArrayList<>(jobLinks.size());
        jobLinks.forEach(jobLink -> {
            final Future<JobSpec> future = executorService.submit(new IrishJobsJobSpecParser(
                    jobLink,
                    this.configuration.getTimeout()));
            futures.add(future);
        });

        for (final Future<JobSpec> future: futures)
        {
            try
            {
                result.add(future.get());
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

}
