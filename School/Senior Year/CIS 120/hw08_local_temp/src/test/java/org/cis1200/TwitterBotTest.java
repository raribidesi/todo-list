package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Tests for TwitterBot class */
public class TwitterBotTest {

    /*
     * This tests whether your TwitterBot class itself is written correctly
     *
     * This test operates very similarly to our MarkovChain tests in its use of
     * `fixDistribution`, so make sure you know how to test MarkovChain before
     * testing this!
     */
    @Test
    public void simpleTwitterBotTest() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", "comes", "from", "data", "with", "no", "duplicate", "words", ".",
                        "the", "end", "should", "come", "."
                )
        );
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);
        t.fixDistribution(desiredTweet);

        String expected = "this comes from data with no duplicate words. the end should come.";
        String actual = TweetParser.replacePunctuation(t.generateTweet(12));
        assertEquals(expected, actual);
    }

    /*
     * This is the empty file test case.
     *
     * When your CSV file is empty, your program should create an empty MarkovChain.
     * An empty tweet should be generated by your bot.
     * No exceptions should be thrown and your program should not go into an
     * infinite loop!
     */
    @Test
    public void emptyFileCreatesEmptyTweet() {
        // Checks that your program does not go into an infinite loop
        assertTimeoutPreemptively(
                Duration.ofSeconds(10), () -> {
                    // No exceptions are thrown if file is empty
                    TwitterBot tb = new TwitterBot(
                            FileLineIterator.fileToReader("./files/empty.csv"), 2
                    );
                    // Checks that the bot creates an empty tweet w
                    assertEquals(0, tb.generateTweet(10).length());
                }
        );
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    @Test
    public void twitterAllBadWords() {
        List<String> desiredTweet = new ArrayList<>(Arrays.asList());
        String words = "0, $bad $words $bad.\n" + "1, $bad $words $bad\n"
                + "2, $bad $words $bad";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot tw = new TwitterBot(br, 1);

        String expected = "";
        String actual = TweetParser.replacePunctuation(tw.generateTweet(14));
        assertEquals(expected, actual);
    }

    @Test
    public void twitterBotSameWord() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", ".", "this", ".", "this", "."
                )
        );
        String words = "0, this";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot tw = new TwitterBot(br, 1);

        String expected = "this. this. this.";
        String actual = TweetParser.replacePunctuation(tw.generateTweet(3));
        assertEquals(expected, actual);
    }

    @Test
    public void twitterBotNegativeNumber() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", "comes", "from", "data", "with", "no", "duplicate", "words", ".",
                        "the", "end", "should", "come", "."
                )
        );
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);
        t.fixDistribution(desiredTweet);

        String expected = "0, The end should come here.\n" +
                "1, This comes from data with no duplicate words";
        assertThrows(IllegalArgumentException.class, () -> {
            t.generateTweet(-10);
        });
    }

}