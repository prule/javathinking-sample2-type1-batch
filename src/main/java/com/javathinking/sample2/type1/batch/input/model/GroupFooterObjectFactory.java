package com.javathinking.sample2.type1.batch.input.model;

import com.javathinking.sample2.common.file.input.custom.ObjectFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GroupFooterObjectFactory implements ObjectFactory<GroupFooter> {

    public static final String COUNT = "count";

    @Override
    public GroupFooter fromMap(Map map) {
        return new GroupFooter((BigDecimal) map.get(COUNT));
    }

    @Override
    public Map<String, Object> toMap(GroupFooter object) {
        Map<String, Object> map = new HashMap();
        map.put(COUNT, object.getCount());
        return map;
    }


}
