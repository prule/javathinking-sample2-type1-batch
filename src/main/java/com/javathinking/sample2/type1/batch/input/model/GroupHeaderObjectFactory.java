package com.javathinking.sample2.type1.batch.input.model;


import com.javathinking.sample2.common.file.input.custom.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

public class GroupHeaderObjectFactory implements ObjectFactory<GroupHeader> {

    public static final String CLIENT = "client";

    @Override
    public GroupHeader fromMap(Map map) {
        return new GroupHeader((String) map.get(CLIENT));
    }

    @Override
    public Map<String, Object> toMap(GroupHeader object) {
        Map<String, Object> map = new HashMap();
        map.put(CLIENT, object.getClient());
        return map;
    }


}
