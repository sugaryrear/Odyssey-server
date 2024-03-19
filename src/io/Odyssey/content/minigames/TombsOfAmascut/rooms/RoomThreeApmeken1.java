package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Apmeken1;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class RoomThreeApmeken1 extends TombsOfAmascutRoom {

    @Override
    public Apmeken1 spawn(InstancedArea instancedArea) {
        return new Apmeken1(instancedArea);
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3794, 5280);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {
        if (player.getY() >= 4256) {
            player.getPA().movePlayer(player.getX(), 4254, player.getHeight());
        } else {
            player.getPA().movePlayer(player.getX(), 4256, player.getHeight());
        }
    }

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return instancedArea.getNpcs().isEmpty();
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.APMEKEN1_BOSS_ROOM_BOUNDARY;
    }

    @Override
    public Position getDeathPosition() {
        return new Position(3795, 5280);
    }

    @Override
    public Position getFightStartPosition() {
        return new Position(3807, 5279);
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return getFoodChest(new Position(3795, 5277, 0), 2);
    }
}
