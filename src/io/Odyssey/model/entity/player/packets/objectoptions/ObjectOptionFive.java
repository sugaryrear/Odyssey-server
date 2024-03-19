package io.Odyssey.model.entity.player.packets.objectoptions;

import io.Odyssey.Server;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public class ObjectOptionFive {

    public static void handleOption(final Player c, int objectType, int obX, int obY) {
        if (Server.getMultiplayerSessionListener().inAnySession(c)) {
            return;
        }
        c.clickObjectType = 0;
        if (c.debugMessage)
            c.sendMessage("Clicked Object Option 5:  "+objectType+"");

        switch (objectType) {
            case 31858:
                					if (c.playerLevel[18] < 60) {
c.sendMessage("You need a Slayer level of 60 to use the Arceeus spellbook");
return;
					}

            c.sendMessage("You switch to the arceeus spellbook.");
            c.setSidebarInterface(6, 23100);
            c.playerMagicBook = 3;
            break;
            case 44599://insta pay toll
            case 44598:
                if (!c.getItems().playerHasItem(995, 10)) {
                    c.sendMessage("I haven't got that much.");
                    c.nextChat = 0;
                } else {
                    c.walkthroughalkharidgate();
                }
                break;


            case 3831://dag kings peek
                int playersinkings = Boundary.getPlayersInBoundary(Boundary.DAGANNOTH_KINGS);
                if(playersinkings>0)
                    c.sendMessage("There are currently "+playersinkings+" players in the dagannoth kings lair.");
                break;
            case 12309://culinaromancer chest
                if(c.getQuesting().getQuestList().get(2).isQuestCompleted()){
                    c.getShops().openShop(157);
                }
                break;

        }
    }

}
