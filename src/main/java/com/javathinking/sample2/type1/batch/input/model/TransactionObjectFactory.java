package com.javathinking.sample2.type1.batch.input.model;

import com.javathinking.sample2.common.file.input.custom.ObjectFactory;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionObjectFactory implements ObjectFactory<TransactionLine> {

    public static final String DATE = "date";
    public static final String AMOUNT = "amount";
    public static final String ACCOUNT = "account";

    @Override
    public TransactionLine fromMap(Map map) {
        return new TransactionLine((String) map.get(ACCOUNT), (DateTime) map.get(DATE), (BigDecimal) map.get(AMOUNT));
    }

    @Override
    public Map<String, Object> toMap(TransactionLine object) {
        Map<String, Object> map = new HashMap();
        map.put(ACCOUNT, object.getAccount());
        map.put(DATE, object.getDate());
        map.put(AMOUNT, object.getAmount());
        return map;
    }


}
