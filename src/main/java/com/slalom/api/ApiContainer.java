package com.slalom.api;

/**
 * Represents container for an API response which contains content and an id.
 */
public class ApiContainer {
    private final long id;
    private final String content;

    public ApiContainer(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
