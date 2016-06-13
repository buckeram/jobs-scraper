package org.buckeram.jobscraper.output;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.buckeram.jobscraper.domain.JobSpec;
import org.buckeram.jobscraper.exception.JobSpecWriterException;

/**
 *
 */
@RequiredArgsConstructor
public class CsvWriter implements JobSpecWriter
{
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    enum Header
    {
        URL, TITLE, DESCRIPTION, LOCATIONS, SALARY, TYPE, EMPLOYER, LAST_UPDATED
    }

    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(Header.class);

    @NonNull
    private Appendable out;

    @Override
    public void process(final Collection<JobSpec> jobSpecs) throws JobSpecWriterException
    {
        try (final CSVPrinter csv = new CSVPrinter(this.out, FORMAT))
        {
            for (final JobSpec jobSpec: jobSpecs)
            {
                csv.print(jobSpec.getUrl());
                csv.print(jobSpec.getTitle());
                csv.print(jobSpec.getDescription());
                csv.print(jobSpec.getLocations());
                csv.print(jobSpec.getSalary());
                csv.print(jobSpec.getType());
                csv.print(jobSpec.getEmployer());
                csv.print(DATE_FORMAT.format(jobSpec.getLastUpdated()));
                csv.println();
            }
        }
        catch (final IOException e)
        {
            throw new JobSpecWriterException("Can't output the job specs as CSV", e);
        }
    }

    public static String[] getHeader()
    {
        return FORMAT.getHeader();
    }

}
