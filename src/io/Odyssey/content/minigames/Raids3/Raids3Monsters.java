package io.Odyssey.content.minigames.Raids3;

import java.util.Arrays;
import java.util.Optional;

import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public enum Raids3Monsters {

    AKKHA(new Boundary(3667, 5394, 3704, 5423), 11789,11790,11791,11792,11793,11794,11795,11796),
    BABA(new Boundary(3793, 5397, 3829, 5419), 11778),
    KEPHRI(new Boundary(3536, 5395, 3567, 5422), 11719),
    TUMEKENS_WARDEN(new Boundary(3787, 5133, 3830, 5176,1), 11747),
    ZEBAK( new Boundary(3914, 5394, 3953, 5426), 11730),
    ELIDINIS_WARDEN(new Boundary(3787, 5133, 3830, 5176,1), 11746),
    PATH_OF_APMEKEN(new Boundary(3787, 5133, 3830, 5176,1), 11746),
    PATH_OF_HET(new Boundary(3787, 5133, 3830, 5176,1), 11746),
    PATH_OF_CRONDIS(new Boundary(3787, 5133, 3830, 5176,1), 11746),
    PATH_OF_SCABARAS(new Boundary(3787, 5133, 3830, 5176,1), 11746),
    OSMUMTEN(new Boundary(3787, 5133, 3830, 5176,1), 11693);

    private final int[] possibleIds;
    private final Boundary bounds;

    Raids3Monsters(Boundary boundary, int...possibleIds) {
        this.possibleIds = possibleIds;
        this.bounds = boundary;
    }

    public Boundary getBoundary() {
        return bounds;
    }

    public boolean isMatch(int npcId) {
        return Arrays.stream(possibleIds).filter(id -> npcId == id).findFirst().isPresent();
    }

    public int[] getIds() {
        return possibleIds;
    }

    public boolean isInBounds(Player p) {
        return p.getX() >= bounds.getMinimumX()
                && p.getY() >= bounds.getMinimumY()
                && p.getX() <= bounds.getMaximumX()
                && p.getY() <= bounds.getMaximumY();
    }

    public static Raids3Monsters getMonster(Player player) {
        Optional<Raids3Monsters> monster = Arrays.stream(values()).filter(rm -> rm.isInBounds(player)).findFirst();
        return monster.isPresent() ? monster.get() : null;
    }

    public static Raids3Monsters getMonsterById(int npcId) {
        Optional<Raids3Monsters> monster = Arrays.stream(values()).filter(rm -> rm.isMatch(npcId)).findFirst();
        return monster.isPresent() ? monster.get() : null;
    }
}