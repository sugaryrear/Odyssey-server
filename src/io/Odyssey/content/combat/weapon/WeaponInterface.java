package io.Odyssey.content.combat.weapon;

public enum WeaponInterface {
    SWORD_STAB(29769, 2279, 7574, 7586),
    SWORD_SLASH(29777, 2426, 7599, 7611),
    SWORD_CRUSH(4705, 4708, 7699, 7711),
    STAFF(29767, 355, 12323, 12335),
    MAUL(29775, 428, 7474, 7486),
    BATTLEAXE(29770, 1701, 7499, 7511),
    BOW(29764, 1767, 7549, 7561),
    MACE(29774, 3799, 7624, 7636),
    THROWN_RANGED(29768, 4449, 7649, 7661),
    SPEAR(29773, 4682, 7674, 7686),
    CLAWS(29776, 7765, 7800, 7812),
    HALBERD(29771, 8463, 8493, 8505),
    WHIP(29765, 12293, 12323, 12335),
    STICKS(6103, 6132, -1, -1),
    UNARMED(29766, 5857, -1, -1),
    SCYTHE(29786, 779, -1, -1),
    ;

    private final int interfaceId;
    private final int nameInterfaceId;
    private final int specialBarInterfaceId;
    private final int specialBarAmountInterfaceId;

    WeaponInterface(int interfaceId, int nameInterfaceId, int specialBarInterfaceId, int specialBarAmountInterfaceId) {
        this.interfaceId = interfaceId;
        this.nameInterfaceId = nameInterfaceId;
        this.specialBarInterfaceId = specialBarInterfaceId;
        this.specialBarAmountInterfaceId = specialBarAmountInterfaceId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getNameInterfaceId() {
        return nameInterfaceId;
    }

    public int getSpecialBarInterfaceId() {
        return specialBarInterfaceId;
    }

    public int getSpecialBarAmountInterfaceId() {
        return specialBarAmountInterfaceId;
    }
}
