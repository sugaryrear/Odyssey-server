package io.Odyssey.content.boosts;

import com.google.common.collect.Lists;
import io.Odyssey.model.entity.player.Player;

import java.util.List;

public class XPBoostInformation {

    public static void openInformationInterface(Player player) {
        List<String> lines = Lists.newArrayList();
        List<? extends Booster<?>> boosts = Boosts.getBoostsOfType(player, null, BoostType.EXPERIENCE);
        if (!boosts.isEmpty()) {
            lines.add("<col=00c0ff> " + boosts.get(0).getDescription());
        }

        boosts = Boosts.getBoostsOfType(player, null, BoostType.GENERIC);
        for (Booster<?> boost : boosts) {
            lines.add("1 <col=00c0ff> " + boost.getDescription());
        }

        player.getPA().openQuestInterface("Active Boosts", lines);
    }

}
