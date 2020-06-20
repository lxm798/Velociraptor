package com.xm.velociraptor;

import com.xm.velociraptor.enums.TypeEnums;

public abstract class VelociraptorValue<V> {
    private V value;
    private TypeEnums type;

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    private boolean isReturn;

    protected VelociraptorValue(TypeEnums type) {
        this.type = type;
    }

    public void setValue(V value) {
        this.value = value;
    }


    public V getValue() {
        return value;
    }

    public TypeEnums getType() {
        return type;
    }

    public VelociraptorValue sub(VelociraptorValue v) {
        throw new UnsupportedOperationException("sub");
    }

    public VelociraptorValue add(VelociraptorValue v) {
        throw new UnsupportedOperationException("add");
    }

    public VelociraptorValue multi(VelociraptorValue v) {
        throw new UnsupportedOperationException("multi");
    }

    public VelociraptorValue div(VelociraptorValue v) {
        throw new UnsupportedOperationException("div");
    }
    public VelociraptorValue mod(VelociraptorValue v) {
        throw new UnsupportedOperationException("mod");
    }
    public VelociraptorValue not() {
        throw new UnsupportedOperationException("not");
    }

    public VelociraptorValue and(VelociraptorValue v) {
        throw new UnsupportedOperationException("and");
    }

    public VelociraptorValue or(VelociraptorValue v) {
        throw new UnsupportedOperationException("or");
    }

    public VelociraptorValue bitOr(VelociraptorValue v) {
        throw new UnsupportedOperationException("bitOr");
    }

    public VelociraptorValue bitAnd(VelociraptorValue v) {
        throw new UnsupportedOperationException("bitAnd");
    }
    public VelociraptorValue equals(VelociraptorValue v) {
        VelociraptorBool velociraptorBool = new VelociraptorBool();
        velociraptorBool.setValue(getValue().equals(v.getValue()));
        return velociraptorBool;
    }

    public String toString() {
        return value.toString();
    }

    protected abstract void assertTypeCompatible(TypeEnums type);
}
