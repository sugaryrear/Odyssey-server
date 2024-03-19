package io.Odyssey.content;

import io.Odyssey.model.entity.player.Player;

public class NotificationsTab {

    private static final int INTERFACE_ID = 31_000;

    private final Player player;

    public NotificationsTab(Player player) {
        this.player = player;
    }

    private void openTab() {
        //        setSidebarInterface(11, 42500); // wrench tab
        //player.setSidebarInterface(11,INTERFACE_ID);
        player.getPA().sendTabAreaOverlayInterface(INTERFACE_ID);
    }

    public boolean clickButton(int buttonId) {
//        if (buttonId == 162070) {
//            openTab();
//        }
        if (buttonId == 12) {//close button

            player.setSidebarInterface(11, 42500); // wrench tab
        }
        return false;
    }

}
