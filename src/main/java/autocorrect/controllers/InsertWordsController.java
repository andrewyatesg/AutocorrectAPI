package autocorrect.controllers;

import autocorrect.domain.InsertWordsRequest;
import autocorrect.domain.InsertWordsResponse;
import autocorrect.exceptions.EmptyStringException;
import autocorrect.exceptions.ExceedsMaxWordLengthException;
import autocorrect.exceptions.TreeFullException;
import autocorrect.util.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests and responses for inserting words
 */
@RestController
public class InsertWordsController
{
    public static final String EMPTY_STRING_ERROR = "One of the words provided is an empty string.";
    public static final String EMPTY_LIST_ERROR = "At least one word to insert must be provided.";
    public static final String TREE_FULL_ERROR = "The current request load will put the tree over it's maximum length and so no words were inserted.";
    public static final String ABOVE_MAX_LENGTH_ERROR = "Word '%s' exceeds maximum word length.";

    /**
     * Points to global instance of Tree structure
     */
    @Autowired
    Tree tree;

    /**
     * Handles an incoming request for inserting a new word into the tree
     *
     * @return a {@link InsertWordsResponse} representing a JSON response object
     */
    @RequestMapping(method = RequestMethod.POST, path = "/insert-words", consumes = "application/json")
    public ResponseEntity<?> insertWords(@RequestBody InsertWordsRequest insertWordsRequest)
    {
        InsertWordsResponse insertWordsResponse = new InsertWordsResponse();
        HttpStatus status = HttpStatus.OK;

        if (insertWordsRequest.getLinks() == null) //If links array is not present in request object
        {
            status = HttpStatus.BAD_REQUEST;
            insertWordsResponse.getErrors().add(EMPTY_LIST_ERROR);
        }
        else if (insertWordsRequest.getLinks().isEmpty()) //If links array is empty
        {
            status = HttpStatus.BAD_REQUEST;
            insertWordsResponse.getErrors().add(EMPTY_LIST_ERROR);
        }
        else if (tree.isAboveMaxTreeSize(insertWordsRequest.getLinks().size())) //Does inserting all the given words put tree above capacity?
        {
            status = HttpStatus.LOCKED; //Resource is locked because tree is above max size
            insertWordsResponse.getErrors().add(TREE_FULL_ERROR);
        }
        else
        {
            boolean emptyWord = false;

            for (String word : insertWordsRequest.getLinks()) //Check validity of each word provided by client
            {
                if (tree.isEmptyWord(word) && !emptyWord) //Only add empty word error once
                {
                    status = HttpStatus.BAD_REQUEST; //Client made bad request
                    insertWordsResponse.getErrors().add(EMPTY_STRING_ERROR);
                    emptyWord = true;
                }
                else if (tree.isAboveMaxWordLength(word))
                {
                    status = HttpStatus.BAD_REQUEST; //Client made bad request
                    insertWordsResponse.getErrors().add(String.format(ABOVE_MAX_LENGTH_ERROR, word));
                }
            }
        }

        if (status.is2xxSuccessful()) //If no errors were found above:
        {
            try
            {
                insertWordsResponse.setClosest_parent(tree.addAll(insertWordsRequest.getLinks()));
            }
            catch (TreeFullException | EmptyStringException | ExceedsMaxWordLengthException e)
            {
                status = HttpStatus.INTERNAL_SERVER_ERROR; //These errors should never be thrown due to checking above
            }
        }

        return new ResponseEntity<>(insertWordsResponse, status);
    }
}
