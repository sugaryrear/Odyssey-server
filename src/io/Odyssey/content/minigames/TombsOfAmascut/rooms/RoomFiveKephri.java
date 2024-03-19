package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Kephri;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class RoomFiveKephri extends TombsOfAmascutRoom {

    @Override
    public Kephri spawn(InstancedArea instancedArea) {
        return new Kephri(instancedArea);
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3537, 5408);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {
        if (worldObject.getY() < 4392) {        // South
            if (player.getY() >= 4380) {
                player.getPA().movePlayer(player.getX(), 4378, player.getHeight());
            } else {
                player.getPA().movePlayer(player.getX(), 4380, player.getHeight());
            }
        } else {                                // North
            if (player.getY() >= 4396) {
                player.getPA().movePlayer(player.getX(), 4394, player.getHeight());
            } else {
                player.getPA().movePlayer(player.getX(), 4396, player.getHeight());
            }
        }
    }

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return instancedArea.getNpcs().isEmpty();
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.KEPHRI_BOSS_ROOM_BOUNDARY;
    }

    @Override
    public Position getDeathPosition() {
        return new Position(3550, 5408);
    }

    @Override
    public Position getFightStartPosition() {
        return new Position(3543, 5409);
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return getFoodChest(new Position(3546, 5400), 3);
    }
}
