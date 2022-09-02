package com.example.springcloudawssample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCloudAwsSampleApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudAwsSampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAwsSampleApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(SqsAsyncClient sqsAsyncClient, DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return args -> {
            try {
                sqsAsyncClient.createQueue(r -> r.queueName("test-queue")).join();
            } catch (Exception e) {
                LOGGER.error("Error during creating a queue", e);
            }

            try {
                dynamoDbEnhancedClient.table("tweet", TableSchema.fromBean(Tweet.class)).createTable();
            } catch (Exception e) {
                LOGGER.error("Error during creating a DynamoDB table");
            }
        };
    }

}

