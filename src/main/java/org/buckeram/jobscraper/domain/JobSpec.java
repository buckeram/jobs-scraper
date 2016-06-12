package org.buckeram.jobscraper.domain;

import lombok.Builder;
import java.net.URL;
import java.util.Date;
import java.util.Set;

/**
 * Details of a Job
 */
@Builder
public class JobSpec
{
    /**
     * Location of the job spec
     */
    private URL url;
    /**
     * Job title; e.g. "Java J2EE Developer"
     */
    private String title;
    /**
     * Description/details of the job
     */
    private String description;
    /**
     * Description of the salary offered; e.g. "40000 - 50000", "Negotiable", etc.
     */
    private String salary;
    /**
     * Name of the employer (or agency) advertising the job
     */
    private String employer;
    /**
     * True if the job is full-time, false otherwise
     */
    private boolean fullTime;
    /**
     * Employment type; e.g. "Permanent", "Contract"
     * TODO maybe this could be a boolean
     */
    private String type;
    /**
     * Locations for which the job is advertised; e.g. "Clare", "Limerick", "Tipperary"
     */
    private Set<String> locations;
    /**
     * Date on which the job advert was last updated
     */
    private Date lastUpdated;
}
