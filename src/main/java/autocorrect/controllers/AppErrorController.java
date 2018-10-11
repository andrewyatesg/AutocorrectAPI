package autocorrect.controllers;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Controller which is called for unhandled errors
 */
@Controller
public class AppErrorController implements ErrorController
{
    private ErrorAttributes errorAttributes;

    private final static String ERROR_PATH = "/error";

    public AppErrorController(ErrorAttributes errorAttributes)
    {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request)
    {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = getStatus(request);
        body.put("errors", Collections.singleton(status.getReasonPhrase()));
        return new ResponseEntity<>(body, status);
    }

    @Override
    public String getErrorPath()
    {
        return ERROR_PATH;
    }
    private HttpStatus getStatus(HttpServletRequest request)
    {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null)
        {
            try
            {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex)
            {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}