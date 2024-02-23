package eventy.domain.enums;

public enum EventStatus {
    CANCELLED(1, "Cancelled"),
    PENDING(2, "Pending"),
    IN_PROGRESS(3, "In Progress"),
    FINISHED(4, "Finished");

    private final Integer value;
    private final String label;

    EventStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static EventStatus fromValue(int value) {
        for (EventStatus status : EventStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid EventStatus value: " + value);
    }
}
