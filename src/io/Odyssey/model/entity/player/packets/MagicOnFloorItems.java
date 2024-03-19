package io.Odyssey.model.entity.player.packets;

import java.util.Objects;
import java.util.Optional;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.hydra.CombatProjectile;
import io.Odyssey.content.combat.magic.MagicRequirements;
import io.Odyssey.model.collisionmap.PathChecker;
import io.Odyssey.model.collisionmap.doors.Location;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.items.GlobalDropsHandler;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;

/**
 * Magic on floor items
 **/
public class MagicOnFloorItems implements PacketType {
	//private static final CombatProjectile TELEGRAB = new CombatProjectile(143, 50, 10, 0, 50, 0, 50);
	private static final CombatProjectile TELEGRAB = new CombatProjectile(143, 50, 0, 0, 100, 0, 50);


	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getMovementState().isLocked() || c.getLock().cannotInteract(c))
			return;
		if (c.isFping()) {
			/**
			 * Cannot do action while fping
			 */
			return;
		}


		c.interruptActions();
		int itemY = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readUnsignedWord();
		int itemX = c.getInStream().readSignedWordBigEndian();
		int spellId = c.getInStream().readUnsignedWordA();
		if (c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
					c.resetWalkingQueue();
		c.stopMovement();
		}
		if (Math.abs(c.getX() - itemX) > 25 || Math.abs(c.getY() - itemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		if (!Server.itemHandler.itemExists(c, itemId, itemX, itemY, c.heightLevel)) {
			c.stopMovement();
			return;
		}

		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		int offY = (c.getX() - itemX) * -1;
		int offX = (c.getY() - itemY) * -1;
		if (c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {

			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(!PathChecker.raycast(c.getPosition(),new Position(itemX,itemY),true)) {
						//c.cantmoveduringtelegrab=true;
						c.facePosition(itemX, itemY);
						//n.facePlayer(c.getIndex());
						c.faceUpdate(0);
						//container.stop();
					} else {
						//do it
						c.resetWalkingQueue();
						c.stopMovement();
						container.stop();


					}
				}

				@Override
				public void onStopped() {
//c.setMovementState(new PlayerMovementStateBuilder().setLocked(true).setAllowClickToMove(false).createPlayerMovementState());
					c.usingMagic = true;
					if (!MagicRequirements.checkMagicReqs(c, 50, true)) {
						c.stopMovement();
						return;
					}
					c.facePosition(itemX, itemY);
					//n.facePlayer(c.getIndex());
					c.faceUpdate(0);
					//do it
					c.startAnimation(710);
					c.gfx100(142);
					c.stopMovement();
					c.getPA().sendProjectileToTile(new Location(itemX,itemY,c.getHeight()),TELEGRAB );
					//
					c.itemId=itemId;
					c.itemY=itemY;
					c.itemX=itemX;
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {


							Optional<GlobalDropsHandler.GlobalDrop> globalitem = GlobalDropsHandler.getGlobalItem(c.itemId,c.itemX,c.itemY);
							if (globalitem.isPresent()) {
								globalitem.get().setTaken(true);
							}
							c.resetWalkingQueue();
							c.stopMovement();
							Server.itemHandler.removeGroundItem(c, c.itemId, c.itemX, c.itemY, c.heightLevel, true);
							c.getPA().sendSound(2582);
							c.getPA().stillGfx(144, itemX, itemY, 0, 0);
								container.stop();
							}


						@Override
						public void onStopped() {

						}
					}, 5);


				}
			}, 1);

//			//can see
//			if(!PathChecker.raycast(c.getPosition(),new Position(itemX,itemY),true)){
//				c.sendMessage("Not in los.");
//				return;
//			}
//			c.cantmoveduringtelegrab=true;
//			//c.setMovementState(new PlayerMovementStateBuilder().setLocked(true).setAllowClickToMove(false).createPlayerMovementState());
//
//			//c.setMovementState(PlayerMovementState.getDefault());
//
//			c.facePosition(itemX, itemY);
//			//n.facePlayer(c.getIndex());
//			c.faceUpdate(0);
//			c.getPA().resetFollow();
//			c.startAnimation(710);
//			c.gfx100(142);
//			c.getPlayerAssistant().createPlayersStillGfx(144, itemX, itemY,
//					0, 72);
//			c.getPlayerAssistant().createPlayersProjectile(c.getX(),
//					c.getY(), offX, offY, 50, 70,
//					143, 50, 10, 0, 50);
//		//	c.setStopPlayer(true);
//			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//				@Override
//				public void execute(CycleEventContainer container) {
//					c.getPA().stillGfx(144, itemX, itemY, 0, 0);
//					//}
//					//if (c.clickNpcType == 0 || c.clickNpcType > 1)
//				//	c.setMovementState(PlayerMovementState.getDefault());
//					//c.cantmoveduringtelegrab=false;
//					//c.setStopPlayer(false);
//					container.stop();
//				}
//
//				@Override
//				public void onStopped() {
//					//c.clickNpcType = 0;
//				}
//			}, 4);
//
//			//                            player.runFn(3, () -> {
//			//                                new Projectile(player.tile(), new Tile(groundItemX,groundItemY), 0, 143, 30, 2, 20, 0,1).sendProjectile();
//			//
//			//                            });
//			//
//			//
//			//                            player.runFn(4, () -> {
//			//                                World.getWorld().tileGraphic(144, tile, 0, 0);
//			//                                PickupItemPacketListener.pickup(player, new Item(groundItemId), tile, true);
//			//                        player.unlock();
//			//                            });
//			//c.getActions().firstClickNpc(n);
//		} else {
//		//	c.clickNpcType = 1;
//			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//			@Override
//			public void execute(CycleEventContainer container) {
//				//if ((c.clickNpcType == 1) && n != null) {
//				//if (c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
////					c.facePosition(itemX, itemY);
////					//n.facePlayer(c.getIndex());
////					c.faceUpdate(0);
////					c.getPA().resetFollow();
////					//c.getActions().firstClickNpc(n);
//					container.stop();
//				//}
//
//			}
//
//			@Override
//			public void onStopped() {
//				//c.clickNpcType = 0;
//			}
//		}, 1);
	}
	}

}
