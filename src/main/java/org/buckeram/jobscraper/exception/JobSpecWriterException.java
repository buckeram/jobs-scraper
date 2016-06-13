package org.buckeram.jobscraper.exception;

import java.io.IOException;

/**
 *
 */
public class JobSpecWriterException extends IOException
{
    public JobSpecWriterException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
