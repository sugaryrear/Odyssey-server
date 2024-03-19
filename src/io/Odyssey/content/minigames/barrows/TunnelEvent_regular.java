package io.Odyssey.content.minigames.barrows;


import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public class TunnelEvent_regular extends Event<Player> {

    public TunnelEvent_regular(String signature, Player attachment, int ticks) {
        super(signature, attachment, ticks);
    }

    @Override
    public void execute() {
        if (attachment == null) {
            super.stop();
            return;
        }
        if (!Boundary.isIn(attachment, Barrows.TUNNEL)) {
            stop();
            return;
        }

        attachment.getBarrows().drainPrayer();
    }

    @Override
    public void stop() {
        super.stop();
        if (attachment == null) {
            return;
        }
        attachment.getPA().sendFrame107();
    }

}
