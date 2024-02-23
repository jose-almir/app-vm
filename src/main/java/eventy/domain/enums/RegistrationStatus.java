package eventy.domain.enums;

public enum RegistrationStatus {
    REJECTED(1, "Rejected"),
    CANCELLED(2, "Cancelled"),
    PENDING(3, "Pending"),
    WAITLIST(4, "Waitlist"),
    CONFIRMED(5, "Confirmed");

    private final Integer value;
    private final String label;

    RegistrationStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

    public static RegistrationStatus fromValue(int value) {
        for (RegistrationStatus status : RegistrationStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid RegistrationStatus value: " + value);
    }
}