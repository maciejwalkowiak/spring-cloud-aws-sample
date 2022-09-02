package com.example.springcloudawssample;

import java.util.UUID;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Tweet {
    private UUID id;
    private String author;
    private String content;

    public Tweet(String author, String content) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.content = content;
    }

    public Tweet() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
