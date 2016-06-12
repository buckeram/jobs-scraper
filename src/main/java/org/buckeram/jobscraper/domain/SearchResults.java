package org.buckeram.jobscraper.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents the one page of job search results.
 * A search results page consists of:
 * <ul>
 *     <li>a collection of links to job descriptions</li>
 *     <li>a link to the next page of results (if one exists)</li>
 * </ul>
 */
@Data
@NoArgsConstructor
public class SearchResults
{
    private Set<URL> jobLinks = new HashSet<URL>();
    private Optional<URL> nextPage = Optional.empty();

    public void setNextPage(final URL nextPage)
    {
        this.nextPage = Optional.of(nextPage);
    }

    public void addJobLink(final URL link)
    {
        this.jobLinks.add(link);
    }
}
