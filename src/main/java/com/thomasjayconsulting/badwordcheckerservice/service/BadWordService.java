package com.thomasjayconsulting.badwordcheckerservice.service;


import com.thomasjayconsulting.badwordcheckerservice.exception.BadWordLoadException;
import com.thomasjayconsulting.badwordcheckerservice.exception.CheckSentenceException;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bad Word Service
 *
 * @author Thomas Jay
 *
 */

@Service
@Data
@Slf4j
public class BadWordService {

    /**
     * bad word file location
     */
    @Value("${badword.file.location}")
    private String BAD_WORD_FILE_LOCATION;

    /**
     * holder for bad word Set
     */
    private Set<String> badWordSet = null;

    /**
     * Called post instantiation to load bad words for the first time
     */
    @PostConstruct
    private void loadAtStartup() {
        // Load Bad Words right after bean is created.
        // can't be done in contstrutor since we need the file location from the @Value
        try {
            loadBadWords();
        }
        catch (BadWordLoadException e) {
            log.error("loadAtStartup BadWordLoadException {}", e.getMessage());
        }
    }


    /**
     * Called to load bad words from a file path
     *
     * @exception  BadWordLoadException - failed to load bad words
     */
    public void loadBadWords() throws BadWordLoadException {
        try {
            // Read all the words from the file, each word is on its own line
            List<String> badWordList = Files.readAllLines(Paths.get(BAD_WORD_FILE_LOCATION));

            // Concert all words to lowerCase
            List <String> lowercaseBadWordList = badWordList.stream().map(String::toLowerCase).collect(Collectors.toList());

            // Make this a Set not a List, this will reduce duplicates
            badWordSet = Set.copyOf(lowercaseBadWordList);

            log.info("loadBadWords found " + badWordSet.size() + " bad words");

         }
        catch (Exception e) {
            log.error("loadBadWords Exception reading bad words {}", e.getMessage());

            throw new BadWordLoadException("loadBadWords Exception reading bad words e: " + e.getMessage());
        }

    }

    /**
     * Check input for any bad words
     *
     * @param sentence - sentence to check for containing bad words
     *
     * @return Set of matching bad words if any found
     *
     * @exception CheckSentenceException - Something went wrong
     */
    public Set checkSentence(String sentence) throws CheckSentenceException {
        log.info("checkSentence started. sentence: {}", sentence);

        try {
            // Convert incoming sentence to lowerCase and check againsts bad words
            return containsWords(sentence.toLowerCase(), badWordSet);
        }
        catch (Exception e) {
            throw new CheckSentenceException("Failed to check sentence");
        }
    }


    /**
     * containsWords
     *
     * Check input for any bad words against the bad words passed in
     *
     * @param sentence - sentence to check for containing bad words
     * @param badWords - Set of Bad Words previously loaded from a file
     *
     * @return Set of matching bad words if any found
     *
     * @exception Exception - Something wen wrong
     */

    private Set containsWords(String sentence, Set<String> badWords) throws Exception {

        log.info("containsWords started.");

        // If we have any bad words then there can't be a match
        if (badWordSet == null) {
            return new HashSet<String>();
        }

        Set<String> wordsInSentence = new HashSet<>(Arrays.asList(sentence.split(" ")));

        log.info("containsWords wordsInSentence size: {}", wordsInSentence.size());
        log.info("containsWords badWords size: {}", badWords.size());

        Set<String> matches = badWords.stream().filter(e -> wordsInSentence.contains(e)).collect(Collectors.toSet());

        log.info("containsWords matches size: {}", matches.size());

        log.info("containsWords matches {}", matches.toString());

        return matches;
    }

}
