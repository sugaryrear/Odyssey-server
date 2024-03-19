package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.Server;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.item.lootable.impl.TombsOfAmascutChest;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutBoss;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import org.apache.commons.lang3.tuple.Pair;

public class RoomSevenLoot extends TombsOfAmascutRoom {

    private static final int CHEST_ID = 44545;
    private static final int RARE_CHEST_ID = 44826;
    private static final int CHEST_OPEN_ID = 44545;

    private static final GlobalObject[] CHESTS = {
            new GlobalObject(CHEST_ID, new Position(3673, 5148), 3, 10),
            new GlobalObject(CHEST_ID, new Position(3673, 5145), 3, 10),
            new GlobalObject(CHEST_ID, new Position(3673, 5142), 0, 10),
            new GlobalObject(CHEST_ID, new Position(3673, 5139), 1, 10),
            new GlobalObject(CHEST_ID, new Position(3686, 5148), 1, 10),
            new GlobalObject(CHEST_ID, new Position(3686, 5145), 1, 10),
            new GlobalObject(CHEST_ID, new Position(3686, 5142), 1, 10),
            new GlobalObject(CHEST_ID, new Position(3681, 5140), 0, 10)
    };

    public static void openChest(Player player, TombsOfAmascutInstance tombsOfAmascutInstance) {
        if (tombsOfAmascutInstance.getChestRewards().contains(player.getLoginName())) {
            player.sendMessage("You've already received your rewards!");
            return;
        }

        tombsOfAmascutInstance.getChestRewards().add(player.getLoginName());
        TombsOfAmascutChest.rewardItems(player, tombsOfAmascutInstance.getChestRewardItems().get(player.getLoginName()));
    }
    public void spawnosmumten(NPC npc) {

        npc.getCombatDefinition();
    }
    @Override
    public TombsOfAmascutBoss spawn(InstancedArea instancedArea) {
        spawnosmumten(new TombsOfAmascutBoss(11693, new Position(3680, 5142, instancedArea.getHeight()), instancedArea));

        TombsOfAmascutInstance tombsOfAmascutInstance = (TombsOfAmascutInstance) instancedArea;
        Player rareWinner = Misc.random(tombsOfAmascutInstance.getPlayers());//tobInstance.getMvpPoints().chooseRareWinner(tobInstance.getPlayers());

        instancedArea.getPlayers().forEach(player -> {
            tombsOfAmascutInstance.getChestRewardItems().put(player.getLoginName(),
                    TombsOfAmascutChest.getRandomItems(player.equals(rareWinner), tombsOfAmascutInstance.getPartySize()));

            Pair<Integer, Integer> rank = tombsOfAmascutInstance.getMvpPoints().getRank(player);
            player.sendMessage("You ranked @pur@#" + rank.getLeft() + "@bla@ with @pur@" + rank.getRight() + " points.");

            if (rareWinner.equals(player)) {
                player.sendMessage("@pur@You have been selected as the MVP.");
            } else {
                player.sendMessage("@pur@" + rareWinner.getDisplayNameFormatted() + " has been selected as the MVP.");
            }
        });

        for (int index = 0; index < instancedArea.getPlayers().size(); index++) {
            Player player = instancedArea.getPlayers().get(index);
            GlobalObject chest = CHESTS[index];

            if (TombsOfAmascutChest.containsRare(tombsOfAmascutInstance.getChestRewardItems().get(player.getLoginName()))) {
                chest = chest.withId(RARE_CHEST_ID);
            }

            Position position = chest.getPosition();
            GlobalObject chestObject = chest.withPosition(instancedArea.resolve(position)).setInstance(instancedArea);
            Server.getGlobalObjects().add(chestObject);
            instancedArea.getPlayers().get(index).getPA().createObjectHints(position.getX() + 1, position.getY() + 1, 100, 5);
        }
        return null;
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3680, 5169, 0);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        TombsOfAmascutInstance tombsOfAmascutInstance = (TombsOfAmascutInstance) player.getInstance();
        if (worldObject.getId() == TombsOfAmascutConstants.TREASURE_ROOM_EXIT_INSTANCE_OBJECT_ID) {
            player.start(new DialogueBuilder(player).option(
                    new DialogueOption("Leave", plr -> {
                        plr.moveTo(TombsOfAmascutConstants.FINISHED_TOMBS_OF_AMASCUT_POSITION);
                        plr.getPA().closeAllWindows();
                    }),
                    new DialogueOption("Stay", plr -> plr.getPA().closeAllWindows())));

        } else if (worldObject.getId() == CHEST_ID || worldObject.getId() == RARE_CHEST_ID) {
//            if (player.getTombsOfAmascutContainer().inTombsOfAmascut()) {
            int index = player.getInstance().getPlayers().indexOf(player);

            if (index == -1) {
                player.sendMessage("@red@There was an issue getting your chest..");
                return true;
            }

            Position clicked = player.getInstance().resolve(worldObject.getPosition());
            Position actual = player.getInstance().resolve(CHESTS[index].getPosition());
            if (!clicked.equals(actual)) {
                player.sendMessage("That's not your chest!");
                return true;
            }

            if (tombsOfAmascutInstance.getChestRewards().contains(player.getLoginName())) {
                player.sendMessage("You've already received your rewards!");
                return true;
            }

            player.getPA().createObjectHints(worldObject.getX() + 1, worldObject.getY() + 1, 0, 0);
            Server.getGlobalObjects().remove(worldObject.toGlobalObject());
            Server.getGlobalObjects().add(worldObject.toGlobalObject().withId(CHEST_OPEN_ID).setInstance(player.getInstance()));
            openChest(player, tombsOfAmascutInstance);
        }
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {}

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return true;
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.LOOT_ROOM_BOUNDARY;
    }

    @Override
    public Position getDeathPosition() {
        return getPlayerSpawnPosition();
    }

    @Override
    public Position getFightStartPosition() {
        return null;
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return null;
    }
}
