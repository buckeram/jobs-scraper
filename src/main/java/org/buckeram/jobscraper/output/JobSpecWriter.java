package org.buckeram.jobscraper.output;

import java.util.Collection;

import org.buckeram.jobscraper.domain.JobSpec;
import org.buckeram.jobscraper.exception.JobSpecWriterException;

/**
 *
 */
public interface JobSpecWriter
{
    void process(Collection<JobSpec> jobSpecs) throws JobSpecWriterException;
}
