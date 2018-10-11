package autocorrect.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a response to the "/auto-correct" route
 * Is automagically converted into a JSON object by Spring
 */
public class AutoCorrectResponse extends BaseResponse
{
    private List<String> links;

    public AutoCorrectResponse()
    {
        links = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public AutoCorrectResponse(List<String> links)
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
