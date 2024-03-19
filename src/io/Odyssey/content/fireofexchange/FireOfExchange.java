package io.Odyssey.content.fireofexchange;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.leaderboards.LeaderboardType;
import io.Odyssey.content.leaderboards.LeaderboardUtils;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.util.Misc;
import io.Odyssey.util.discord.Discord;
import io.Odyssey.util.logging.player.FireOfExchangeLog;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.Odyssey.content.fireofexchange.FireOfExchangeBurnPrice.getBurnPrice;

public class FireOfExchange {

    public static final int FOE_SHOP_ID = 171;

    public static int TOTAL_POINTS_EXCHANGED;

    public static boolean canBurnWithBranch(Player c) {
        int currentItem = c.currentExchangeItem;
        boolean isFoeRewardItem = getExchangeShopPrice(currentItem) != Integer.MAX_VALUE;

        if (isFoeRewardItem) {
            c.sendMessage("You cannot burn this item with your ancient branch.");
            return false;
        }
        return true;
    }


    public static void exchangeItemForPoints(Player c) {
        if (Configuration.DISABLE_FOE) {
            c.sendMessage("Fire of Exchange has been temporarily disabled.");
            return;
        }
        c.objectYOffset = 5;
        c.objectXOffset = 5;
        c.objectDistance = 5;
        c.getQuesting().exchangeItemForPoints(c);
        if (c.currentExchangeItem == -1) {
            c.sendMessage("@red@You cannot exchange this item for Exchange Points.");
            return;
        }

        int exchangePrice = getBurnPrice(c, c.currentExchangeItem, true) * c.currentExchangeItemAmount;
        if (exchangePrice == -1) {
        	c.sendMessage("@red@You cannot exchange @blu@" + ItemAssistant.getItemName(c.currentExchangeItem) + " for @red@ Exchange Points.");
        	return;
		}

        if (!c.getItems().playerHasItem(c.currentExchangeItem)) {
            c.sendMessage("You no longer have this item on you.");
            return;
        }

        if (c.getMode().isIronmanType() && canBurnWithBranch(c)) {
                exchangePrice *= 1.10;
        }
        int itemAmount = c.currentExchangeItemAmount;
        c.getItems().deleteItem2(c.currentExchangeItem, itemAmount);
        c.exchangePoints += exchangePrice;
		TOTAL_POINTS_EXCHANGED += exchangePrice;
        List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
        DiscordMessager.sendLogs("[FOE] "+ c.getDisplayName() +" burned " + ItemAssistant.getItemName(c.currentExchangeItem)
                +  " x" + c.currentExchangeItemAmount + "");
        Discord.writeFoeMessage("[FOE] "+ c.getDisplayName() +" burned " + ItemAssistant.getItemName(c.currentExchangeItem)
                +  " x" + c.currentExchangeItemAmount + "");
		if (TOTAL_POINTS_EXCHANGED >= 100000) {
			PlayerHandler.executeGlobalMessage("@bla@[@red@FoE@bla@]@blu@ Another @red@100,000@blu@ exchange points has been consumed by the fire!");
			TOTAL_POINTS_EXCHANGED = 0;
		}

        c.sendMessage("The fire takes your @blu@" + ItemAssistant.getItemName(c.currentExchangeItem) + "@bla@ and gives back @blu@" + Misc.formatCoins(exchangePrice) + " Exchange Points!");
        if (canBurnWithBranch(c)) {
        c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, exchangePrice);
        //LeaderboardUtils.addCount(LeaderboardType.MOST_BURNED, c, exchangePrice);
        }
        if (c.currentExchangeItem != 691 &&
                c.currentExchangeItem != 692 &&
                c.currentExchangeItem != 693 &&
                c.currentExchangeItem != 696 &&
                c.currentExchangeItem != 2399 &&
                c.currentExchangeItem != 21046 &&
                c.currentExchangeItem != 8866 &&
                c.currentExchangeItem != 8868) {
            Achievements.increase(c, AchievementType.FOE_POINTS, exchangePrice);
            c.totalEarnedExchangePoints += exchangePrice;
            if (exchangePrice > 20000) {
                PlayerHandler.executeGlobalMessage("@bla@[@red@FoE@bla@] @blu@"+c.getDisplayNameFormatted()+" burned a " + ItemAssistant.getItemName(c.currentExchangeItem) + " x" + c.currentExchangeItemAmount +
                        "@blu@ for @red@" + Misc.formatCoins(exchangePrice) + " Exchange points.");

            }
        }
        Server.getLogging().write(new FireOfExchangeLog(c, new GameItem(c.currentExchangeItem, c.currentExchangeItemAmount)));
    }

    /**
     * Buying from shop price.
     */
    public static int getExchangeShopPrice(int id) {
        switch (id) {
            case 29014://postie pete
                return 2000000;
            case 29952://imp
                return 30000;
            case 29953://toucan
                return 30000;
            case 29954://penguin king
                return 35000;
            case 29955://k'klik
                return 150000;
            case 29956://melee pet
                return 75000;
            case 29957://range pet
                return 75000;
            case 29959://mage pet
                return 75000;
            case 29960://healer
                return 80000;
            case 29961://holy
                return 80000;
            case 29962://corrupt beast
                return 100000;
            case 29963://corrupt beast
                return 500000;
            case 25748://roc
                return 500000;
            case 25751://yama
                return 1500000;
            case 25749://yama
                return 1500000;
            case 23804://imbue dust
                return 15000;
            case 12783://row i scroll
                return 100000;
            case 21259://name change scroll
                return 35000;
            case 691://10k cert
                return 10000;
            case 692://25k cert
                return 25000;
            case 693://50k cert
                return 50000;
            case 696://250k cert
                return 250000;
            case 21046://chest rate increase
                return 850;
            case 8866://uim key
                return 250;
            case 8868://perm uim key
                return 10000;
            case 7629://double slayer points
                return 1250;
            case 24460://double clues
                return 2500;
            case 7968://pet rate increase
                return 4000;
            case Items.OVERLOAD_4://pet rate increase
                return 2000;
            case 1004://25m coins
                return 10000;
        }
        return Integer.MAX_VALUE;
    }


}
