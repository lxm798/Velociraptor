package com.xm.velociraptor;

import com.xm.velociraptor.enums.TypeEnums;

public class VelociraptorInteger extends VelociraptorValue<Integer> {
    public VelociraptorInteger() {
        super(TypeEnums.Integer);
    }

    @Override
    protected void assertTypeCompatible(TypeEnums type) {
        if (TypeEnums.Float != type && TypeEnums.Integer != type && TypeEnums.String != type) {
            throw new RuntimeException();
        }
    }


    public VelociraptorValue sub(VelociraptorValue v) {
        assertTypeCompatible(v.getType());
        if (v.getType() == TypeEnums.Integer) {
            VelociraptorInteger ret = new VelociraptorInteger();
            ret.setValue(getValue() - (Integer) v.getValue());
            return ret;
        } else if (v.getType() == TypeEnums.Float){
            VelociraptorFloat ret = new VelociraptorFloat();
            ret.setValue(getValue() - (Float) v.getValue());
            return ret;
        }
        return null;
    }
    public VelociraptorValue mod(VelociraptorValue v) {
        VelociraptorInteger right = (VelociraptorInteger)v;
        VelociraptorInteger ret = new VelociraptorInteger();
        ret.setValue(getValue().intValue() % right.getValue().intValue());
        return ret;
    }
    public VelociraptorValue add(VelociraptorValue v) {
        assertTypeCompatible(v.getType());

        if (v.getType() == TypeEnums.Integer) {
            VelociraptorInteger ret = new VelociraptorInteger();
            ret.setValue(getValue() + (Integer) v.getValue());
            return ret;
        } else if (v.getType() == TypeEnums.Float){
            VelociraptorFloat ret = new VelociraptorFloat();
            ret.setValue(getValue() + (Float) v.getValue());
            return ret;
        }
        return null;
    }

    public VelociraptorValue multi(VelociraptorValue v) {
        assertTypeCompatible(v.getType());

        if (v.getType() == TypeEnums.Integer) {
            VelociraptorInteger ret = new VelociraptorInteger();
            ret.setValue(getValue() * (Integer) v.getValue());
            return ret;
        } else if (v.getType() == TypeEnums.Float){
            VelociraptorFloat ret = new VelociraptorFloat();
            ret.setValue(getValue() * (Float) v.getValue());
            return ret;
        }
        return null;
    }

    public VelociraptorValue div(VelociraptorValue v) {
        assertTypeCompatible(v.getType());

        if (v.getType() == TypeEnums.Integer) {
            VelociraptorInteger ret = new VelociraptorInteger();
            ret.setValue(getValue() / (Integer) v.getValue());
            return ret;
        } else if (v.getType() == TypeEnums.Float){
            VelociraptorFloat ret = new VelociraptorFloat();
            ret.setValue(getValue() / (Float) v.getValue());
            return ret;
        }
        return null;
    }
    public VelociraptorValue bitOr(VelociraptorValue v) {
        VelociraptorInteger right = (VelociraptorInteger)v;
        VelociraptorInteger ret = new VelociraptorInteger();
        ret.setValue(getValue().intValue() | right.getValue().intValue());
        return ret;
    }

    public VelociraptorValue bitAnd(VelociraptorValue v) {
        VelociraptorInteger right = (VelociraptorInteger)v;
        VelociraptorInteger ret = new VelociraptorInteger();
        ret.setValue(getValue().intValue() ^ right.getValue().intValue());
        return ret;
    }
}
