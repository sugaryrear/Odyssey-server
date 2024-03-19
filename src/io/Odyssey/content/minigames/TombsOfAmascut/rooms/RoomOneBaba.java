package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Baba;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class              RoomOneBaba extends TombsOfAmascutRoom {

    @Override
    public Baba spawn(InstancedArea instancedArea) {
        return new Baba(instancedArea);
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3793, 5408);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {
        if (player.getX() >= 3186) {
            player.getPA().movePlayer(3184, player.getY(), player.getHeight());
        } else if (player.getX() <= 3184) {
            player.getPA().movePlayer(3186, player.getY(), player.getHeight());
        }
    }

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return instancedArea.getNpcs().isEmpty();
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.BABA_BOSS_ROOM_BOUNDARY;
    }

    @Override
    public Position getDeathPosition() {
        return new Position(3801, 5405);
    }

    @Override
    public Position getFightStartPosition() {
        return new Position(3801, 5405, 0);
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return getFoodChest(new Position(3175, 4422, 0), 0);
    }
}