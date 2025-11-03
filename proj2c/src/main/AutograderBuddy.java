package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String synsetFile, String hyponymFile,
            String wordFile, String countFile) {

        return new HyponymsHandler(synsetFile, hyponymFile, wordFile, countFile);
    }
}
