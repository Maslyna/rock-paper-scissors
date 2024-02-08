package net.roshambo.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Result {
    TIE((short) -1),
    WIN_A((short) 0),
    WIN_B((short) 1);

    public final Short value;


    public static Result valueOf(Short value) {
        return switch (value) {
            case -1 -> TIE;
            case 0 -> WIN_A;
            case 1 -> WIN_B;
            default -> null;
        };
    }
}
