package io.Odyssey.model;

import io.Odyssey.util.Misc;

public enum SpellBook {
    MODERN, ANCIENT, LUNAR, ARCEEUS
    ;

    @Override
    public String toString() {
        return Misc.formatPlayerName(name().toLowerCase());
    }
}
