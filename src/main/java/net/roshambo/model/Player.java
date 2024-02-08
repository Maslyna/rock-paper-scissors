package net.roshambo.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Player {
    A((short) 0), B((short) 1);

    public final Short value;

    public static Player valueOf(Short value) {
        return switch (value) {
            case 0 -> A;
            case 1 -> B;
            default -> null;
        };
    }
}
