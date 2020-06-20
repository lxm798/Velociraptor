package com.xm.velociraptor.enums;

public enum TypeEnums {
    Integer(0),
    Float(1),
    String(2),
    Function(3),
    Boolean(4),
    Null(5),
    ;

    private Integer type;

    TypeEnums(Integer type) {
        this.type = type;
    }
}
