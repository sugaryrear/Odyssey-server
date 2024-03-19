package io.Odyssey.content.minigames.TombsOfAmascut.rooms;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutRoom;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Akkha;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class RoomFourAkkha extends TombsOfAmascutRoom {

    @Override
    public Akkha spawn(InstancedArea instancedArea) {
        return new Akkha(instancedArea);
    }

    @Override
    public Position getPlayerSpawnPosition() {
        return new Position(3696, 5407,1);
    }

    @Override
    public boolean handleClickObject(Player player, WorldObject worldObject, int option) {
        return false;
    }

    @Override
    public void handleClickBossGate(Player player, WorldObject worldObject) {
        if (player.getY() >= 4308) {
            player.getPA().movePlayer(player.getX(), 4306, player.getHeight());
        } else {
            player.getPA().movePlayer(player.getX(), 4308, player.getHeight());
        }
    }

    @Override
    public boolean isRoomComplete(InstancedArea instancedArea) {
        return instancedArea.getNpcs().isEmpty();
    }

    @Override
    public Boundary getBoundary() {
        return TombsOfAmascutConstants.AKKHA_BOSS_ROOM_BOUNDARY;
    }

    @Override
    public Position getDeathPosition() {
        return new Position(3690, 5408,1 );
    }

    @Override
    public Position getFightStartPosition() {
        return new Position(3690, 5408, 1);
    }

    @Override
    public GlobalObject getFoodChestPosition() {
        return getFoodChest(new Position(3694, 5405, 1), 0);
    }
}
