package io.Odyssey.model.cycleevent.impl;

import java.util.concurrent.TimeUnit;

import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Misc;

public class NexGodWarsEvent extends Event<Object> {


	private static final int INTERVAL = Misc.toCycles(10, TimeUnit.SECONDS);

	
	public NexGodWarsEvent() {
		super("", new Object(), INTERVAL);
	}	

	@Override
	public void execute() {

//		PlayerHandler.nonNullStream().forEach(player -> {
//			player.getQuestTab().updateInformationTab();
//		});
//		int playersinnex=0;
//		if(NexNPC.nexnpc.isSpawned()) {//if nex is alive
//		//	System.out.println("its spawned");
//			if(NexNPC.nexnpc.currentPhase == null){//if current phase is null aka not in combat of any kind
//				playersinnex = Boundary.getPlayersInBoundary(Boundary.NEX);
//				if(playersinnex>0)
//					NexNPC.nexnpc.init();
//			}
//		} else {
//			new NexNPC(Npcs.NEX, NexNPC.SPAWN_POSITION);
//		}
//
//	}
	}
} 