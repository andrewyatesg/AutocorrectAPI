package autocorrect.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a response to the "/insert-words" route
 * Is automagically converted into a JSON object by Spring
 */
public class InsertWordsResponse extends BaseResponse
{
    private List<String> closest_parent;

    public InsertWordsResponse()
    {
        closest_parent = new ArrayList<>();
    }

    public InsertWordsResponse(List<String> closest_parent, List<String> errors)
    {
        this.closest_parent = closest_parent;
    }

    public List<String> getClosest_parent()
    {
        return closest_parent;
    }

    public void setClosest_parent(List<String> closest_parent)
    {
        this.closest_parent = closest_parent;
    }
}
