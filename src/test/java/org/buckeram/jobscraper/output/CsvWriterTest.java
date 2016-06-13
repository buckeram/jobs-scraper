package org.buckeram.jobscraper.output;

import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.buckeram.jobscraper.domain.JobSpec;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CsvWriterTest
{
    private static final String DESCRIPTION = "Beer taster";
    private static final String EMPLOYER = "Hipster Microbrewery";
    private static final String UPDATED = "17/12/2004";
    private static final String TITLE = "Software Developer";
    private static final String TYPE = "Contract";
    private static final String URL = "http://www.example.com";
    private static final String SALARY = "Negotiable";
    private static final String LOCATION1 = "E-Town til I die";
    private static final String LOCATION2 = "Tracy Island";

    private static final String HEADER_STRING = String.join(",", CsvWriter.getHeader());
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private StringWriter stringWriter;
    private CsvWriter csvWriter;


    @Before
    public void setup()
    {
        this.stringWriter = new StringWriter();
        this.csvWriter = new CsvWriter(this.stringWriter);
    }

    @Test
    public void testNoJobSpecs() throws Exception
    {
        final Set<JobSpec> jobSpecs = Collections.emptySet();
        this.csvWriter.process(jobSpecs);
        final String csv = this.stringWriter.toString();
        assertEquals(HEADER_STRING, csv.trim());
    }

    @Test
    public void testOneJobSpec() throws Exception
    {
        final JobSpec jobSpec = JobSpec.builder()
                .url(new URL(URL))
                .title(TITLE)
                .description(DESCRIPTION)
                .employer(EMPLOYER)
                .type(TYPE)
                .salary(SALARY)
                .location(LOCATION1).location(LOCATION2)
                .lastUpdated(DATE_FORMAT.parse(UPDATED))
                .build();

        this.csvWriter.process(Collections.singleton(jobSpec));

        final String csv = this.stringWriter.toString();
        final String[] lines = csv.split("\\n+");
        assertEquals(HEADER_STRING, lines[0].trim());
        assertEquals(expectedRow(jobSpec), lines[1].trim());
    }

    @Test
    public void testTwoJobSpecs() throws Exception
    {
        final JobSpec jobSpec1 = JobSpec.builder()
                .url(new URL(URL))
                .title(TITLE)
                .description(DESCRIPTION)
                .employer(EMPLOYER)
                .type(TYPE)
                .salary(SALARY)
                .location(LOCATION1).location(LOCATION2)
                .lastUpdated(DATE_FORMAT.parse(UPDATED))
                .build();
        final JobSpec jobSpec2 = JobSpec.builder()
                .url(new URL(URL + "/blah/blah/blah"))
                .title(TITLE + "2")
                .description(DESCRIPTION + "2")
                .employer(EMPLOYER + "2")
                .type(TYPE + "2")
                .salary(SALARY + "2")
                .location(LOCATION1 + "2").location(LOCATION2 + "2")
                .lastUpdated(DATE_FORMAT.parse(UPDATED))
                .build();

        final List<JobSpec> jobSpecs = Arrays.asList(jobSpec1, jobSpec2);
        this.csvWriter.process(jobSpecs);

        final String csv = this.stringWriter.toString();
        final String[] lines = csv.split("\\n+");
        assertEquals(HEADER_STRING, lines[0].trim());
        assertEquals(expectedRow(jobSpec1), lines[1].trim());
        assertEquals(expectedRow(jobSpec2), lines[2].trim());
    }

    private String expectedRow(final JobSpec jobSpec)
    {
        return new StringJoiner(",")
                .add(jobSpec.getUrl().toString())
                .add(jobSpec.getTitle())
                .add(jobSpec.getDescription())
                .add('"' + jobSpec.getLocations().toString() + '"')
                .add(jobSpec.getSalary())
                .add(jobSpec.getType())
                .add(jobSpec.getEmployer())
                .add(DATE_FORMAT.format(jobSpec.getLastUpdated()))
                .toString();
    }
}
