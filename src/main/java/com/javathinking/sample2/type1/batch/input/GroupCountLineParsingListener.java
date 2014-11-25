package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.file.input.AbstractLineParsingListener;
import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.type1.batch.input.model.GroupFooter;
import com.javathinking.sample2.type1.batch.input.model.TransactionLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 12/03/2014
 */
public class GroupCountLineParsingListener extends AbstractLineParsingListener {
    private static final Logger log = LoggerFactory.getLogger(GroupCountLineParsingListener.class);
    private int count = 0;

    public GroupCountLineParsingListener() {
        super(TransactionLine.class, GroupFooter.class);
    }

    @Override
    public void handleObject(Object object, FileFormatContext context) {
        if (object instanceof TransactionLine) {
            count++;
        }
        if (object instanceof GroupFooter) {
            GroupFooter groupFooter = (GroupFooter) object;
            if (groupFooter.getCount().intValue() != count) {
                context.error("Group footer count of " + count + " does not match expected " + groupFooter.getCount());
            } else {
                log.debug("Group footer count passed");
            }
            count = 0;
        }
    }
}
