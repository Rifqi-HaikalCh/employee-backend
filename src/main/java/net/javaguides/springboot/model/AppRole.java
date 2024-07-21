package net.javaguides.springboot.model;

public enum AppRole {
    USER("User"),
    SUPER_ADMIN("Super Admin"),
    STAFF_ADMIN("Staff Admin"),
    CONTROL_ADMIN("Control Admin");

    private final String displayName;

    AppRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Method untuk mendapatkan AppRole dari String
    public static AppRole fromString(String text) {
        for (AppRole role : AppRole.values()) {
            if (role.displayName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
