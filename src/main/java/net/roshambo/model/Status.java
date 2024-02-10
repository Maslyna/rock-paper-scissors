package net.roshambo.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    ACTIVE((short) -1),
    TIE((short) 0),
    WIN_A((short) 1),
    WIN_B((short) 2);

    public final Short value;


    public static Status valueOf(Short value) {
        return switch (value) {
            case -1 -> ACTIVE;
            case 0  -> TIE;
            case 1  -> WIN_A;
            case 2  -> WIN_B;
            default -> null;
        };
    }
}
