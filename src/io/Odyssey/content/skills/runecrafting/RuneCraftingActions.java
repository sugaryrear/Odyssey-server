package io.Odyssey.content.skills.runecrafting;

import io.Odyssey.model.entity.player.Player;

public class RuneCraftingActions {

    public static void handleRuneCrafting(Player player, int objectId) {
        switch (objectId) {
            case 34748:// air altar portal
                if (player.objectX == 2841 && player.objectY == 4828) {
                    player.getPlayerAssistant().startTeleport(2983, 3293, 0, "modern",false);
                }
                break;

            case 34749:// mind altar portal
                if (player.objectX == 2793 && player.objectY == 4827) {
                    player.getPlayerAssistant().startTeleport(2980, 3514, 0, "modern",false);
                }
                break;

            case 34750:// water altar portal
                if (player.objectX == 2727 && player.objectY == 4832) {
                    player.getPlayerAssistant().startTeleport(3184, 3162, 0, "modern",false);
                }
                break;

            case 34751:// earth rune portal
                if (player.objectX == 2655 && player.objectY == 4829) {
                    player.getPlayerAssistant().startTeleport(3308, 3476, 0, "modern",false);
                }
                break;

            case 34752:// fire rune portal
                if (player.objectX == 2574 && player.objectY == 4850) {
                    player.getPlayerAssistant().startTeleport(3311, 3256, 0, "modern",false);
                }
                break;

            case 34753:// body altar portal
                if (player.objectX == 2523 && player.objectY == 4825) {
                    player.getPlayerAssistant().startTeleport(3051, 3444, 0, "modern",false);
                }
                break;

            case 34754:// cosmic altar portal
                if (player.objectX == 2163 && player.objectY == 4833 || player.objectX == 2142 && player.objectY == 4854 || player.objectX == 2121 && player.objectY == 4833 || player.objectX == 2412 && player.objectY == 4812) {
                    player.getPlayerAssistant().startTeleport(2410, 4379, 0, "modern",false);
                }
                break;

            case 34755:// law altar portal
                if (player.objectX == 2464 && player.objectY == 4817) {
                    player.getPlayerAssistant().startTeleport(2857, 3379, 0, "modern",false);
                }
                break;

            case 34756:// nature portal altar
                if (player.objectX == 2400 && player.objectY == 4834) {
                    player.getPlayerAssistant().startTeleport(2866, 3022, 0, "modern",false);
                }
                break;

            case 34757:
                if (player.objectX == 3233 && player.objectY == 9312) {// desert
                    // treasure
                    // portal
                    player.getPlayerAssistant().startTeleport(3233, 2887, 0, "modern",false);
                } else if (player.objectX == 2282 && player.objectY == 4837) {// chaos
                    // altar
                    // portal
                    player.getPlayerAssistant().startTeleport(3062, 3593, 0, "modern",false);
                }
                break;

            case 34758:// death altar portal
                if (player.objectX == 2208 && player.objectY == 4829) {
                    player.getPlayerAssistant().startTeleport(1863, 4639, 0,
                            "modern",false);
                }
                break;
            case 34759:// wrath altar portal

                    player.getPlayerAssistant().startTeleport(2013, 3650, 0,
                            "modern",false);

                break;
            case 43478:// blood altar portal

                    player.getPlayerAssistant().startTeleport(2013, 3650, 0,
                            "modern",false);

                break;
        }
    }

}

