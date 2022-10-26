package org.launchcode.codingevents.models;

public enum EventType {

    CONFERENCE("Conference"),
    MEETUP("Meetup"),
    WORKSHOP("Workshop"),
    SOCIAL("Social");

    private final String displayName;

    EventType(String displayname) {
        this.displayName = displayname;
    }

    public String getDisplayName() {
        return displayName;
    }
}
