package org.buckeram.jobscraper.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.buckeram.jobscraper.domain.JobSpec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 */
@RequiredArgsConstructor
public class IrishJobsJobSpecParser implements Callable<JobSpec>
{
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("'Updated 'dd/MM/yyyy");

    @NonNull
    private final URL jobLink;
    private final int timeout;

    @Override
    public JobSpec call() throws Exception
    {
        final Document doc = Jsoup.parse(this.jobLink, this.timeout);
        return parseJobSpec(doc);
    }

    public JobSpec parseJobSpec(final Document doc)
    {
        return JobSpec.builder()
                .url(this.jobLink)
                .title(extractTitle(doc))
                .locations(extractLocations(doc))
                .description(extractDescription(doc))
                .employer(extractEmployer(doc))
                .salary(extractSalary(doc))
                .type(extractType(doc))
                .lastUpdated(extractLastUpdatedDate(doc))
                .build();

    }

    private String extractTitle(final Document doc)
    {
        return selectText(doc, "div.job-description > h1");
    }

    private Set<String> extractLocations(final Document doc)
    {
        final Set<String> result = new HashSet<>();
        final String locationString = selectText(doc, "div.job-description > ul.job-overview > li.location");
        if (locationString.trim().length() > 0)
        {
            Collections.addAll(result, locationString.split("\\s*/\\s*"));
        }

        return result;
    }

    private String extractDescription(final Document doc)
    {
        return selectText(doc, "div.job-description > div.job-details");
    }

    private String extractEmployer(final Document doc)
    {
        return selectText(doc, "div.company-details > div > h2");
    }

    private String extractSalary(final Document doc)
    {
        return selectText(doc, "ul.job-overview > li.salary");
    }

    private String extractType(final Document doc)
    {
        return selectText(doc, "ul.job-overview > li.employment-type");
    }

    private Date extractLastUpdatedDate(final Document doc)
    {
        Date result = null;
        try
        {
            result = DATE_FORMAT.parse(selectText(doc, "ul.job-overview > li.updated-time:not([style])"));
        }
        catch (ParseException ignore) { }

        return result;
    }

    private String selectText(final Document doc, final String selection)
    {
        String result = "";
        final Element element = doc.select(selection).first();
        if (element != null)
        {
            result = element.text();
        }
        return result;
    }
}
