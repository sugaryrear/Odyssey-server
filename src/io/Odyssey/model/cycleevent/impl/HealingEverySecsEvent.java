package io.Odyssey.model.cycleevent.impl;

import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Player;

public class HealingEverySecsEvent extends Event<Player> {

    /**
     * The maximum amount of ticks that we have to wait for our run to regenerate
     */
    private static final int MAXIMUM_TICKS = 12;

    /**
     * The minimum amount of ticks that we have to wait for our run to regenerate
     */
    private static final int MINIMUM_TICKS = 10;

    /**
     * The amount of agility levels that impact run energy regeneration
     */
    private static final int INTERVAL = 99 / (MAXIMUM_TICKS - MINIMUM_TICKS);

    /**
     * The amount of ticks that must pass before energy can be restored
     */
    private int ticksRequired = 10;

    public HealingEverySecsEvent(Player attachment, int ticks) {
        super("healing_pets_event", attachment, ticks);
    }


    @Override
    public void execute() {
        if (attachment == null || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (!PetHandler.hasHealingPet(attachment)){
            super.stop();
            return;
        }
        if(attachment.getHealth().isMaximum())
            return;

        attachment.getHealth().increase(howmuchtoheal(attachment, attachment.petSummonId));

    }
    private int howmuchtoheal(Player player, int summonId){
        int howmuchtoheal = 1;

        player.gfx0(1424);
//        switch(player.petSummonId){
//            case 6633:
//                howmuchtoheal = 2;
//                break;
//        }
        PetHandler.Pets petForItem = PetHandler.forItem(summonId);
        if (petForItem != null) {
            howmuchtoheal = petForItem.gethpheal();
        }
        return howmuchtoheal;
    }

    private final int updateTicksRequired() {
//        int level = Integer.min(99, attachment.playerLevel[Skill.AGILITY.getId()]);
//        int reduction = level < INTERVAL ? 0 : level / INTERVAL;
//        return Integer.max(MINIMUM_TICKS, MAXIMUM_TICKS - reduction);
        return 10;
    }

}