package org.buckeram.jobscraper;

import java.io.IOException;

import org.buckeram.jobscraper.exception.ConfigurationException;
import org.buckeram.jobscraper.output.CsvWriter;
import org.buckeram.jobscraper.scraper.IrishJobsScraper;


public class App 
{
    public static void main( String[] args ) throws ConfigurationException, IOException
    {
        final Configuration configuration = new Configuration(System.getProperties());
        final IrishJobsScraper scraper = new IrishJobsScraper(configuration, new CsvWriter(System.out));
        scraper.scrape();
    }
}
