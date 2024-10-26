package com.sms.PaymentsService.enums;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant found for status: " + status);
    }
}
