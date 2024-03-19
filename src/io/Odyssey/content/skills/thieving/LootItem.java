package io.Odyssey.content.skills.thieving;

import com.google.gson.annotations.Expose;
import io.Odyssey.content.DiceHandler;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.util.Misc;

/**
 * @author PVE
 * @Since augustus 29, 2020
 */
public class LootItem {

    @Expose public final int id;

    @Expose public final int min, max;

    @Expose public final int weight;

    public LootItem(int id, int amount, int weight) {
        this.id = id;
        this.min = amount;
        this.max = amount;
        this.weight = weight;
    }

    public LootItem(int id, int minAmount, int maxAmount, int weight) {
        this.id = id;
        this.min = minAmount;
        this.max = maxAmount;
        this.weight = weight;
    }

    public GameItem toItem() {
        GameItem item = new GameItem(id, min == max ? min : Misc.random(min, max));
        return item;
    }

    public String getName() {

       String name = ItemAssistant.getItemName(id);
//        ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, id);
//        if(def.isNote())
//            return World.getWorld().definitions().get(ItemDefinition.class, def.notelink).name + " (noted)";
        return name;
    }

}
