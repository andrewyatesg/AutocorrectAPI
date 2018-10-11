package autocorrect.domain;

import java.util.List;

/**
 * Wrapper object for the list of strings passed in when POSTing to the "/insert-words" route
 */
public class InsertWordsRequest
{
    private List<String> links;

    public InsertWordsRequest()
    {
    }

    public InsertWordsRequest(List<String> links)
    {
        this.links = links;
    }

    public List<String> getLinks()
    {
        return links;
    }

    public void setLinks(List<String> links)
    {
        this.links = links;
    }
}
