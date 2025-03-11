package com.healthcare.management.entity;


public enum Role {
    DOCTOR(0),
    PATIENT(1);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
