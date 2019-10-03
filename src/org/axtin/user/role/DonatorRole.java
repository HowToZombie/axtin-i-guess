package org.axtin.user.role;

public enum DonatorRole {
    NONE(0, null, null);

    private int identifier;
    private String registry;
    private String display;

    DonatorRole(int identifier, String registry, String display) {
        this.identifier = identifier;
        this.registry = registry;
        this.display = display;
    }

    public static DonatorRole getRole(String registry) {
        for (DonatorRole role : DonatorRole.values()) {
            if (role.getRegistry().equalsIgnoreCase(registry)) {
                return role;
            }
        }

        return DonatorRole.NONE;
    }

    public static DonatorRole getRole(int identifier) {
        for (DonatorRole role : DonatorRole.values()) {
            if (role.getIdentifier() == identifier) {
                return role;
            }
        }

        return DonatorRole.NONE;
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
}
