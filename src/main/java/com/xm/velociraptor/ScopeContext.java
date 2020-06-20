package com.xm.velociraptor;

import java.util.HashMap;
import java.util.Map;

public class ScopeContext {
    private Map<String, VelociraptorValue> nameToValue = new HashMap<String, VelociraptorValue>();

    public void put(String str, VelociraptorValue v) {
        nameToValue.put(str, v);
    }

    public VelociraptorValue get(String str) {
        return nameToValue.get(str);
    }

    public boolean contains(String str) {
        return nameToValue.containsKey(str);
    }
}
