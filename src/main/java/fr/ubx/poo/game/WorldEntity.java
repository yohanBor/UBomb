/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.util.Arrays;
import java.util.Optional;

// ENUMERE TOUTES LES POSSIBILITE D'UNE CASE
public enum WorldEntity {
    Empty('_'),
    Box('B'),
    Heart('H'),
    Key('K'),
    Teleporteur('|'),
    Lava('L'),
    Monster('M'),
    DoorPrevOpened('V'),
    DoorNextOpened('N'),
    DoorNextClosed('n'),
    Player('P'),
    Stone('S'),
    Tree('T'),
    Princess('W'),
    BombRangeInc('>'),
    BombRangeDec('<'),
    BombNumberInc('+'),
    BombNumberDec('-'),
    Chest_opened('Q'),
    Chest_closed('q'),
        ;

    private final char code;

    WorldEntity(char code) {
        this.code = code;
    }

    public static Optional<WorldEntity> fromCode(char code) {
        return Arrays.stream(values())
                .filter(e->e.acceptCode(code))
                .findFirst();
    }

    private boolean acceptCode(char code) {
        return this.code == code;
    }

    @Override
    public String toString() {
        return ""+code;
    }
}
