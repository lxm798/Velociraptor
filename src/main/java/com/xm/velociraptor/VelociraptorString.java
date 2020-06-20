package com.xm.velociraptor;

import com.xm.velociraptor.enums.TypeEnums;

public class VelociraptorString  extends VelociraptorValue<String> {
    public VelociraptorString() {
        super(TypeEnums.String);
        setValue("");
    }

    public VelociraptorValue add(VelociraptorValue v) {
        VelociraptorString ret = new VelociraptorString();
        ret.setValue(getValue() + v.getValue());
        return ret;
    }

    @Override
    protected void assertTypeCompatible(TypeEnums type) {
        return;
    }
}
