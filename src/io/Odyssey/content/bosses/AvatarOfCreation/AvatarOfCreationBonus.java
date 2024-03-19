package io.Odyssey.content.bosses.AvatarOfCreation;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;

import static io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.AvatarOfCreation_PLANTER_OBJECT;

public interface AvatarOfCreationBonus {

    void activate(Player player);

    void deactivate();

    boolean canPlant(Player player);

    AvatarOfCreationBonusPlant getPlant();

    Position OBJECT_POSITION = new Position(3091, 3468);

    default void updateObject(boolean adding) {
        GlobalObject grass = new GlobalObject(AvatarOfCreation_PLANTER_OBJECT, OBJECT_POSITION, 0, 10);
        GlobalObject object = new GlobalObject(getPlant().getObjectId(), OBJECT_POSITION, 0, 10);
        if (adding) {
            //Server.getGlobalObjects().remove(grass);
            //Server.getGlobalObjects().add(object);
        } else {
            //Server.getGlobalObjects().remove(object);
            //Server.getGlobalObjects().add(grass);
        }
    }

}
