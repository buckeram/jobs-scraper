package org.buckeram.jobscraper.scraper;

import java.io.File;
import java.net.URISyntaxException;

import org.buckeram.jobscraper.Configuration;
import org.buckeram.jobscraper.domain.SearchResults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *
 */
public class IrishJobsScraperTest
{
    private IrishJobsScraper scraper;

    @Before
    public void setup() throws URISyntaxException
    {
        final Configuration configuration = new Configuration();
        configuration.setUrl(this.getClass().getClassLoader().getResource("irishjobs.ie.html"));
        this.scraper = new IrishJobsScraper(configuration);
    }

    @Test
    public void scrapeIrishJobsSearchResults() throws Exception
    {
        final File html = new File(this.getClass().getClassLoader().getResource("irishjobs.ie.html").toURI());
        final Document doc = Jsoup.parse(html, "UTF-8", "http://www.irishjobs.ie");
        final SearchResults searchResults = this.scraper.parseSearchResults(doc);
        assertEquals(25, searchResults.getJobLinks().size());
        assertFalse(searchResults.getNextPage().isPresent());
    }
}
