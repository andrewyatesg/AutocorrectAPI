package autocorrect.util;

import autocorrect.exceptions.EmptyStringException;
import autocorrect.exceptions.ExceedsMaxWordLengthException;
import autocorrect.exceptions.TreeFullException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Tree
{
    private static final int MAX_WORD_LENGTH = 20;
    private static final int MAX_TREE_SIZE = 150;

    /**
     * The maximum distance between two words for them to be considered "similar"
     */
    private static final int MAX_DIST = 2;

    private TreeNode root;
    private int size;

    public Tree()
    {
        size = 0;
    }

    public int size()
    {
        return size;
    }

    /**
     * Returns all words X in the tree with {@code dist(word, X) <= 2}
     *
     * @param word word to find all words similar to
     * @return list of similar words
     * @throws EmptyStringException if {@code word} is an empty string
     * @throws ExceedsMaxWordLengthException if {@code word} exceeds max word length
     */
    public List<String> getSimilarWords(String word) throws EmptyStringException, ExceedsMaxWordLengthException
    {
        if (isEmptyWord(word)) throw new EmptyStringException();
        if (isAboveMaxWordLength(word)) throw new ExceedsMaxWordLengthException();
        List<String> similarWords = new ArrayList<>();
        return getSimilarWords(word, similarWords, root); //Recursive method for finding similar words
    }

    /**
     * Returns all words X in the subtree with root node {@code currentNode} with {@code dist(word, X) <= 2}
     *
     * @param word word to find all words similar to
     * @param similarWords list of similar words
     * @param currentNode root node of subtree to search
     * @return list of similar words
     */
    private List<String> getSimilarWords(String word, List<String> similarWords, TreeNode currentNode)
    {
        if (currentNode == null) return similarWords;
        //Distance between specified word and current node
        int currDist = LevenshteinMetric.dist(word, currentNode.word);
        //Adds word to similar words list if it's less than max distance
        if (currDist <= MAX_DIST) similarWords.add(currentNode.word);
        //Loop through each child node with edge between currDist - 2 and currDist + 2
        for (int dist = currDist - MAX_DIST; dist <= currDist + MAX_DIST; dist++)
        {
            //Calls getSimilarWords() recursively on each subtree of the children nodes we're looping through
            // Each node X of the subtree will have dist(word, X) between currDist - 2 and currDist + 2
            // We continue to do this until no more children nodes are found
            if (currentNode.connections.containsKey(dist)) similarWords = getSimilarWords(word, similarWords, currentNode.connections.get(dist));
        }
        return similarWords;
    }

    public boolean isValidWord(String word)
    {
        return !isAboveMaxTreeSize() && !isEmptyWord(word) && !isAboveMaxWordLength(word);
    }

    public List<String> addAll(List<String> words) throws TreeFullException, EmptyStringException, ExceedsMaxWordLengthException
    {
        List<String> responses = new ArrayList<>();
        for (String word : words)
        {
            responses.add(addWord(word));
        }
        return responses;
    }

    /**
     * Adds a new word to the Tree in its proper location
     *
     * @param word new word to add
     * @return "none" if tree was previously empty, "not_inserted" if {@code word} already exists inside the tree OR {@code word} is empty OR {@code word} is above max word length
     *                    and the parent node of added word otherwise
     * @throws TreeFullException if adding {@code word} causes Tree to exceed it's maximum size
     */
    public String addWord(String word) throws TreeFullException, EmptyStringException, ExceedsMaxWordLengthException
    {
        if (isAboveMaxTreeSize()) throw new TreeFullException();
        if (isEmptyWord(word)) throw new EmptyStringException();
        if (isAboveMaxWordLength(word)) throw new ExceedsMaxWordLengthException();
        if (root == null)
        {
            //If tree is empty, we just put word at root
            root = new TreeNode(word);
            size++;
            return "none";
        }
        return addWord(word, root);
    }

    /**
     * Recursive method for adding a new word, given a root node
     *
     * @param word new word to add
     * @param root root of subtree to add the new word to
     * @return "none" if tree was previously empty, "not_inserted" if {@code word} already exists inside the tree OR {@code word} is empty OR {@code word} is above max word length
     *                    and the parent node of added word otherwise
     */
    private String addWord(String word, TreeNode root)
    {
        //When adding a duplicate word, it will walk down the same edges in the tree as its duplicate entry,
        // so at some point we encounter it, if it exists, and then we return "not_inserted"
        if (word.equals(root.word)) return "not_inserted";
        //Find distance between new word and root word
        int dist = LevenshteinMetric.dist(word, root.word);
        //Find child of root with same distance to root as the new word
        TreeNode sameDistNode = root.connections.get(dist);
        if (sameDistNode == null)
        {
            //If no such child exists, we add new word as child of root
            root.connections.put(dist, new TreeNode(word));
            size++;
            return root.word; //return parent of added node
        }
        else
        {
            //If such a child exists, we call this method again but with root set to node of the same distance child
            return addWord(word, sameDistNode);
        }
    }

    public boolean isEmptyWord(String word)
    {
        return word.length() <= 0;
    }

    public boolean isAboveMaxWordLength(String word)
    {
        return word.length() > MAX_WORD_LENGTH;
    }

    public boolean isAboveMaxTreeSize()
    {
        //Check is performed before adding, so if tree is currently at MAX_TREE_SIZE, we should throw exception
        return size >= MAX_TREE_SIZE;
    }

    /**
     * Returns whether adding {@code newElements} to list will put tree above its capacity.
     *
     * @param newElements number of new elements to add to list
     * @return tree will go over it's capacity after adding {@code newElements} number of elements
     */
    public boolean isAboveMaxTreeSize(int newElements)
    {
        return size + newElements > MAX_TREE_SIZE;
    }

    private class TreeNode
    {
        /**
         * Word stored by this {@code TreeNode}
         */
        String word;

        /**
         * Map containing all children of current {@code TreeNode}. Each TreeNode in the map has an associated
         * integer which is the Levenshtein distance from current {@code TreeNode} to the child.
         */
        HashMap<Integer, TreeNode> connections;

        TreeNode(String word)
        {
            this.word = word;
            connections = new HashMap<>();
        }
    }
}
