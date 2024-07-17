package net.javaguides.springboot.model;

public enum Role {
    USER("User"),
    SUPER_ADMIN("Super Admin"),
    STAFF_ADMIN("Staff Admin"),
    CONTROL_ADMIN("Control Admin");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Metode untuk mendapatkan Role dari String
    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.displayName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
