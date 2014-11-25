package com.javathinking.sample2.type1.batch.input.model;

import java.math.BigDecimal;

/**
 * Date: 7/03/2014
 */
public class GroupFooter {
    private BigDecimal count;

    public GroupFooter() {
    }

    public GroupFooter(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}
