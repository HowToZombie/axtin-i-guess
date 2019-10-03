package org.axtin.user.role;

public enum StaffRole {
    /**
     * Volunteer: 1-20
     * IT: 21-40
     * Legal: 41-60
     * Administration: 61-80
     * Leadership: 81-100
     */
    NONE(0, "member", "Member", "&7"),

    // Volunteer
    ARCHITECT(1, "architect", "Architect", "&4"),
    ARCHITECT2(2, "architect2", "Architect", "&d"),
    MODERATOR(3, "moderator", "Moderator", "&9"),
    MODERATOR2(4, "moderator2", "Moderator", "&3"),

    // IT
    PROGRAMMER(21, "programmer", "Admin", "&1"),
    SYSTEM_ADMINISTRATOR(22, "systemadministrator", "Admin", "&5"),

    // Legal
    COMPLIANCE(41, "compliance", "Admin", "&c"),

    // Administration
    COORDINATOR(61, "coordinator", "Admin", "&c"),

    // Leadership
    DIRECTOR(81, "director", "Admin", "&c");

    private int identifier;
    private String registry;
    private String display;
    private String color;

    StaffRole(int identifier, String registry, String display, String color) {
        this.identifier = identifier;
        this.registry = registry;
        this.display = display;
        this.color = color;
    }

    public static StaffRole getRole(String registry) {
        for (StaffRole role : StaffRole.values()) {
            if (role.getRegistry().equalsIgnoreCase(registry)) {
                return role;
            }
        }

        return StaffRole.NONE;
    }

    public static StaffRole getRole(int identifier) {
        for (StaffRole role : StaffRole.values()) {
            if (role.getIdentifier() == identifier) {
                return role;
            }
        }

        return StaffRole.NONE;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public String getRegistry() {
        return this.registry;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getColor() {
        return this.color;
    }
}
