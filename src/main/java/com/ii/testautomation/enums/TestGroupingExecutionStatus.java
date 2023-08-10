package com.ii.testautomation.enums;

import lombok.Getter;

@Getter
public enum TestGroupingExecutionStatus {
    TRUE(true), FALSE(false);

    private boolean value;

    private TestGroupingExecutionStatus(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
