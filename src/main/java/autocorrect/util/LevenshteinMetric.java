package autocorrect.util;

public class LevenshteinMetric
{
    /**
     * Compute the Levenshtein distance between {@code word1} and {@code word2}
     *
     * @param word1
     * @param word2
     * @return
     */
    public static int dist(String word1, String word2)
    {
        return dist(word1, word2, word1.length(), word2.length());
    }

    /**
     * Compute the Levenshtein distance between {@code word1} and {@code word2}, given both
     * their lengths. Algorithm description taken from Wikipedia.
     */
    private static int dist(String word1, String word2, int i, int j)
    {
        if (Math.min(i, j) == 0) return Math.max(i, j);
        int lev1 = dist(word1, word2, i - 1, j) + 1;
        int lev2 = dist(word1, word2, i, j - 1) + 1;
        int lev3 = dist(word1, word2, i - 1, j - 1) + indicator(word1.charAt(i - 1), word2.charAt(j - 1));
        return min(lev1, lev2, lev3);
    }

    private static int min(int i, int j, int k)
    {
        return Math.min(i, Math.min(j, k));
    }

    private static int indicator(char a, char b)
    {
        return a == b ? 0 : 1;
    }
}
