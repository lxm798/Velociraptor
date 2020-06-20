package com.xm.velociraptor;

import com.xm.velociraptor.enums.TypeEnums;

public class VelociraptorFloat   extends VelociraptorValue<Float> {
    protected VelociraptorFloat() {
        super(TypeEnums.Float);
    }

    @Override
    protected void assertTypeCompatible(TypeEnums type) {

    }
}
