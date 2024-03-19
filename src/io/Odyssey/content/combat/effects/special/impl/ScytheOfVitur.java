package io.Odyssey.content.combat.effects.special.impl;

import io.Odyssey.Server;
import io.Odyssey.content.combat.effects.special.SpecialEffect;
import io.Odyssey.model.Direction;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.Items;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

/**
 * @author Arthur Behesnilian 9:11 PM
 */
public class ScytheOfVitur implements SpecialEffect {

    public static ScytheOfVitur SCYTHE_EFFECT = new ScytheOfVitur();

    public static boolean usingScythe(Player player) {
        return player.getItems().isWearingItem(Items.SCYTHE_OF_VITUR, Player.playerWeapon);
    }

    @Override
    public boolean activateSpecialEffect(Player player, Object... args) {
        boolean usingScythe = player.getItems().isWearingItem(Items.SCYTHE_OF_VITUR, Player.playerWeapon);

        Entity defender = (Entity) args[0];

        if (usingScythe) {
            Position defenderAdjacentPosition = defender.getAdjacentBorderPosition(player.getPosition());
            Position directional = player.getPosition().toDirectional(defenderAdjacentPosition);
            Direction direction = Direction.fromDirectional(directional);
            Position position = player.getPosition().translate(direction);
            int gfx = direction == Direction.NORTH ? 506
                    : direction == Direction.EAST ? 1172
                    : direction == Direction.SOUTH ? 478
                    : 1231;

            Server.playerHandler.sendStillGfx(new StillGraphic(gfx, Graphic.GraphicHeight.HIGH, position), player.getInstance());
        }

        return usingScythe;
    }

}
