package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RevenantEtherDeathItem implements DeathItemModifier {

    private static final Set<Integer> CRAWS = Set.of(Items.CRAWS_BOW);
    private static final Set<Integer> VIGGORAS = Set.of(Items.VIGGORAS_CHAINMACE);
    private static final Set<Integer> THAMMARONS = Set.of(Items.THAMMARONS_SCEPTRE);
    private static final Set<Integer> WEBWEAVER = Set.of(Items.WEBWEAVER_BOW);
    private static final Set<Integer> URASINE = Set.of(Items.URASINE_CHAINMACE);
    private static final Set<Integer> ACCURSED = Set.of(Items.ACCURSED_SCEPTRE);
    private static final Set<Integer> BRACELET = Set.of(Items.BRACELET_OF_ETHEREUM);
    private static final Set<Integer> ALL;

    static {
        ALL = new HashSet<>();
        ALL.addAll(CRAWS);
        ALL.addAll(VIGGORAS);
        ALL.addAll(THAMMARONS);
        ALL.addAll(WEBWEAVER);
        ALL.addAll(URASINE);
        ALL.addAll(ACCURSED);
        ALL.addAll(BRACELET);
    }

    @Override
    public Set<Integer> getItemIds() {
        return ALL;
    }

    @Override
    public void modify(Player player, GameItem gameItem, boolean kept, List<GameItem> keptItems, List<GameItem> lostItems) {
        if (kept)
            return;

        lostItems.remove(gameItem);
        int id = gameItem.getId();
        int charge = 0;

        if (CRAWS.contains(id)) {
            player.getPvpWeapons().setCrawsBowCharges(0);
            lostItems.add(new GameItem(Items.CRAWS_BOW_U));
        } else if (VIGGORAS.contains(id)) {
            player.getPvpWeapons().setViggoraChainmaceCharges(0);
            lostItems.add(new GameItem(Items.VIGGORAS_CHAINMACE_U));
        } else if (THAMMARONS.contains(id)) {
            player.getPvpWeapons().setThammaronSceptreCharges(0);
            lostItems.add(new GameItem(Items.THAMMARONS_SCEPTRE_U));
        } else if (URASINE.contains(id)) {
            player.getPvpWeapons().setUrsineChainmaceCharges(0);
            lostItems.add(new GameItem(Items.URASINE_CHAINMACE_U));
        } else if (WEBWEAVER.contains(id)) {
            player.getPvpWeapons().setWebweaverBowCharges(0);
            lostItems.add(new GameItem(Items.WEBWEAVER_BOW_U));
        } else if (BRACELET.contains(id)) {
            charge = player.braceletEtherCount;
            player.braceletEtherCount = 0;
            lostItems.add(new GameItem(Items.BRACELET_OF_ETHEREUM_UNCHARGED));
        }

        if (charge > 0)
            lostItems.add(new GameItem(Items.REVENANT_ETHER, charge));
    }
}
