package io.Odyssey.model.cycleevent.impl;


import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;



/**
 *
 * things that have to do with the al-kharid desert
 * socklol
 *
 */
public class DesertEvent extends Event<Player> {


    public enum Waterskins {
        WS1(0, 1829, 1831),
        WS2(1, 1827, 1829),
        WS3(2, 1825, 1827),
        WS4(3, 1823, 1825);


        private final int priority;
        private final int itemId;
        private final int replaceWith;

        public int getItemId() {
            return itemId;
        }
        public int getReplacement() {
            return replaceWith;
        }
        public int getPriority() {
            return priority;
        }
        private Waterskins(int priority, int itemId, int replacementId) {
            this.priority = priority;
            this.itemId = itemId;
            this.replaceWith = replacementId;

        }

    }
    private static final Set<Waterskins> WATERSKINS = Collections.unmodifiableSet(EnumSet.allOf(DesertEvent.Waterskins.class));


    public static Waterskins getBestWaterskin(Player player) {
        Waterskins ws = null;
        for (Waterskins waterskin : WATERSKINS) {
            if (player.getItems().playerHasItem(waterskin.itemId)) {
                if (ws == null || waterskin.priority > ws.priority) {
                    ws = waterskin;

                }
            }
        }
        return ws;
    }

    public static final int[][] FILLS = { {1825, 1823 }, {1827, 1825 },
            {1829, 1827 }, {1831, 1829 } ,};

    public static final Boundary DESERT = new Boundary(3120,2760,3500,3109, 0);

    public DesertEvent(String signature, Player attachment, int ticks) {
        super(signature, attachment, ticks);
    }



    @Override
    public void execute() {

        if (attachment == null) {
            super.stop();
            return;
        }
        if (!Boundary.isIn(attachment, DESERT)) {
            CycleEventHandler.getSingleton().stopEvents(this);
            super.stop();
            return;
        }

        Waterskins ws = getBestWaterskin(attachment);
        if (ws == null) {
            attachment.appendDamage(5, Hitmark.HIT);
            attachment.sendMessage("The heat of the desert takes a toll.");
            return;
        }

        attachment.getItems().deleteItem2(ws.getItemId(), 1);
        attachment.getItems().addItem(ws.getReplacement(), 1);
        attachment.sendMessage("You drink from your waterskin.");

    }


    @Override
    public void stop() {
        super.stop();
        if (attachment == null) {
            return;
        }
    }

}