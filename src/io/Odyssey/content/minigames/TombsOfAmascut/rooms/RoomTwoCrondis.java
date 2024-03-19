package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Crondis;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class RoomTwoCrondis extends TombsOfAmascutRoom {

    @Override
    public Crondis spawn(InstancedArea instancedArea) {
        return new Crondis(instancedArea);
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3957, 5408);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {
        if (player.getX() > 3300) {         // West gate
            if (player.getX() <= 3303) {
                player.getPA().movePlayer(3305, player.getY(), player.getHeight());
            } else {
                player.getPA().movePlayer(3303, player.getY(), player.getHeight());
            }
        } else {                            // East gate
            if (player.getX() <= 3286) {
                player.getPA().movePlayer(3288, player.getY(), player.getHeight());
            } else {
                player.getPA().movePlayer(3286, player.getY(), player.getHeight());
            }
        }
    }

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return instancedArea.getNpcs().isEmpty();
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.CRONDIS_BOSS_ROOM_BOUNDARY;
    }


    @Override
    public Position getDeathPosition() {
        return new Position(3939, 5409);
    }
    @Override
    public Position getFightStartPosition() {
        return new Position(3939, 5409, 0);
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return getFoodChest(new Position(3920, 5278, 0), 2);
    }
}
