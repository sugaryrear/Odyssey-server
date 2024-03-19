package io.Odyssey.content.minigames.fight_cave;

import io.Odyssey.model.Npcs;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class JadNPC extends NPC {

    private boolean minionSpawn;
    NPC ythurtokNPC;

    private Player player;
    public JadNPC(int npcId, Position position, Player player) {
        super(npcId, position);
        this.maxHit = 70;
        this.player = player;
        this.spawnedBy = player.getIndex();
    }



    @Override
    public void process() {
        // setAttacks();
        if(!minionSpawn){
            if(this.getHealth().getCurrentHealth() <= (this.getHealth().getMaximumHealth() * 0.8)){
                minionSpawn = true;
                ythurtokNPC = new YtHurkotNPC(Npcs.YT_HURKOT, new Position(this.getX()+3, this.getY()-2, this.heightLevel),player,this);
                ythurtokNPC = new YtHurkotNPC(Npcs.YT_HURKOT, new Position(this.getX()-3, this.getY()+2, this.heightLevel),player,this);


            }
        }

        super.process();

    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        if (this.spawnedBy != entity.getIndex()) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                if (p != null)
                    p.sendMessage(this.getName()+" isn't after you.");
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean canBeDamaged(Entity entity) {

        return true;
    }

    @Override
    public boolean isFreezable() {
        return false;
    }

}


