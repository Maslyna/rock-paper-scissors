package net.roshambo.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Move {
    NONE((short) -1),
    ROCK((short) 0),
    PAPER((short) 1),
    SCISSORS((short) 2);

    public final short value;

    public static Move valueOf(short value) {
        return switch (value) {
            case -1 -> NONE;
            case 0 -> ROCK;
            case 1 -> PAPER;
            case 2 -> SCISSORS;
            default -> null;
        };
    }
}
