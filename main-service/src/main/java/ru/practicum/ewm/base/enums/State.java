package ru.practicum.ewm.base.enums;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State from(String state) {
        for (State value: State.values()) {
            if (value.name().equalsIgnoreCase(state)) {
                return value;
            }
        }
        return null;
    }
}
