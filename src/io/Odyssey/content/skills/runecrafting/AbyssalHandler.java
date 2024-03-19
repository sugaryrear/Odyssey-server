package io.Odyssey.content.skills.runecrafting;


import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.mining.Pickaxe;
import io.Odyssey.content.skills.woodcutting.Hatchet;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

import static io.Odyssey.model.Items.TINDERBOX;

public class AbyssalHandler {

    public static void handleAbyssalTeleport(Player player, int objectId) {
        switch (objectId) {
            case 26208:
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        player.startAnimation(844);


                            if(objectId == 26208)
                                player.getPlayerAssistant().movePlayer(3038, 4846, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3028, 4840, 0);


                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 5);
                break;
            case 26250:

            case 26192:
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        player.startAnimation(844);
                        boolean didwepass = false;

                        if(Misc.random(100) < (player.playerLevel[Player.playerAgility]+1))
                            didwepass=true;


                        if(didwepass){
                            if(objectId == 26250)
                                player.getPlayerAssistant().movePlayer(3049, 4841, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3028, 4840, 0);
                            player.getPA().addSkillXPMultiplied(25,player.playerLevel[Player.playerAgility],true);

                        } else {
                            player.sendMessage("You fail to pass the obstacle.");
                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 5);
                break;
            case 26251:

            case 26191:
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        player.startAnimation(881);
                        boolean didwepass = false;

                        if(Misc.random(100) < (player.playerLevel[Player.playerThieving]+1))
                            didwepass=true;


                        if(didwepass){
                            if(objectId == 26251)
                                player.getPlayerAssistant().movePlayer(3051, 4839, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3028, 4840, 0);
                            player.getPA().addSkillXPMultiplied(25,player.playerLevel[Player.playerThieving],true);

                        } else {
                            player.sendMessage("You fail to pass the obstacle.");
                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 5);
                break;
            case 26252:

            case 26190:
                if (!player.getItems().playerHasItem(TINDERBOX)) {
                player.sendMessage("You need a tinderbox to light this.");
                return;
            }
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {

                        player.startAnimation(733);
                        boolean didwepass = false;

                        if(Misc.random(100) < (player.playerLevel[Player.playerFiremaking]+1))
                            didwepass=true;


                        if(didwepass){
                            if(objectId == 26252)
                                player.getPlayerAssistant().movePlayer(3054, 4830, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3024, 4833, 0);
                            player.getPA().addSkillXPMultiplied(25,player.playerLevel[Player.playerFiremaking],true);

                        } else {
                            player.sendMessage("You fail to pass the obstacle.");
                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 3);
                break;
            case 26188:
            case 26574:
                Pickaxe pickaxe = Pickaxe.getBestPickaxe(player);
                if (pickaxe == null) {
                    player.sendMessage("You need a pickaxe to mine this vein.");
                    return;
                }
                player.startAnimation(pickaxe.getAnimation());
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {

                        boolean didwepass = false;

                        if(Misc.random(100) < (player.playerLevel[Player.playerMining]+1))
                            didwepass=true;


                        if(didwepass){
                            if(objectId == 26188)
                                player.getPlayerAssistant().movePlayer(3030, 4832, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3048, 4831, 0);
                            player.getPA().addSkillXPMultiplied(25,player.playerLevel[Player.playerMining],true);

                        } else {
                            player.sendMessage("You fail to pass the obstacle.");
                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 3);
                break;
            case 26253:
            case 26189:
                Hatchet hatchet = Hatchet.getBest(player);
                if (hatchet == null) {
                    player.sendMessage("You must have an axe to chop the tendrils down.");
                    return;
                }
                player.startAnimation(hatchet.getAnimation());
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {


                    @Override
                    public void execute(CycleEventContainer container) {

                        boolean didwepass = false;

                        if(Misc.random(100) < (player.playerLevel[Player.playerWoodcutting]+1))
                            didwepass=true;


                        if(didwepass){

                            if(objectId == 26253)
                                player.getPlayerAssistant().movePlayer(3051, 4824, 0);
                            else
                                player.getPlayerAssistant().movePlayer(3048, 4821, 0);

                            player.getPA().addSkillXPMultiplied(25,player.playerLevel[Player.playerWoodcutting],true);
                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {

                    }
                }, 3);
                break;
            case 7147: // squeeze through gap
                player.getPlayerAssistant().movePlayer(3030, 4842, 0);
                break;
            case 24975: // nature
                player.getPlayerAssistant().startTeleport(2395, 4841, 0, "modern",false);
                break;
            case 24974: // cosmic
                player.getPlayerAssistant().startTeleport(2144, 4831, 0, "modern",false);
                break;
            case 24971: // fire
                player.getPlayerAssistant().startTeleport(2585, 4836, 0, "modern",false);
                break;
            case 24972: // earth
                player.getPlayerAssistant().startTeleport(2658, 4839, 0, "modern",false);
                break;
            case 24973: // body
                player.getPlayerAssistant().startTeleport(2525, 4830, 0, "modern",false);
                break;
            case 25379: // mind
                player.getPlayerAssistant().startTeleport(2786, 4834, 0, "modern",false);
                break;
            case 25378: // air
                player.getPlayerAssistant().startTeleport(2844, 4837, 0, "modern",false);
                break;
            case 25376: // water
                player.getPlayerAssistant().startTeleport(2722, 4833, 0, "modern",false);
                break;
            case 25035: // death
                player.getPlayerAssistant().startTeleport(2205, 4834, 0, "modern",false);
                break;
            case 25034: // law
                player.getPlayerAssistant().startTeleport(2464, 4830, 0, "modern",false);
                break;
            case 24976: // chaos
                player.getPlayerAssistant().startTeleport(2274, 4842, 0, "modern",false);
                break;
            case 25377: // soul
                player.getPlayerAssistant().startTeleport(2150, 3864, 0, "modern",false);
break;
            case 43848: // blood
                player.getPlayerAssistant().startTeleport(3231, 4826, 0, "modern",false);

                break;
        }
    }

}
