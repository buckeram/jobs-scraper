package org.buckeram.jobscraper.scraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.buckeram.jobscraper.Configuration;
import org.buckeram.jobscraper.domain.SearchResults;
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
        final Set<URL> jobLinks = getJobLinks();

    }

    private Set<URL> getJobLinks() throws IOException
    {
        Optional<URL> next = Optional.of(this.configuration.getUrl());
        final Set<URL> result = Collections.emptySet();
        while (next.isPresent())
        {
            final Document doc = Jsoup.parse(next.get(), this.configuration.getTimeout());
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
}
