package org.buckeram.jobscraper;

import java.net.URL;
import java.util.Properties;

import org.buckeram.jobscraper.exception.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 */
public class ConfigurationTest
{
    private static final String TEST_URL = "http://example.com";

    private Properties properties;


    @Before
    public void setup()
    {
        properties = new Properties();
        properties.put(Configuration.URL, TEST_URL);
    }

    @Test(expected = NullPointerException.class)
    public void constructorRejectsNull() throws Exception
    {
        new Configuration(null);
    }

    @Test(expected = ConfigurationException.class)
    public void rejectsEmptyProperties() throws Exception
    {
        new Configuration(new Properties());
    }

    @Test
    public void readsUrlFromProperties() throws Exception
    {
        final Configuration configuration = new Configuration(this.properties);
        assertEquals(new URL(TEST_URL), configuration.getUrl());
    }

    @Test(expected = ConfigurationException.class)
    public void urlMustNotBeEmpty() throws Exception
    {
        this.properties.put(Configuration.URL, "");
        new Configuration(this.properties);
        fail("Should have failed for empty URL");
    }

    @Test(expected = ConfigurationException.class)
    public void rejectsInvalidUrl() throws Exception
    {
        this.properties.put(Configuration.URL, "http////This is invalid");
        new Configuration(this.properties);
        fail("Should have failed for invalid URL");
    }

    @Test
    public void defaultTimeout() throws Exception
    {
        final Configuration configuration = new Configuration(this.properties);
        assertEquals(Configuration.DEFAULT_TIMEOUT, configuration.getTimeout());
    }

    @Test
    public void readsTimeoutFromProperties() throws Exception
    {
        final int expected = 123456789;
        this.properties.put(Configuration.TIMEOUT, String.valueOf(expected));
        final Configuration configuration = new Configuration(this.properties);
        assertEquals(expected, configuration.getTimeout());
    }
}
