package com.javathinking.sample2.type1.batch.input.model;

import org.joda.time.DateTime;

/**
 * Date: 7/03/2014
 */
public class Header {
    private DateTime date;

    public Header() {
    }

    public Header(DateTime date) {
        this.date = date;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
