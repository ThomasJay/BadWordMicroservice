package com.thomasjayconsulting.badwordcheckerservice.controller;

import com.thomasjayconsulting.badwordcheckerservice.dto.CheckSentenceRequest;
import com.thomasjayconsulting.badwordcheckerservice.dto.CheckSentenceResponse;
import com.thomasjayconsulting.badwordcheckerservice.exception.BadWordLoadException;
import com.thomasjayconsulting.badwordcheckerservice.exception.CheckSentenceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thomasjayconsulting.badwordcheckerservice.service.BadWordService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Rest API Controller end point
 *
 * @author Thomas Jay
 *
 */

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "BadWords Service", description = "This Rest Controller allows 2 Post methods. One to load the words and one to check a sentence.")
public class BadWordRestController {

    @Value("${apikey}")
    private String API_KEY;

    private BadWordService badWordService;

    BadWordRestController(BadWordService badWordService) {
        this.badWordService = badWordService;
    }

    @Operation(summary = "Load Bad words from file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File was loaded", content = @Content),
            @ApiResponse(responseCode = "400", description = "File load failed. See logs", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authorized", content = @Content)})
    @PostMapping("/loadwords")
    public ResponseEntity<?> loadWords(@RequestHeader(name = "apiKey", required = false) String apiKey) {

        if (apiKey == null || !apiKey.equals(API_KEY)) {
            return new ResponseEntity("Invalid apiKey", HttpStatus.UNAUTHORIZED);
        }

        try {
            badWordService.loadBadWords();

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("messages", "Successfully loaded bad words");

            return new ResponseEntity(responseMap, HttpStatus.OK);
        }
        catch (BadWordLoadException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "Failed");
            responseMap.put("messages", "Failed to load bad words");

            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Check sentence for bad words")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Check Sentence", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CheckSentenceResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Failed to check sentence", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authorized", content = @Content)})

    @PostMapping("/checksentence")
    public ResponseEntity<?> checkSentence(@RequestHeader(name = "apiKey", required = false) String apiKey, @RequestBody CheckSentenceRequest checkSentenceRequest) {

        if (apiKey == null || !apiKey.equals(API_KEY)) {
            return new ResponseEntity("Invalid apiKey", HttpStatus.UNAUTHORIZED);
        }


        try {
            Set<String> matches = badWordService.checkSentence(checkSentenceRequest.getSentence());

            if (matches != null && matches.size() > 0) {
                CheckSentenceResponse checkSentenceResponse = new CheckSentenceResponse();
                checkSentenceResponse.setStatus("Failed");
                checkSentenceResponse.setMessages("Bad words found");
                checkSentenceResponse.setCount(matches.size());
                checkSentenceResponse.setBadWords(matches);

                return new ResponseEntity(checkSentenceResponse, HttpStatus.OK);

            } else {
                CheckSentenceResponse checkSentenceResponse = new CheckSentenceResponse();
                checkSentenceResponse.setStatus("Success");
                checkSentenceResponse.setMessages("No bad words found");
                checkSentenceResponse.setCount(0);
                checkSentenceResponse.setBadWords(new HashSet<String>());

                return new ResponseEntity(checkSentenceResponse, HttpStatus.OK);
            }
        }
        catch (CheckSentenceException e) {
            CheckSentenceResponse checkSentenceResponse = new CheckSentenceResponse();
            checkSentenceResponse.setStatus("Failed");
            checkSentenceResponse.setMessages("Failed to check sentence");
            checkSentenceResponse.setCount(0);
            checkSentenceResponse.setBadWords(new HashSet<String>());

            return new ResponseEntity(checkSentenceResponse, HttpStatus.BAD_REQUEST);

        }

    }

}
