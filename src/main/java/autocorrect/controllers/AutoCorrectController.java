package autocorrect.controllers;

import autocorrect.domain.AutoCorrectResponse;
import autocorrect.exceptions.EmptyStringException;
import autocorrect.exceptions.ExceedsMaxWordLengthException;
import autocorrect.util.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoCorrectController
{
    public static final String EMPTY_STRING_ERROR = "The link provided is an empty string.";
    public static final String ABOVE_MAX_LENGTH_ERROR = "Link '%s' exceeds maximum word length.";

    @Autowired
    Tree tree;

    @RequestMapping(method = RequestMethod.GET, path = "/auto-correct/")
    public ResponseEntity<?> autoCorrect(@RequestParam(value = "link") String link)
    {
        AutoCorrectResponse autoCorrectResponse = new AutoCorrectResponse();
        HttpStatus status = HttpStatus.OK;

        try
        {
            autoCorrectResponse.setLinks(tree.getSimilarWords(link)); //Fetches similar words to link from the tree structure
        }
        catch (EmptyStringException e)
        {
            autoCorrectResponse.getErrors().add(EMPTY_STRING_ERROR);
            status = HttpStatus.BAD_REQUEST;
        }
        catch (ExceedsMaxWordLengthException e)
        {
            autoCorrectResponse.getErrors().add(String.format(ABOVE_MAX_LENGTH_ERROR, link));
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(autoCorrectResponse, status); //Return response
    }
}
