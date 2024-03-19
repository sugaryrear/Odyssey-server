package io.Odyssey.content.combat.magic.items;
import io.Odyssey.model.entity.player.Player;

public class Lightbearer {

    public static void activate(Player player) {
        player.specRestore = 200;
        player.specAmount = 10.0;
    }
}
