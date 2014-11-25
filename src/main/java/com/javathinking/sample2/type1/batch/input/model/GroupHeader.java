package com.javathinking.sample2.type1.batch.input.model;

/**
 * Date: 7/03/2014
 */
public class GroupHeader {
    private String client;

    public GroupHeader() {
    }

    public GroupHeader(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
