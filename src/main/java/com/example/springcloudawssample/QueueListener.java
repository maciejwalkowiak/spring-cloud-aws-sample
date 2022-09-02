package com.example.springcloudawssample;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
class QueueListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);
    private final DynamoDbTemplate dynamoDbTemplate;

    QueueListener(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @SqsListener(queueNames = "test-queue")
    void handle(TweetDto tweet) {
        LOGGER.info("Received tweet dto: {}", tweet);
        dynamoDbTemplate.save(new Tweet(tweet.author(), tweet.content()));
    }
}
