package org.buckeram.jobscraper.scraper;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.buckeram.jobscraper.Configuration;
import org.buckeram.jobscraper.domain.SearchResults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void parseShowMoreLinks() throws Exception
    {
        final String html = "<a class=\"show-more\" href='/Jobs/Senior-Java-Developer-Cork-SAAS-7857438.aspx'>Show More</a>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final SearchResults searchResults = this.scraper.parseSearchResults(doc);
        assertEquals(1, searchResults.getJobLinks().size());
        final URL jobLink = searchResults.getJobLinks().iterator().next();
        assertEquals("http://www.irishjobs.ie/Jobs/Senior-Java-Developer-Cork-SAAS-7857438.aspx", jobLink.toString());
    }

    @Test
    public void parseNextPage() throws Exception
    {
        final String html = "<ul id=\"pagination\">\n" +
                "  <li><a rel=\"nofollow\" class=\"active\">1</a></li>\n" +
                "  <li><a rel=\"nofollow\" href=\"http://www.irishjobs.ie/ShowResults.aspx?Keywords=java&amp;lr=4&amp;Location=45&amp;SortBy=MostRecent&amp;erbloc=4%7c42%7c48%7c61%7c44&amp;Page=2\">2</a></li>\n" +
                "  <li><a rel=\"nofollow\" class=\"alt\" href=\"http://www.irishjobs.ie/ShowResults.aspx?Keywords=java&amp;lr=4&amp;Location=45&amp;SortBy=MostRecent&amp;erbloc=4%7c42%7c48%7c61%7c44&amp;Page=2\">&gt;</a></li>\n" +
                "</ul>\n";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final SearchResults searchResults = this.scraper.parseSearchResults(doc);
        assertTrue(searchResults.getNextPage().isPresent());
        final URL nextPage = searchResults.getNextPage().get();
        assertEquals(
                "http://www.irishjobs.ie/ShowResults.aspx?Keywords=java&lr=4&Location=45&SortBy=MostRecent&erbloc=4%7c42%7c48%7c61%7c44&Page=2",
                nextPage.toString());
    }

    @Test
    public void noNextPage() throws Exception
    {
        final String html = "<ul id=\"pagination\">\n" +
                "  <li><a rel=\"nofollow\" class=\"alt\" href=\"http://www.irishjobs.ie/ShowResults.aspx?Keywords=java&amp;lr=4&amp;Location=45&amp;SortBy=MostRecent&amp;erbloc=4%7c42%7c48%7c61%7c44&amp;Page=1\">&lt;</a></li>\n" +
                "  <li><a rel=\"nofollow\" href=\"http://www.irishjobs.ie/ShowResults.aspx?Keywords=java&amp;lr=4&amp;Location=45&amp;SortBy=MostRecent&amp;erbloc=4%7c42%7c48%7c61%7c44&amp;Page=1\">1</a></li>\n" +
                "  <li><a rel=\"nofollow\" class=\"active\">2</a></li>\n" +
                "</ul>\n";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final SearchResults searchResults = this.scraper.parseSearchResults(doc);
        assertFalse(searchResults.getNextPage().isPresent());
    }

    @Test
    public void parseIrishJobsSearchResultsHtml() throws Exception
    {
        final File html = new File(this.getClass().getClassLoader().getResource("irishjobs.ie.html").toURI());
        final Document doc = Jsoup.parse(html, "UTF-8", "http://www.irishjobs.ie");
        final SearchResults searchResults = this.scraper.parseSearchResults(doc);
        assertEquals(25, searchResults.getJobLinks().size());
        assertFalse(searchResults.getNextPage().isPresent());
    }
}
