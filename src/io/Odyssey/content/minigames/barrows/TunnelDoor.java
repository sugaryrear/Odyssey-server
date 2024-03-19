package io.Odyssey.content.minigames.barrows;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

public class TunnelDoor extends GlobalObject {

    //	public GlobalObject(int id, int x, int y, int height, int face) {
    public TunnelDoor(int doorID, Position position, TunnelDoors.Rotation rotation) {
         super(doorID, position.getX(),position.getY(), 0, rotation);
    }

    public static TunnelDoor create(int doorID, Position position, TunnelDoors.Rotation rotation) {
        return new TunnelDoor(doorID, position, rotation);
    }

//    public void resetDoor(Player client) {
//        GameObjectManager.placeLocalObject(client, this);
//    }
//
//    public void closeDoor(Player client, int closeID) {
//        GameObjectManager.placeLocalObject(client, new GameObject(closeID, getPosition(), 0, getRotation()));
//    }
}