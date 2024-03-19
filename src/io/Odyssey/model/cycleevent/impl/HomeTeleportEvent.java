package io.Odyssey.model.cycleevent.impl;


import io.Odyssey.Configuration;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

/**
 *
 * hometeleport
 * socklol
 *
 */
public class HomeTeleportEvent extends Event<Player> {



    public HomeTeleportEvent(String signature, Player attachment, int ticks) {
        super(signature, attachment, ticks);
    }



    @Override
    public void execute() {

        if (attachment == null || attachment.timer == 0) {
            super.stop();
            return;
        }

        attachment.getPA().closeAllWindows();
        attachment.stopMovement();

            if (attachment.timer == 26) {
                attachment.startAnimation(4847);
                attachment.gfx0(800);
            }else if (attachment.timer == 18) {
                // attachment.getPA().stillGfx( 801, attachment.absX, attachment.absY, attachment.heightLevel, 50);
                attachment.getPA().createPlayersStillGfx(801, attachment.absX, attachment.absY, attachment.heightLevel, 50);
                attachment.startAnimation(4850);
            } else if (attachment.timer == 15) {
                attachment.startAnimation(4853);
                attachment.gfx0(802);
            } else if (attachment.timer == 10) {
                attachment.startAnimation(4855);
                attachment.gfx0(803);
            } else if (attachment.timer == 5) {
                attachment.startAnimation(4857);
                attachment.gfx0(804);
            } else if (attachment.timer < 2) {
                attachment.getPA().movePlayer(Configuration.START_LOCATION_X, Configuration.START_LOCATION_Y, 0);
                super.stop();

                attachment.homeTeleportLength = (int) Configuration.HOMETELE_LENGTH;
                attachment.homeTeleportDelay = System.currentTimeMillis();

            }

        attachment.timer--;

    }


    @Override
    public void stop() {
        super.stop();

        if (attachment == null) {
            return;
        }
    }

}
