package io.Odyssey.model.entity.player.packets;

import io.Odyssey.model.ContainerAction;
import io.Odyssey.model.ContainerActionType;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;

public class ContainerAction7 implements PacketType {//all but 1

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		if (player.getMovementState().isLocked() || player.getLock().cannotInteract(player))
			return;
		if (player.isFping()) {
			/**
			 * Cannot do action while fping
			 */
			return;
		}
		player.interruptActions();
		int interfaceId = player.getInStream().readUnsignedWord();
		int itemId = player.getInStream().readSignedWordBigEndianA();
		int itemSlot = player.getInStream().readSignedWordBigEndian();

		ContainerAction action = new ContainerAction(ContainerActionType.ACTION_7, interfaceId, itemId, itemSlot);

		if (player.debugMessage)
			player.sendMessage("ContainerAction7: interfaceid: "+interfaceId+", removeSlot: "+itemSlot+", removeID: " + itemId);
		
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}

		if (player.getBank().withdraw(interfaceId, itemId, player.getBank().getAllButOne(interfaceId, itemId))) {
			return;
		}
	}

}
