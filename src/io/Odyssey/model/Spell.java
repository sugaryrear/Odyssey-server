package io.Odyssey.model;

import java.util.Arrays;

import io.Odyssey.util.Misc;

public enum Spell {

    // Modern
    WIND_STRIKE(7038,1152, SpellBook.MODERN, true),
    WATER_STRIKE(7039,1154, SpellBook.MODERN, true),
    EARTH_STRIKE(7040,1156, SpellBook.MODERN, true),
    FIRE_STRIKE(7041,1158, SpellBook.MODERN, true),
    WIND_BOLT(7042,1160, SpellBook.MODERN, true),
    WATER_BOLT(7043,1163, SpellBook.MODERN, true),
    EARTH_BOLT(7044,1166, SpellBook.MODERN, true),
    FIRE_BOLT(7045,1169, SpellBook.MODERN, true),
    WIND_BLAST(7046,1172, SpellBook.MODERN, true),
    WATER_BAST(7047,1175, SpellBook.MODERN, true),
    EARTH_BLAST(7048,1177, SpellBook.MODERN, true),
    FIRE_BLAST(7049,1181, SpellBook.MODERN, true),
    WIND_WAVE(7050,1183, SpellBook.MODERN, true),
    WATER_WAVE(7051,1185, SpellBook.MODERN, true),
    EARTH_WAVE(7052,1188, SpellBook.MODERN, true),
    FIRE_WAVE(7053,1189, SpellBook.MODERN, true),
    WIND_SURGE(167236,22644 + 975, SpellBook.MODERN, true),
    WATER_SURGE(167237,22658 + 975, SpellBook.MODERN, true),
    EARTH_SURGE(167238,22628 + 975, SpellBook.MODERN, true),
    FIRE_SURGE(167239, 22608 + 975, SpellBook.MODERN, true),

//
//    SLAYER_DART(12037, SpellBook.MODERN, true),
//    IBAN_BLAST(1539, SpellBook.MODERN, true),
//    CRUMBLE_UNDEAD(1171, SpellBook.MODERN, true),
//    FLAMES_OF_ZAMORAK(1192, SpellBook.MODERN, true),
//    CLAWS_OF_GUTHIX(1191, SpellBook.MODERN, true),
//    SARADOMIN_STRIKE(1190, SpellBook.MODERN, true),
//
    // Ancient
    SMOKE_RUSH(51133,12939, SpellBook.ANCIENT, true),
    SHADOW_RUSH(51185,12987, SpellBook.ANCIENT, true),
    BLOOD_RUSH(51091,12901, SpellBook.ANCIENT, true),
    ICE_RUSH(24018,12861, SpellBook.ANCIENT, true),
    SMOKE_BURST(51159,12963, SpellBook.ANCIENT, true),
    SHADOW_BURST(51211,13011, SpellBook.ANCIENT, true),
    BLOOD_BURST(51111,12919, SpellBook.ANCIENT, true),
    ICE_BURST(51069,12881, SpellBook.ANCIENT, true),
    SMOKE_BLITZ(51146,12951, SpellBook.ANCIENT, true),
    SHADOW_BLITZ(51192,12999, SpellBook.ANCIENT, true),
    BLOOD_BLITZ(51102,12911, SpellBook.ANCIENT, true),
    ICE_BLITZ(51058,12871, SpellBook.ANCIENT, true),
    SMOKE_BARRAGE(51172,12975, SpellBook.ANCIENT, true),
    SHADOW_BARRAGE(51224,13023, SpellBook.ANCIENT, true),
    BLOOD_BARRAGE(51122,12929, SpellBook.ANCIENT, true),
    ICE_BARRAGE(51080,12891, SpellBook.ANCIENT, true),
    ;

    private final int id;
    private final int actionbuttonid;
    private final SpellBook spellBook;
    private final boolean autocastable;

    Spell( int actionbuttonid,int id, SpellBook spellBook, boolean autocastable) {
        this.id = id;
        this.actionbuttonid = actionbuttonid;
        this.spellBook = spellBook;
        this.autocastable = autocastable;
    }

    Spell(int id, SpellBook spellBook) {
        this.id = id;
        this.spellBook = spellBook;
        this.actionbuttonid = -1;
        autocastable = false;
    }

    @Override
    public String toString() {
        return Misc.formatPlayerName(name().toLowerCase().replaceAll("_", " "));
    }

    public int getId() {
        return id;
    }
    public int getactionbuttonid() {
        return actionbuttonid;
    }
    public SpellBook getSpellBook() {
        return spellBook;
    }

    public boolean isAutocastable() {
        return autocastable;
    }

    public static Spell forId(int id) {
        return Arrays.stream(Spell.values()).filter(spell -> spell.id == id).findAny().orElse(null);
    }
    public static Spell foractionbuttonId(int id) {
        return Arrays.stream(Spell.values()).filter(spell -> spell.actionbuttonid == id).findAny().orElse(null);
    }
}
