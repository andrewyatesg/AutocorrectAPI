package autocorrect;

import autocorrect.exceptions.EmptyStringException;
import autocorrect.exceptions.ExceedsMaxWordLengthException;
import autocorrect.exceptions.TreeFullException;
import autocorrect.util.Tree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TreeTest
{
    Tree tree;

    @Before
    public void createTree()
    {
        tree = new Tree();
    }

    @Test
    public void testAddingEmptyString()
    {
        assertThrows(EmptyStringException.class, () -> tree.addWord(""));
    }

    @Test
    public void testAddingDuplicates() throws TreeFullException, ExceedsMaxWordLengthException, EmptyStringException
    {
        assertEquals("none", tree.addWord("lol"));
        assertEquals("lol", tree.addWord("hi"));
        assertEquals("not_inserted", tree.addWord("hi"));
        assertEquals("not_inserted", tree.addWord("lol"));
    }

    @Test
    public void testAddingRootDuplcate() throws TreeFullException, ExceedsMaxWordLengthException, EmptyStringException
    {
        assertEquals("none", tree.addWord("lol"));
        assertEquals("not_inserted", tree.addWord("lol"));
    }

    @Test
    public void testAddingExceedsMaxWordLength()
    {
        assertThrows(ExceedsMaxWordLengthException.class, () -> tree.addWord("qwertyuiopasdfghjklzx"));
        try
        {
            tree.addWord("qwertyuiopasdfghjklz");
        }
        catch (ExceedsMaxWordLengthException | EmptyStringException | TreeFullException e)
        {
            fail();
        }
    }

    @Test
    public void testAddingExceedsTreeSize()
    {
        for (int i = 0; i < 150; i++)
        {
            try
            {
                tree.addWord("word" + i);
            }
            catch (ExceedsMaxWordLengthException | EmptyStringException | TreeFullException e)
            {
                fail();
            }
        }

        assertThrows(TreeFullException.class, () -> tree.addWord("exceeds"));
    }

    @Test
    public void testGetSimilarWordEmptyWord()
    {
        assertThrows(EmptyStringException.class, () -> tree.getSimilarWords(""));
    }

    @Test
    public void testGetSimilarWordEmptyTree() throws EmptyStringException, ExceedsMaxWordLengthException
    {
        assertTrue(tree.getSimilarWords("a").isEmpty());
    }

    @Test
    public void testGetSimilarWord() throws TreeFullException, ExceedsMaxWordLengthException, EmptyStringException
    {
        tree.addWord("lol");
        tree.addWord("lolo");
        tree.addWord("loa");
        tree.addWord("aol");
        tree.addWord("bob");
        tree.addWord("zoz");
        tree.addWord("tree");
        tree.addWord("tree1");
        tree.addWord("treee1");

        List<String> similarWordsLol = new ArrayList<>();
        similarWordsLol.addAll(Arrays.asList("lol", "lolo", "loa", "aol", "bob", "zoz"));
        List<String> actualSimilarWordsLol = tree.getSimilarWords("lol");
        assertTrue(similarWordsLol.containsAll(actualSimilarWordsLol));
        assertEquals(6, actualSimilarWordsLol.size());

        List<String> similarWordsTree = new ArrayList<>();
        similarWordsTree.addAll(Arrays.asList("tree", "tree1", "treee1"));
        List<String> actualSimilarWordsTree = tree.getSimilarWords("tree");
        assertTrue(similarWordsTree.containsAll(actualSimilarWordsTree));
        assertEquals(3, actualSimilarWordsTree.size());
    }
}
