package com.xm.velociraptor;

import com.xm.velociraptor.enums.TypeEnums;

public class VelociraptorBool  extends VelociraptorValue<Boolean> {
    public VelociraptorBool(Boolean v) {
        super(TypeEnums.Boolean);
        setValue(v);
    }

    public VelociraptorBool() {
        super(TypeEnums.Boolean);
        setValue(false);
    }

    @Override
    public VelociraptorValue not() {
        VelociraptorBool velociraptorBool = new VelociraptorBool();
        velociraptorBool.setValue(!getValue());
        return velociraptorBool;
    }

    public VelociraptorBool and(VelociraptorValue v) {
        assertTypeCompatible(v.getType());

        VelociraptorBool velociraptorBool = new VelociraptorBool();
        velociraptorBool.setValue(getValue() && (Boolean) v.getValue());
        return velociraptorBool;
    }

    public VelociraptorBool or(VelociraptorValue v) {
        assertTypeCompatible(v.getType());

        VelociraptorBool velociraptorBool = new VelociraptorBool();
        velociraptorBool.setValue(getValue() || (Boolean) v.getValue());
        return velociraptorBool;    }

    @Override
    protected void assertTypeCompatible(TypeEnums type) {
        if (TypeEnums.Boolean != type) {
            throw new RuntimeException();
        }
    }
}
