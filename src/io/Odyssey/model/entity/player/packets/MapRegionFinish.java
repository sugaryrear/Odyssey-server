package io.Odyssey.model.entity.player.packets;

import io.Odyssey.Server;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GlobalDropsHandler;

public class MapRegionFinish implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
//why is this being called twice?
		Server.itemHandler.reloadItems(c);
		GlobalDropsHandler.load(c);
		c.customclips();
		Server.getGlobalObjects().updateRegionObjects(c);
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}

		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}
		if(c.getBarrows().getActive().isPresent())
		c.getBarrows().getActive().get().setActive(false);


	}

}
