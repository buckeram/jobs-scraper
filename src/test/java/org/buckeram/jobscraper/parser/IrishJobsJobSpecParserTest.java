package org.buckeram.jobscraper.parser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.buckeram.jobscraper.domain.JobSpec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

/**
 *
 */
public class IrishJobsJobSpecParserTest
{
    private IrishJobsJobSpecParser parser;

    private static final String DUMMY_URL = "http://not.important";

    @Before
    public void setup() throws MalformedURLException
    {
        this.parser = new IrishJobsJobSpecParser(new URL(DUMMY_URL), 0);
    }

    @Test
    public void jobUrl()
    {
        final String html = "<p>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals(DUMMY_URL, jobSpec.getUrl().toString());
    }

    @Test
    public void extractsTitleFromHtml()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <div style=\"font-size:11px;text-align:right;\">Ref:&nbsp;PBRECH/10753/IJ</div>\n" +
                "  <h1>Agile Software Development Manager, Limerick</h1>" +
                "</div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals("Agile Software Development Manager, Limerick", jobSpec.getTitle());
    }

    @Test
    public void extractsLocationsFromHtml()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <ul class=\"job-overview\"> " +
                "    <li class=\"location\">Clare / Limerick / Tipperary</li>" +
                "  </ul>" +
                "</div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        final Set<String> locations = jobSpec.getLocations();
        assertEquals(3, locations.size());
        assertThat(jobSpec.getLocations(), containsInAnyOrder("Clare", "Limerick", "Tipperary"));
    }

    @Test
    public void emptySetIfNoLocationsInHtml()
    {
        final String html = "<p>Nada</p>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals(0, jobSpec.getLocations().size());
    }

    @Test
    public void extractSalaryFromHtml()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <ul class=\"job-overview\"> " +
                "    <li class=\"salary\">40000 - 50000</li>" +
                "  </ul>" +
                "</div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals("40000 - 50000", jobSpec.getSalary());
    }

    @Test
    public void extractTypeFromHtml()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <ul class=\"job-overview\"> " +
                "    <li class=\"employment-type\">Contract</li>" +
                "  </ul>" +
                "</div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals("Contract", jobSpec.getType());
    }

    @Test
    public void extractUpdatedDateFromHtml()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <ul class=\"job-overview\"> " +
                "    <li class=\"updated-time\">Updated 10/06/2016</li>" +
                "  </ul>" +
                "</div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals("10/06/2016", new SimpleDateFormat("dd/MM/yyyy").format(jobSpec.getLastUpdated()));
    }

    @Test
    public void extractDescriptionFromHtml_discardsTags()
    {
        final String html = "<div class=\"job-description\" itemscope itemtype=\"http://schema.org/JobPosting\">\n" +
                "  <div class=\"job-details\">\n" +
                "    <h2>Description</h2>\n" +
                "    <p>Agile Software Development Manager, expanding international software company, Limerick City. See pbrec.ie for full details.</p>\n" +
                "    <p>&nbsp;</p>\n" +
                "    <p><p><strong>Agile Software Development Manager, Limerick </strong></p><p>Our client is a well established international company...</p></p>\n" +
                "</div></div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertTrue(jobSpec.getDescription().startsWith("Description"));
        assertTrue(jobSpec.getDescription().contains("Agile Software Development Manager, Limerick"));
        assertTrue(jobSpec.getDescription().contains("a well established international company"));
        assertFalse(jobSpec.getDescription().contains("strong"));
        assertFalse(jobSpec.getDescription().contains("<p>"));
    }

    @Test
    public void extractEmployerFromHtml()
    {
        final String html = "<div class=\"company-details\" itemprop=\"hiringOrganization\" itemscope itemtype=\"http://schema.org/Organization\">\n" +
                "  <img src='/Logos/PBRecruitment-Ltd-5449.gif' alt=\"PBRecruitment Ltd\" style=\"width:180px;\" />\n" +
                "  <div class=\"border-wrap\">\n" +
                "    <h2>PBRecruitment Ltd</h2>\n" +
                "  </div></div>";
        final Document doc = Jsoup.parse(html, "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);
        assertEquals("PBRecruitment Ltd", jobSpec.getEmployer());
    }

    @Test
    public void parsesFullJobSpecHtml() throws Exception
    {
        final File html = new File(this.getClass().getClassLoader().getResource("irishjobs.ie.jobspec.html").toURI());
        final Document doc = Jsoup.parse(html, "UTF-8", "http://www.irishjobs.ie");
        final JobSpec jobSpec = this.parser.parseJobSpec(doc);

        assertEquals("Agile Software Development Manager, Limerick", jobSpec.getTitle());
        assertTrue(jobSpec.getDescription().contains("a well established international company"));
        assertEquals("PBRecruitment Ltd", jobSpec.getEmployer());
        assertEquals("Permanent full-time", jobSpec.getType());
        assertEquals("See description", jobSpec.getSalary());
        assertEquals("10/06/2016", new SimpleDateFormat("dd/MM/yyyy").format(jobSpec.getLastUpdated()));
        assertThat(jobSpec.getLocations(), contains("Timbuktu"));
    }
}
