package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.file.input.AbstractLineParsingListener;
import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.type1.batch.input.model.GroupHeader;
import com.javathinking.sample2.type1.batch.input.model.TransactionLine;
import com.javathinking.sample2.type1.transaction.Transaction;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Date: 12/03/2014
 */
public class PersistenceLineParsingListener extends AbstractLineParsingListener {
    @Autowired
    private TransactionRepository transactionRepository;

    public PersistenceLineParsingListener() {
        super(TransactionLine.class);
    }

    @Override
    public void handleObject(Object object, FileFormatContext context) {
        TransactionLine tx = (TransactionLine) object;
        GroupHeader gh = (GroupHeader) context.getState(GroupHeader.class);
        transactionRepository.save(
                new Transaction(context.getFileRef(), gh.getClient(), tx.getAccount(), tx.getDate(), tx.getAmount())
        );
    }

}
