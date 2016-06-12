package org.buckeram.jobscraper;

import java.io.IOException;

import org.buckeram.jobscraper.exception.ConfigurationException;
import org.buckeram.jobscraper.scraper.IrishJobsScraper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ConfigurationException, IOException
    {
        final Configuration configuration = new Configuration(System.getProperties());
        final IrishJobsScraper scraper = new IrishJobsScraper(configuration);
        scraper.scrape();
    }
}
