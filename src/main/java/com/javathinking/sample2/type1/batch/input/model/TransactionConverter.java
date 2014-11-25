package com.javathinking.sample2.type1.batch.input.model;

import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.common.file.input.FileFormatConverter;
import com.javathinking.sample2.type1.transaction.Transaction;

/**
 * Date: 28/03/2014
 */
public class TransactionConverter implements FileFormatConverter<Transaction, TransactionLine> {
    @Override
    public Transaction convert(TransactionLine txnLine, FileFormatContext context) {
        GroupHeader gh = (GroupHeader) context.getState(GroupHeader.class);
        return (new Transaction(context.getFileRef(), gh!=null?gh.getClient():null, txnLine.getAccount(), txnLine.getDate(), txnLine.getAmount()));
    }
}
