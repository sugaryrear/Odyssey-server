package io.Odyssey.content.skills.smithing;

import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.types.MakeItemDialogue;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Items;
import io.Odyssey.model.SkillLevel;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.model.tickable.impl.ItemProductionTickableBuilder;

public class CannonballSmelting {

    public static boolean isSmeltingCannonballs(Player player) {
        return player.getItems().playerHasItem(Items.AMMO_MOULD);
    }

    public static void smelt(Player player) {
        player.start(new DialogueBuilder(player).makeItems(
                100, CannonballSmelting::make,
                new MakeItemDialogue.MakeItem(Items.CANNONBALL)
        ));
    }

    private static void make(MakeItemDialogue.PlayerMakeItem makeItem) {
        new ItemProductionTickableBuilder()
                .setPlayer(makeItem.getPlayer())
                .setProductionDelay(3)
                .setProductionAmount(makeItem.getAmount())
                .setExecutionConsumer(task -> task.getPlayer().startAnimation(899))
                .setItemsConsumed(new ImmutableItem(Items.STEEL_BAR))
                .setItemsProduced(new ImmutableItem(Items.CANNONBALL, 4))
                .setExperiencedGained(new SkillLevel(Skill.SMITHING, 25))
                .createItemProductionTask().begin();
    }

}
