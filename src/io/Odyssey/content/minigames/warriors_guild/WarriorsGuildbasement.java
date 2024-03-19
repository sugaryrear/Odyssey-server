package io.Odyssey.content.minigames.warriors_guild;

import java.util.Arrays;
import java.util.Optional;

import io.Odyssey.Server;
import io.Odyssey.content.SkillcapePerks;
import io.Odyssey.content.commands.CommandManager;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PathFinder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.lock.CompleteLock;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

/**
 *
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 20, 2013
 */
public class WarriorsGuildbasement {

    public static final Boundary CYCLOPS_BOUNDARY = new Boundary(2903, 9952, 2941, 9976, 0);

    //public static final Boundary[] WAITING_ROOM_BOUNDARY = { new Boundary(2905, 9966, 2911, 3542, 0), new Boundary(2847, 3537, 2847, 3537, 2) };
    public static final Boundary[] WAITING_ROOM_BOUNDARY = { new Boundary(2905, 9966, 2911, 9973, 0) };

    private final Player player;

    private boolean active;

    public static final int[][] DEFENDER_DATA = { { 8850, 35 }, { 12954, 50 } };

    public WarriorsGuildbasement(Player player) {
        this.player = player;

    }

    public void cycle() {
        CycleEventHandler.getSingleton().stopEvents(this);
        setActive(true);
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer event) {
                if (player == null || player.isDisconnected()) {
                    event.stop();
                    return;
                }
                if (!Boundary.isIn(player, CYCLOPS_BOUNDARY) || Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
                    setActive(false);
                    event.stop();
                    return;
                }
                if (SkillcapePerks.ATTACK.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
                    if (player.debugMessage) {
                        player.sendMessage("Has cape. Yeet");
                    }
                } else {
                    if (!player.getItems().playerHasItem(8851, 10)) {
                        removeFromRoom();
                        setActive(false);
                        event.stop();
                        return;
                    }
                }
                if (SkillcapePerks.ATTACK.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
                    //player.sendMessage("Your cape negates the need for warriors guild tokens.", 255);
                } else {
                    player.getItems().deleteItem2(8851, 20);
                    player.sendMessage("You notice some of your warrior guild tokens disappear..", 255);
                }
            }

            @Override
            public void onStopped() {

            }

        }, 200);
    }
    public void closedoorevent() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {

                player.getPA().object(10043, 2911, 9968, 2, 0);
                container.stop();
            }

            @Override
            public void onStopped() {

            }
        }, 3);
    }
    int x = 0;
    int y = 0;
    public void walkthrudoor(boolean entering) {
        if(entering){
//            if(player.absX > player.objectX){
//                 x = -1;
//            }
//            if(player.absY > player.objectY){
//                y = -1;
//            } else {
//                y = 1;
//            }
//         player.getPA().walkTo(x, y);
            PathFinder.getPathFinder().findRouteNoclip(player, 2911, 9968, true, 0, 0);
            //CommandManager.execute(player,"wlkto");
            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (player.getAgilityHandler().hotSpot(player, 2911, 9968)) {
                        player.facePosition(new Position(2912, 9968));
                       player.getPA().object(10043, 2911, 9968, 1, 0);

                        PathFinder.getPathFinder().findRouteNoclip(player, 2912, 9968, true, 0, 0);
                        player.setNewWalkCmdIsRunning(false);

                        closedoorevent();
                        cycle();
                        container.stop();

                    }

                }

                @Override
                public void onStopped() {

                }
            }, 1);
           // CommandManager.execute(player,"wlkto");


        } else {
            PathFinder.getPathFinder().findRouteNoclip(player, 2912, 9968, true, 0, 0);
            //CommandManager.execute(player,"wlkto");
            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (player.getAgilityHandler().hotSpot(player, 2912, 9968)) {
                        player.facePosition(new Position(2911, 9968));
                        player.getPA().object(10043, 2911, 9968, 1, 0);

                        PathFinder.getPathFinder().findRouteNoclip(player, 2911, 9968, true, 0, 0);
                        player.setNewWalkCmdIsRunning(false);

                        closedoorevent();
                        cycle();
                        container.stop();

                    }

                }

                @Override
                public void onStopped() {

                }
            }, 1);

            closedoorevent();
        }


    }
    public void handleDoor() {
        //Walking out of the cyclops room
        if (player.absX > 2911) {
            walkthrudoor(false);
            //Walking into the cyclops room
        } else if (Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
            if (player.getItems().playerHasItem(8851, 200)) {
                int current = currentDefender();
                if (current == -1) {
                    player.getDH().sendNpcChat3("You are not in the possession of a rune defender.", "You must kill cyclops upstairs to obtain a defender.",
                            "Then come back here.", 2135, "Lorelai");
                    player.nextChat = -1;
                } else {
                    player.getDH().sendNpcChat3("You are currently in possession of a " + ItemAssistant.getItemName(current) + ".",
                            "It will cost 200 tokens to re-enter the cyclops area.", "Do you want to enter?", 2135, "Lorelai");
                    player.nextChat = 6277;
                }
            } else {
                player.getDH().sendNpcChat2("You need at least 200 warrior guild tokens.", "You can get some by operating the armour animator.", 2135, "Lorelai");
                player.nextChat = 0;
            }
        }
    }

    /**
     * Attempts to return the value of the defender the player is wearing or is in posession of in their inventory.
     *
     * @return -1 will be returned in the case that the player does not have a defender
     */
    private int currentDefender() {
        for (int index = DEFENDER_DATA.length - 1; index > -1; index--) {
            int[] defender = DEFENDER_DATA[index];
            if (player.getItems().playerHasItem(defender[0]) || player.getItems().isWearingItem(defender[0])) {
                return defender[0];
            }
        }
        return -1;
    }

    /**
     * Attempts to return the next best defender.
     *
     * @return The first defender, bronze, if the player doesnt have a defender. If the player has the best it will return the best. If either of the afforementioned conditions are
     *         not met, the next best defender is returned.
     */
    private int nextDefender() {
        int defender = currentDefender();
        if (defender == -1) {
            return DEFENDER_DATA[0][0];
        }
        int best = DEFENDER_DATA[DEFENDER_DATA.length - 1][0];
        if (best == defender) {
            return best;
        }
        int index = indexOf(defender);
        if (index != -1) {
            defender = DEFENDER_DATA[index + 1][0];
        }
        return defender;
    }

    /**
     * Attempts to retrieve the index in the array of the defender
     *
     * @param defender the defender
     * @return -1 will be returned if the defender cannot be found
     */
    private int indexOf(int defender) {
        for (int index = 0; index < DEFENDER_DATA.length; index++) {
            if (defender == DEFENDER_DATA[index][0]) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Retrieves the drop chance of the next best defender the player can receive.
     *
     * @return the chance of the dropped dagger.
     */
    private int chance() {
        Optional<int[]> defender = Arrays.stream(DEFENDER_DATA).filter(data -> data[0] == nextDefender()).findFirst();
        return defender.isPresent() ? defender.get()[1] : 0;
    }

    public void dropDefender(int x, int y) {
        int amount = player.getItems().getItemAmount(8851);
        if (isActive() && Boundary.isIn(player, CYCLOPS_BOUNDARY) && !Boundary.isIn(player, WAITING_ROOM_BOUNDARY) && amount > 1) {
            int chance = chance();
            int current = currentDefender();
            int item = current == -1 ? DEFENDER_DATA[0][0] : nextDefender();
          if (Misc.random(chance) == 0) {
                Server.itemHandler.createGroundItem(player, item, x, y, player.heightLevel, 1, player.getIndex());
                player.sendMessage("@blu@The cyclops dropped a " + ItemAssistant.getItemName(item) + " on the ground.", 600000);
           }
        }
    }

    private void removeFromRoom() {
        player.getPA().movePlayer(2911, 9968, 0);
        player.getDH().sendStatement("You do not have enough tokens to continue.");
        player.nextChat = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
