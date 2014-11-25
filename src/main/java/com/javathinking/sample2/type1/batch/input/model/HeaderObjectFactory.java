package com.javathinking.sample2.type1.batch.input.model;

import com.javathinking.sample2.common.file.input.custom.ObjectFactory;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class HeaderObjectFactory implements ObjectFactory<Header> {

    public static final String DATE = "date";

    @Override
    public Header fromMap(Map map) {
        return new Header((DateTime) map.get(DATE));
    }

    @Override
    public Map<String, Object> toMap(Header object) {
        Map<String, Object> map = new HashMap();
        map.put(DATE, object.getDate());
        return map;
    }


}
