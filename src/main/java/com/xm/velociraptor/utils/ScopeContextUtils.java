package com.xm.velociraptor.utils;

import com.xm.velociraptor.ScopeContext;
import com.xm.velociraptor.VelociraptorValue;

import java.util.Stack;

public class ScopeContextUtils {
    public static VelociraptorValue getValue(Stack<ScopeContext> scopeContexts, String name) {
        int size = scopeContexts.size();
        for (int i = size -1; i>=0; --i) {
            ScopeContext s = scopeContexts.get(i);
            if (s.contains(name)) {
                return s.get(name);
            }
        }
        return null;
    }

    public static void addValue(Stack<ScopeContext> scopeContexts, String name, VelociraptorValue v) {
        scopeContexts.peek().put(name, v);
    }

}
