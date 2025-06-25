package org.aqilea.threadpilot.insuranceservice.model;

public enum InsuranceType {
    CAR_INSURANCE("Car insurance"),
    PERSONAL_HEALTH_INSURANCE("Personal health insurance"),
    PET_INSURANCE("Pet insurance");

    private final String displayName;

    InsuranceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static InsuranceType fromDisplayName(String displayName) {
        for (InsuranceType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown insurance type: " + displayName);
    }
}
