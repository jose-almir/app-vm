package eventy.domain.enums;

public enum Role {
    ROOT,
    ADMIN,
    COMMON;

    public String getDetail() {
        return "ROLE_" + this.name();
    }
}
