package org.buckeram.jobscraper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.buckeram.jobscraper.exception.ConfigurationException;

// TODO: Constructor takes non-null Properties, @Getters and @Setters for @NonNull configuration fields
/**
 *
 */
@Getter @Setter
@NoArgsConstructor
public class Configuration
{
    static final String URL = "jobscraper.url";
    static final String TIMEOUT = "jobscraper.timeout";
    static final int DEFAULT_TIMEOUT = 10000;

    /**
     * URL to retrieve the results from
     */
    @NonNull
    private URL url;
    /**
     * Connection and read timeout, in milliseconds. Defaults to 10000.
     */
    private int timeout = DEFAULT_TIMEOUT;

    public Configuration(@NonNull final Properties properties) throws ConfigurationException
    {
        try
        {
            setUrl(new URL(properties.getProperty(URL)));
            if (properties.containsKey(TIMEOUT))
            {
                setTimeout(Integer.parseInt(properties.getProperty(TIMEOUT)));
            }
        }
        catch (MalformedURLException e)
        {
            throw new ConfigurationException(e);
        }
    }

}
