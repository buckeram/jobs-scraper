package org.buckeram.jobscraper.output;

import java.util.Collection;

import org.buckeram.jobscraper.domain.JobSpec;

/**
 *
 */
public class StandardOutWriter implements JobSpecWriter
{
    @Override
    public void process(final Collection<JobSpec> jobSpecs)
    {
        jobSpecs.forEach(System.out::println);
    }
}
