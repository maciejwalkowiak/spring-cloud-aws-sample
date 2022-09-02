package com.example.springcloudawssample;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TweetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetController.class);
    private final DynamoDbTemplate dynamoDbTemplate;
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    TweetController(DynamoDbTemplate dynamoDbTemplate, SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.sqsAsyncClient = sqsAsyncClient;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/tweets")
    List<Tweet> tweets() {
        return dynamoDbTemplate.scanAll(Tweet.class).items().stream().toList();
    }

    @PostMapping("/tweets")
    void create(@RequestBody TweetDto tweetDto) {
        LOGGER.info("Creating tweet: {}", tweetDto);
        String s = sqsAsyncClient.getQueueUrl(r -> r.queueName("test-queue")).join().queueUrl();
        sqsAsyncClient.sendMessage(r -> {
            try {
                r.queueUrl(s).messageBody(objectMapper.writeValueAsString(tweetDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
