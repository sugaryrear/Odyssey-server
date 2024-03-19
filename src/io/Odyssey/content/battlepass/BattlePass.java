package io.Odyssey.content.battlepass;


import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Misc;

import java.util.Calendar;

public class BattlePass {

    private final Player player;
    public int TICKET = 27;
    public static int currentSeason = 1;
    public int MONTH = 11;
    /*
     * all tiers
     */
    public int TIER_1 = 537; public int TIER_2 = 990; public int TIER_3 = 6199; public int TIER_4 = 23933; public int TIER_5 = 995;
    public int TIER_6 = 19484; public int TIER_7 = 2528; public int TIER_8 = 12526; public int TIER_9 = 12638; public int TIER_10 = 208;

    public int TIER_11 = 6199; public int TIER_12 = 12849; public int TIER_13 = 12846; public int TIER_14 = 23490; public int TIER_15 = 1437;
    public int TIER_16 = 2528; public int TIER_17 = 11212; public int TIER_18 = 2364; public int TIER_19 = 10551; public int TIER_20 = 6570;

    public int TIER_21 = 22114; public int TIER_22 = 12954; public int TIER_23 = 995; public int TIER_24 = 4185; public int TIER_25 = 6792;
    public int TIER_26 = 2577; public int TIER_27 = 12757; public int TIER_28 = 6828; public int TIER_29 = 8901; public int TIER_30 = 13346;

    /*
     * all tiers amount of item
     */
    public int TIER_1_AMOUNT = 100; public int TIER_2_AMOUNT = 3; public int TIER_3_AMOUNT = 1; public int TIER_4_AMOUNT = 5; public int TIER_5_AMOUNT = 8_000_000;
    public int TIER_6_AMOUNT = 30; public int TIER_7_AMOUNT = 2; public int TIER_8_AMOUNT = 1; public int TIER_9_AMOUNT = 1; public int TIER_10_AMOUNT = 50;

    public int TIER_11_AMOUNT = 2; public int TIER_12_AMOUNT = 1; public int TIER_13_AMOUNT = 1; public int TIER_14_AMOUNT = 3; public int TIER_15_AMOUNT = 125;
    public int TIER_16_AMOUNT = 4; public int TIER_17_AMOUNT = 150; public int TIER_18_AMOUNT = 45; public int TIER_19_AMOUNT = 1; public int TIER_20_AMOUNT = 1;

    public int TIER_21_AMOUNT = 1; public int TIER_22_AMOUNT = 1; public int TIER_23_AMOUNT = 10_000_000; public int TIER_24_AMOUNT = 1; public int TIER_25_AMOUNT = 1;
    public int TIER_26_AMOUNT = 1; public int TIER_27_AMOUNT = 1; public int TIER_28_AMOUNT = 1; public int TIER_29_AMOUNT = 1; public int TIER_30_AMOUNT = 1;
    /*
     * all tiers XPS
     */
    public int TIER_1_XP = 2300; public int TIER_2_XP = 7000; public int TIER_3_XP = 20_000; public int TIER_4_XP = 45_000; public int TIER_5_XP = 90_000;
    public int TIER_6_XP = 170_000; public int TIER_7_XP = 350_000; public int TIER_8_XP = 700_000; public int TIER_9_XP = 900_000; public int TIER_10_XP = 1_400_000;

    public int TIER_11_XP = 3_500_000; public int TIER_12_XP = 8_000_000; public int TIER_13_XP = 10_000_000; public int TIER_14_XP = 13_000_000; public int TIER_15_XP = 15_000_000;
    public int TIER_16_XP = 17_000_000; public int TIER_17_XP = 19_000_000; public int TIER_18_XP = 21_000_000; public int TIER_19_XP = 23_000_000; public int TIER_20_XP = 26_000_000;

    public int TIER_21_XP = 29_000_000; public int TIER_22_XP = 32_000_000; public int TIER_23_XP = 35_000_000; public int TIER_24_XP = 38_000_000; public int TIER_25_XP = 42_000_000;
    public int TIER_26_XP = 43_000_000; public int TIER_27_XP = 45_000_000; public int TIER_28_XP = 47_000_000; public int TIER_29_XP = 48_500_000; public int TIER_30_XP = 50_000_000;


    /*
     * end tiers
     */
    public BattlePass(Player player) {
        this.player = player;
    }
    public static void small_npcs_bp_experience(Player player) {
        if (player.boughtBP == false) {
            return;
        }
        int xp = 70 + Misc.random(125);
        player.currentTierXP+=xp;
    }
    public static void medium_npcs_bp_experience(Player player) {
        if (player.boughtBP == false) {
            return;
        }
        int xp = 125 + Misc.random(180);
        player.currentTierXP+=xp;
    }
    public static  void high_npcs_bp_experience(Player player) {
        if (player.boughtBP == false) {
            return;
        }
        int xp = 200 + Misc.random(200);
        player.currentTierXP+=xp;
    }
    public boolean clickButton(int buttonId) {
        switch (buttonId) {
            case 173114:
                player.getPA().closeAllWindows();
                break;
            case 173125:
                if (player.battlePassSeason != currentSeason) {
                    return true;
                }
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 1) {
                    if (player.currentTier != 1) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_1_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_1, player.getBattlePass().TIER_1_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 11) {
                    if (player.currentTier != 11) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_11_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_11, player.getBattlePass().TIER_11_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 21) {
                    if (player.currentTier != 21) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_21_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_21, player.getBattlePass().TIER_21_AMOUNT);
                    player.currentTier++;
                }
                break;

            case 173126:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 2) {
                    if (player.currentTier != 2) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_2_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_2, player.getBattlePass().TIER_2_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 12) {
                    if (player.currentTier != 12) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_12_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_12, player.getBattlePass().TIER_12_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 22) {
                    if (player.currentTier != 22) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_22_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_22, player.getBattlePass().TIER_22_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173127:
                if (player.getItems().freeSlots() < 1) { 
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 3) {
                    if (player.currentTier != 3) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_3_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_3, player.getBattlePass().TIER_3_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 13) {
                    if (player.currentTier != 13) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_13_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_13, player.getBattlePass().TIER_13_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 23) {
                    if (player.currentTier != 23) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_23_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_23, player.getBattlePass().TIER_23_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173128:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 4) {
                    if (player.currentTier != 4) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_4_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_4, player.getBattlePass().TIER_4_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 14) {
                    if (player.currentTier != 14) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_14_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_14, player.getBattlePass().TIER_14_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 24) {
                    if (player.currentTierXP < player.getBattlePass().TIER_24_XP) {
                        if (player.currentTier != 24) {
                            player.sendMessage("@red@You have already claimed this reward..");
                            return true;
                        }
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_24, player.getBattlePass().TIER_24_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173129:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 5) {
                    if (player.currentTier != 5) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_5_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_5, player.getBattlePass().TIER_5_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 15) {
                    if (player.currentTier != 15) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_15_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_15, player.getBattlePass().TIER_15_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 25) {
                    if (player.currentTier != 25) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_25_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_25, player.getBattlePass().TIER_25_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173130:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 6) {
                    if (player.currentTier != 6) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_6_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_6, player.getBattlePass().TIER_6_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 16) {
                    if (player.currentTier != 16) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_16_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_16, player.getBattlePass().TIER_16_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 26) {
                    if (player.currentTierXP < player.getBattlePass().TIER_26_XP) {
                        if (player.currentTier != 26) {
                            player.sendMessage("@red@You have already claimed this reward..");
                            return true;
                        }
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_26, player.getBattlePass().TIER_26_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173131:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 7) {
                    if (player.currentTier != 7) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_7_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_7, player.getBattlePass().TIER_7_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 17) {
                    if (player.currentTier != 17) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_17_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_17, player.getBattlePass().TIER_17_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 27) {
                    if (player.currentTier != 27) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_27_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_27, player.getBattlePass().TIER_27_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173132:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 8) {
                    if (player.currentTier != 8) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_8_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_8, player.getBattlePass().TIER_8_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 18) {
                    if (player.currentTier != 18) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_18_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_18, player.getBattlePass().TIER_18_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 28) {
                    if (player.currentTier != 28) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_28_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_28, player.getBattlePass().TIER_28_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173133:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 9) {
                    if (player.currentTier != 9) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_9_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_9, player.getBattlePass().TIER_9_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 2 && player.currentTier == 19) {
                    if (player.currentTier != 19) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_19_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_19, player.getBattlePass().TIER_19_AMOUNT);
                    player.currentTier++;
                } else if (player.battlePage == 3 && player.currentTier == 29) {
                    if (player.currentTier != 29) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_29_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_29, player.getBattlePass().TIER_29_AMOUNT);
                    player.currentTier++;
                }
                break;
            case 173134:
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("@red@You need more inventory room for this.");
                    return true;
                }
                if (player.boughtBP == false) {
                    player.sendMessage("@red@You have not yet purchased a battle pass ticket.");
                    player.getPA().closeAllWindows();
                    return true;
                }
                if (player.battlePage == 1 && player.currentTier == 10) {
                    if (player.currentTier != 10) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_10_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_10, player.getBattlePass().TIER_10_AMOUNT);
                    player.currentTier++;
                    player.battlePage++;
                    BattlePass.execute(player);
                } else if (player.battlePage == 2 && player.currentTier == 20) {
                    if (player.currentTier != 20) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_20_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_20, player.getBattlePass().TIER_20_AMOUNT);
                    player.currentTier++;
                    player.battlePage++;
                    BattlePass.execute(player);
                } else if (player.battlePage == 3 && player.currentTier == 30) {
                    if (player.currentTier == 31) {
                        player.sendMessage("@red@You have completed the battlepass!");
                        return true;
                    }
                    if (player.currentTier != 30) {
                        player.sendMessage("@red@You have already claimed this reward..");
                        return true;
                    }
                    if (player.currentTierXP < player.getBattlePass().TIER_30_XP) {
                        player.sendMessage("@red@You dont have enough tier experience to claim this reward.");
                        return true;
                    }
                    if (player.getItems().freeSlots() < 1) {
                        player.sendMessage("@red@You dont have enough inventory space for this item.");
                        return true;
                    }
                    player.getItems().addItem(player.getBattlePass().TIER_30, player.getBattlePass().TIER_30_AMOUNT);
                    player.currentTier++;
                    player.sendMessage("@red@You have completed the battlepass!.");
                    PlayerHandler.executeGlobalMessage("@red@ [BATTLEPASS] ["+player.getDisplayName()+"]@blu@ Has completed the battlepass!");
                    player.sendMessage("@red@For Completing the battlepass you get a free battlepass entry.");
                    player.getItems().addItem(player.getBattlePass().TICKET, 1);
                    player.boughtBP = false;
                    player.getPA().closeAllWindows();
                }
                player.getPA().sendFrame126("Current XP:@yel@ "+player.currentTierXP+"", 44406);
                player.sendMessage("@red@You have just claimed a battlepass item!");
                break;
        }
        return false;
    }
    public void purchaseBP(Player c) {
        if (c.boughtBP == true) {
            c.getDH().sendStatement("You have already activated the battlepass.");
            execute(player);
            return;
        }
        if (c.battlePassSeason != BattlePass.currentSeason) {
            c.getDH().sendStatement("The battlepass new battlepass not yet started.");
            return;
        }
        if (c.donatorPoints < 10) {
            c.getDH().sendStatement("You need 10 donator points to buy the battlepass.");
            return;
        }
        c.boughtBP = true;
//        c.donatorPoints -= 10;
//        c.amDonated+= 10;
        c.sendMessage("@red@You have activated the battlepass!");
        c.getPA().closeAllWindows();
    }
    public void skip5Tiers(Player player) {
        if (player.currentTier > 26) {
            player.sendMessage("You cant skip during this tier as it is to far.");
            return;
        }
        if (player.donatorPoints < 5) {
            player.sendMessage("@red@ You need 5 donator points to skip 5 tiers.");
            player.getPA().closeAllWindows();
            return;
        }
        if (player.getItems().freeSlots() < 5) {
            player.sendMessage("@red@ You need atleast 5 inventory spaces to skip 5 tiers.");
            player.getPA().closeAllWindows();
            return;
        }
        if (player.currentTier == 1) {
            player.currentTier = 5;
            player.currentTierXP = TIER_4_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_1, TIER_1_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_2, TIER_2_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_3, TIER_3_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_4, TIER_4_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_5, TIER_5_AMOUNT);
        } else if (player.currentTier == 2) {
            player.currentTier = 6;
            player.currentTierXP = TIER_5_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_2, TIER_2_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_3, TIER_3_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_4, TIER_4_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_5, TIER_5_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_6, TIER_6_AMOUNT);
        } else if (player.currentTier == 3) {
            player.currentTier = 7;
            player.currentTierXP = TIER_6_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_3, TIER_3_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_4, TIER_4_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_5, TIER_5_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_6, TIER_6_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_7, TIER_7_AMOUNT);
        } else if (player.currentTier == 4) {
            player.currentTier = 8;
            player.currentTierXP = TIER_7_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_4, TIER_4_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_5, TIER_5_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_6, TIER_6_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_7, TIER_7_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_8, TIER_8_AMOUNT);
        } else if (player.currentTier == 5) {
            player.currentTier = 9;
            player.currentTierXP = TIER_8_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_5, TIER_5_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_6, TIER_6_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_7, TIER_7_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_8, TIER_8_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_9, TIER_9_AMOUNT);
        } else if (player.currentTier == 6) {
            player.currentTier = 10;
            player.currentTierXP = TIER_9_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_6, TIER_6_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_7, TIER_7_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_8, TIER_8_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_9, TIER_9_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_10, TIER_10_AMOUNT);
        } else if (player.currentTier == 7) {
            player.currentTier = 11;
            player.currentTierXP = TIER_10_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_7, TIER_7_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_8, TIER_8_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_9, TIER_9_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_10, TIER_10_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_11, TIER_11_AMOUNT);
        } else if (player.currentTier == 8) {
            player.currentTier = 12;
            player.currentTierXP = TIER_11_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_8, TIER_8_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_9, TIER_9_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_10, TIER_10_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_11, TIER_11_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_12, TIER_12_AMOUNT);
        } else if (player.currentTier == 9) {
            player.currentTier = 13;
            player.currentTierXP = TIER_12_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_9, TIER_9_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_10, TIER_10_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_11, TIER_11_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_12, TIER_12_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_13, TIER_13_AMOUNT);
        } else if (player.currentTier == 10) {
            player.currentTier = 14;
            player.currentTierXP = TIER_13_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_10, TIER_10_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_11, TIER_11_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_12, TIER_12_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_13, TIER_13_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_14, TIER_14_AMOUNT);
        } else if (player.currentTier == 11) {
            player.currentTier = 15;
            player.currentTierXP = TIER_14_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_11, TIER_10_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_12, TIER_12_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_13, TIER_13_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_14, TIER_14_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_15, TIER_15_AMOUNT);
        } else if (player.currentTier == 12) {
            player.currentTier = 16;
            player.currentTierXP = TIER_15_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_12, TIER_12_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_13, TIER_13_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_14, TIER_14_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_15, TIER_15_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_16, TIER_16_AMOUNT);
        } else if (player.currentTier == 13) {
            player.currentTier = 17;
            player.currentTierXP = TIER_16_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_13, TIER_13_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_14, TIER_14_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_15, TIER_15_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_16, TIER_16_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_17, TIER_17_AMOUNT);
        } else if (player.currentTier == 14) {
            player.currentTier = 18;
            player.currentTierXP = TIER_17_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_14, TIER_14_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_15, TIER_15_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_16, TIER_16_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_17, TIER_17_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_18, TIER_18_AMOUNT);
        } else if (player.currentTier == 15) {
            player.currentTier = 19;
            player.currentTierXP = TIER_18_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_15, TIER_15_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_16, TIER_16_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_17, TIER_17_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_18, TIER_18_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_19, TIER_19_AMOUNT);
        } else if (player.currentTier == 16) {
            player.currentTier = 20;
            player.currentTierXP = TIER_19_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_16, TIER_16_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_17, TIER_17_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_18, TIER_18_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_19, TIER_19_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_20, TIER_20_AMOUNT);
        } else if (player.currentTier == 17) {
            player.currentTier = 21;
            player.currentTierXP = TIER_20_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_17, TIER_17_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_18, TIER_18_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_19, TIER_19_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_20, TIER_20_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_21, TIER_21_AMOUNT);
        } else if (player.currentTier == 18) {
            player.currentTier = 22;
            player.currentTierXP = TIER_21_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_18, TIER_18_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_19, TIER_19_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_20, TIER_20_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_21, TIER_21_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_22, TIER_22_AMOUNT);
        } else if (player.currentTier == 19) {
            player.currentTier = 23;
            player.currentTierXP = TIER_22_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_19, TIER_19_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_20, TIER_20_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_21, TIER_21_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_22, TIER_22_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_23, TIER_23_AMOUNT);
        } else if (player.currentTier == 20) {
            player.currentTier = 24;
            player.currentTierXP = TIER_23_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_20, TIER_20_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_21, TIER_21_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_22, TIER_22_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_23, TIER_23_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_24, TIER_24_AMOUNT);
        } else if (player.currentTier == 21) {
            player.currentTier = 25;
            player.currentTierXP = TIER_24_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_21, TIER_21_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_22, TIER_22_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_23, TIER_23_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_24, TIER_24_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_25, TIER_25_AMOUNT);
        } else if (player.currentTier == 22) {
            player.currentTier = 26;
            player.currentTierXP = TIER_25_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_22, TIER_22_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_23, TIER_23_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_24, TIER_24_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_25, TIER_25_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_26, TIER_26_AMOUNT);
        } else if (player.currentTier == 23) {
            player.currentTier = 27;
            player.currentTierXP = TIER_26_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_23, TIER_23_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_24, TIER_24_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_25, TIER_25_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_26, TIER_26_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_27, TIER_27_AMOUNT);
        } else if (player.currentTier == 24) {
            player.currentTier = 28;
            player.currentTierXP = TIER_27_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_24, TIER_24_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_25, TIER_25_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_26, TIER_26_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_27, TIER_27_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_28, TIER_28_AMOUNT);
        } else if (player.currentTier == 25) {
            player.currentTier = 29;
            player.currentTierXP = TIER_28_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_25, TIER_25_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_26, TIER_26_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_27, TIER_27_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_28, TIER_28_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_29, TIER_29_AMOUNT);
        } else if (player.currentTier == 26) {
            player.currentTier = 30;
            player.currentTierXP = TIER_29_XP;
            player.getItems().addItemUnderAnyCircumstance(TIER_26, TIER_26_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_27, TIER_27_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_28, TIER_28_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_29, TIER_29_AMOUNT);
            player.getItems().addItemUnderAnyCircumstance(TIER_30, TIER_30_AMOUNT);
        }
        player.donatorPoints-= 5;
        player.amDonated+=5;
        player.sendMessage("@blu@You have skipped @red@5@blu@ tiers and are now on @red@"+player.currentTier+"@blu@ tier level. ");
        execute(player);

    }
    public int checkDate() {
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JANUARY) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.FEBRUARY) {
            return 28 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.MARCH) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL) {
            return 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL) {
            return 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.MAY) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE) {
            return 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JULY) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.AUGUST) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.SEPTEMBER) {
            return 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.NOVEMBER) {
            return 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER) {
            return 31 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }
    public void loginAlert (Player player) {
        if (player.boughtBP == false) {
            return;
        }
        int tiersLeft = 30 - player.currentTier;
        checkAlerts(player);
        player.sendMessage("@blu@[BATTLE-PASS] @red@You have got @blu@"+tiersLeft+"@red@ tiers left to complete.");
        player.sendMessage("@blu@[BATTLE-PASS] @red@There are now @blu@"+checkDate()+"@red@ days left to complete the battlepass");
    }
    public void checkAlerts(Player player) {
        if (player.boughtBP == true && player.currentTier == 1 && player.currentTierXP > player.getBattlePass().TIER_1_XP
                || player.currentTier == 2 && player.currentTierXP > player.getBattlePass().TIER_2_XP
                || player.currentTier == 3 && player.currentTierXP > player.getBattlePass().TIER_3_XP
                || player.currentTier == 4 && player.currentTierXP > player.getBattlePass().TIER_4_XP
                || player.currentTier == 5 && player.currentTierXP > player.getBattlePass().TIER_5_XP
                || player.currentTier == 6 && player.currentTierXP > player.getBattlePass().TIER_6_XP
                || player.currentTier == 7 && player.currentTierXP > player.getBattlePass().TIER_7_XP
                || player.currentTier == 8 && player.currentTierXP > player.getBattlePass().TIER_8_XP
                || player.currentTier == 9 && player.currentTierXP > player.getBattlePass().TIER_9_XP
                || player.currentTier == 10 && player.currentTierXP > player.getBattlePass().TIER_10_XP
                || player.currentTier == 11 && player.currentTierXP > player.getBattlePass().TIER_11_XP
                || player.currentTier == 12 && player.currentTierXP > player.getBattlePass().TIER_12_XP
                || player.currentTier == 13 && player.currentTierXP > player.getBattlePass().TIER_13_XP
                || player.currentTier == 14 && player.currentTierXP > player.getBattlePass().TIER_14_XP
                || player.currentTier == 15 && player.currentTierXP > player.getBattlePass().TIER_15_XP
                || player.currentTier == 16 && player.currentTierXP > player.getBattlePass().TIER_16_XP
                || player.currentTier == 17 && player.currentTierXP > player.getBattlePass().TIER_17_XP
                || player.currentTier == 18 && player.currentTierXP > player.getBattlePass().TIER_18_XP
                || player.currentTier == 19 && player.currentTierXP > player.getBattlePass().TIER_19_XP
                || player.currentTier == 20 && player.currentTierXP > player.getBattlePass().TIER_20_XP
                || player.currentTier == 21 && player.currentTierXP > player.getBattlePass().TIER_21_XP
                || player.currentTier == 22 && player.currentTierXP > player.getBattlePass().TIER_22_XP
                || player.currentTier == 23 && player.currentTierXP > player.getBattlePass().TIER_23_XP
                || player.currentTier == 24 && player.currentTierXP > player.getBattlePass().TIER_24_XP
                || player.currentTier == 25 && player.currentTierXP > player.getBattlePass().TIER_25_XP
                || player.currentTier == 26 && player.currentTierXP > player.getBattlePass().TIER_26_XP
                || player.currentTier == 27 && player.currentTierXP > player.getBattlePass().TIER_27_XP
                || player.currentTier == 28 && player.currentTierXP > player.getBattlePass().TIER_28_XP
                || player.currentTier == 29 && player.currentTierXP > player.getBattlePass().TIER_29_XP) {
            player.sendMessage("@blu@[BATTLE-PASS] @red@You have completed tier "+player.currentTier+", you can now collect your reward.");
        } else if (player.boughtBP == true && player.currentTier == 30 && player.currentTierXP > player.getBattlePass().TIER_30_XP) {
            player.sendMessage("@blu@[BATTLE-PASS] @red@You have completed the battlepass, collect ur last tier!");
        }
    }
    public static void execute(Player player) {
        if (player.battlePassSeason != BattlePass.currentSeason) {
            player.sendMessage("@red@The battlepass has not yet started or has been completed this month.");
            return;
        }
        if (player.currentTier == 31) {
            player.sendMessage("@red@You have completed the battlepass!");
            return;
        }
        player.getPA().sendFrame126("BattlePass", 44404);
        player.getPA().sendFrame126("@or1@Current Tier:@yel@ "+player.currentTier+"", 44405);
        player.getPA().sendFrame126("Current XP:@yel@ "+player.currentTierXP+"", 44406);

        if (player.currentTier < 11) {
            player.battlePage = 1;
        }
        if (player.currentTier > 10) {
            player.battlePage = 2;
        }
        if (player.currentTier > 20) {
            player.battlePage = 3;
        }
        if (player.currentTier == 1) {
            int xp_needed = player.getBattlePass().TIER_1_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 2) {
            int xp_needed = player.getBattlePass().TIER_2_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 3) {
            int xp_needed = player.getBattlePass().TIER_3_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 4) {
            int xp_needed = player.getBattlePass().TIER_4_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 5) {
            int xp_needed = player.getBattlePass().TIER_5_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 6) {
            int xp_needed = player.getBattlePass().TIER_6_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 7) {
            int xp_needed = player.getBattlePass().TIER_7_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 8) {
            int xp_needed = player.getBattlePass().TIER_8_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 9) {
            int xp_needed = player.getBattlePass().TIER_9_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 10) {
            int xp_needed = player.getBattlePass().TIER_10_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 11) {
            int xp_needed = player.getBattlePass().TIER_11_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 12) {
            int xp_needed = player.getBattlePass().TIER_12_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 13) {
            int xp_needed = player.getBattlePass().TIER_13_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 104) {
            int xp_needed = player.getBattlePass().TIER_14_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 15) {
            int xp_needed = player.getBattlePass().TIER_15_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 16) {
            int xp_needed = player.getBattlePass().TIER_16_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 17) {
            int xp_needed = player.getBattlePass().TIER_17_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 18) {
            int xp_needed = player.getBattlePass().TIER_18_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 19) {
            int xp_needed = player.getBattlePass().TIER_19_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 20) {
            int xp_needed = player.getBattlePass().TIER_20_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 21) {
            int xp_needed = player.getBattlePass().TIER_21_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 22) {
            int xp_needed = player.getBattlePass().TIER_22_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 23) {
            int xp_needed = player.getBattlePass().TIER_23_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 24) {
            int xp_needed = player.getBattlePass().TIER_24_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 25) {
            int xp_needed = player.getBattlePass().TIER_25_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 26) {
            int xp_needed = player.getBattlePass().TIER_26_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 27) {
            int xp_needed = player.getBattlePass().TIER_27_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 28) {
            int xp_needed = player.getBattlePass().TIER_28_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 29) {
            int xp_needed = player.getBattlePass().TIER_29_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        } else if (player.currentTier == 30) {
            int xp_needed = player.getBattlePass().TIER_30_XP - player.currentTierXP;
            player.getPA().sendFrame126("XP To Next Level:@yel@ "+xp_needed+"XP", 44407);
        }

        if (player.battlePage == 1) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_1, player.getBattlePass().TIER_1_AMOUNT, 44441, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_2, player.getBattlePass().TIER_2_AMOUNT, 44441, 1);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_3, player.getBattlePass().TIER_3_AMOUNT, 44441, 2);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_4, player.getBattlePass().TIER_4_AMOUNT, 44441, 3);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_5, player.getBattlePass().TIER_5_AMOUNT, 44441, 4);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_6, player.getBattlePass().TIER_6_AMOUNT, 44441, 5);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_7, player.getBattlePass().TIER_7_AMOUNT, 44441, 6);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_8, player.getBattlePass().TIER_8_AMOUNT, 44441, 7);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_9, player.getBattlePass().TIER_9_AMOUNT, 44441, 8);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_10, player.getBattlePass().TIER_10_AMOUNT, 44441, 9);
        } else if (player.battlePage == 2) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_11, player.getBattlePass().TIER_11_AMOUNT, 44441, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_12, player.getBattlePass().TIER_12_AMOUNT, 44441, 1);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_13, player.getBattlePass().TIER_13_AMOUNT, 44441, 2);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_14, player.getBattlePass().TIER_14_AMOUNT, 44441, 3);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_15, player.getBattlePass().TIER_15_AMOUNT, 44441, 4);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_16, player.getBattlePass().TIER_16_AMOUNT, 44441, 5);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_17, player.getBattlePass().TIER_17_AMOUNT, 44441, 6);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_18, player.getBattlePass().TIER_18_AMOUNT, 44441, 7);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_19, player.getBattlePass().TIER_19_AMOUNT, 44441, 8);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_20, player.getBattlePass().TIER_20_AMOUNT, 44441, 9);
        } else if (player.battlePage == 3) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_21, player.getBattlePass().TIER_21_AMOUNT, 44441, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_22, player.getBattlePass().TIER_22_AMOUNT, 44441, 1);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_23, player.getBattlePass().TIER_23_AMOUNT, 44441, 2);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_24, player.getBattlePass().TIER_24_AMOUNT, 44441, 3);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_25, player.getBattlePass().TIER_25_AMOUNT, 44441, 4);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_26, player.getBattlePass().TIER_26_AMOUNT, 44441, 5);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_27, player.getBattlePass().TIER_27_AMOUNT, 44441, 6);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_28, player.getBattlePass().TIER_28_AMOUNT, 44441, 7);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_29, player.getBattlePass().TIER_29_AMOUNT, 44441, 8);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_30, player.getBattlePass().TIER_30_AMOUNT, 44441, 9);
        }

        if (player.currentTier == 1) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_1, player.getBattlePass().TIER_1_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_2, player.getBattlePass().TIER_2_AMOUNT, 44462, 0);
        } else if (player.currentTier == 2) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_2, player.getBattlePass().TIER_2_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_3, player.getBattlePass().TIER_3_AMOUNT, 44462, 0);
        } else if (player.currentTier == 3) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_3, player.getBattlePass().TIER_3_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_4, player.getBattlePass().TIER_4_AMOUNT, 44462, 0);
        } else if (player.currentTier == 4) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_4, player.getBattlePass().TIER_4_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_5, player.getBattlePass().TIER_5_AMOUNT, 44462, 0);
        } else if (player.currentTier == 5) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_5, player.getBattlePass().TIER_5_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_6, player.getBattlePass().TIER_6_AMOUNT, 44462, 0);
        } else if (player.currentTier == 6) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_6, player.getBattlePass().TIER_6_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_7, player.getBattlePass().TIER_7_AMOUNT, 44462, 0);
        } else if (player.currentTier == 7) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_7, player.getBattlePass().TIER_7_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_8, player.getBattlePass().TIER_8_AMOUNT, 44462, 0);
        } else if (player.currentTier == 8) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_8, player.getBattlePass().TIER_8_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_9, player.getBattlePass().TIER_9_AMOUNT, 44462, 0);
        } else if (player.currentTier == 9) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_9, player.getBattlePass().TIER_9_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_10, player.getBattlePass().TIER_10_AMOUNT, 44462, 0);
        } else if (player.currentTier == 10) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_10, player.getBattlePass().TIER_10_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_11, player.getBattlePass().TIER_11_AMOUNT, 44462, 0);
            //page 2
        } else if (player.currentTier == 11) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_11, player.getBattlePass().TIER_11_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_12, player.getBattlePass().TIER_12_AMOUNT, 44462, 0);
        } else if (player.currentTier == 12) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_12, player.getBattlePass().TIER_12_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_13, player.getBattlePass().TIER_13_AMOUNT, 44462, 0);
        } else if (player.currentTier == 13) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_13, player.getBattlePass().TIER_13_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_14, player.getBattlePass().TIER_14_AMOUNT, 44462, 0);
        } else if (player.currentTier == 14) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_14, player.getBattlePass().TIER_14_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_15, player.getBattlePass().TIER_15_AMOUNT, 44462, 0);
        } else if (player.currentTier == 15) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_15, player.getBattlePass().TIER_15_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_16, player.getBattlePass().TIER_16_AMOUNT, 44462, 0);
        } else if (player.currentTier == 16) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_16, player.getBattlePass().TIER_16_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_17, player.getBattlePass().TIER_17_AMOUNT, 44462, 0);
        } else if (player.currentTier == 17) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_17, player.getBattlePass().TIER_17_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_18, player.getBattlePass().TIER_18_AMOUNT, 44462, 0);
        } else if (player.currentTier == 18) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_18, player.getBattlePass().TIER_18_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_19, player.getBattlePass().TIER_19_AMOUNT, 44462, 0);
        } else if (player.currentTier == 19) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_19, player.getBattlePass().TIER_19_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_20, player.getBattlePass().TIER_20_AMOUNT, 44462, 0);
        } else if (player.currentTier == 20) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_20, player.getBattlePass().TIER_20_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_21, player.getBattlePass().TIER_21_AMOUNT, 44462, 0);
            //page 3
        } else if (player.currentTier == 11) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_21, player.getBattlePass().TIER_21_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_22, player.getBattlePass().TIER_22_AMOUNT, 44462, 0);
        } else if (player.currentTier == 12) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_22, player.getBattlePass().TIER_22_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_23, player.getBattlePass().TIER_23_AMOUNT, 44462, 0);
        } else if (player.currentTier == 13) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_23, player.getBattlePass().TIER_23_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_24, player.getBattlePass().TIER_24_AMOUNT, 44462, 0);
        } else if (player.currentTier == 14) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_24, player.getBattlePass().TIER_24_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_25, player.getBattlePass().TIER_25_AMOUNT, 44462, 0);
        } else if (player.currentTier == 15) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_25, player.getBattlePass().TIER_25_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_26, player.getBattlePass().TIER_26_AMOUNT, 44462, 0);
        } else if (player.currentTier == 16) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_26, player.getBattlePass().TIER_26_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_27, player.getBattlePass().TIER_27_AMOUNT, 44462, 0);
        } else if (player.currentTier == 17) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_27, player.getBattlePass().TIER_27_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_28, player.getBattlePass().TIER_28_AMOUNT, 44462, 0);
        } else if (player.currentTier == 18) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_28, player.getBattlePass().TIER_28_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_29, player.getBattlePass().TIER_29_AMOUNT, 44462, 0);
        } else if (player.currentTier == 19) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_29, player.getBattlePass().TIER_29_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TIER_30, player.getBattlePass().TIER_30_AMOUNT, 44462, 0);
        } else if (player.currentTier == 20) {
            player.getPA().itemOnInterface(player.getBattlePass().TIER_30, player.getBattlePass().TIER_30_AMOUNT, 44461, 0);
            player.getPA().itemOnInterface(player.getBattlePass().TICKET, 1, 44462, 0);
        }
        player.getPA().showInterface(44400);
    }
}
