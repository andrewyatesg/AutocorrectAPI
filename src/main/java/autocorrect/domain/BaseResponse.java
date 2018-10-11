package autocorrect.domain;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse
{
    protected List<String> errors;

    public BaseResponse()
    {
        errors = new ArrayList<>();
    }

    public BaseResponse(List<String> errors)
    {
        this.errors = errors;
    }

    public List<String> getErrors()
    {
        return errors;
    }

    public void setErrors(List<String> errors)
    {
        this.errors = errors;
    }
}
