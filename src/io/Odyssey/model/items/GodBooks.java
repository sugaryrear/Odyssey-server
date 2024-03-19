package io.Odyssey.model.items;

import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.model.entity.player.Player;

import java.util.Arrays;


public enum GodBooks {
    SARADOMIN(3839, new int[]{3827,3828,3829,3830}, 3840),
    GUTHIX(3843, new int[]{3835,3836,3837,3838}, 3844),
    ZAMORAK(3841, new int[]{3831,3832,3833,3834}, 3842),
    ;

    private final int damagedbookId;
    private final int[] pages;

    private final int godbook;

    public int getDamagedbookId() {
        return damagedbookId;
    }


    public int getgodBook() {
        return godbook;
    }

    public int[] getPages() {
        return pages;
    }

    GodBooks(int damagedbookId, int[] pages, int godbook) {
        this.damagedbookId = damagedbookId;
        this.pages = pages;
        this.godbook = godbook;
    }


    public static GodBooks forUsedItems(int item1, int item2) {

        return Arrays.stream(GodBooks.values()).filter(it -> it.getDamagedbookId() == item2).findFirst().orElse(null);
    }
    public static GodBooks isagodbook(int item1) {
        return Arrays.stream(GodBooks.values()).filter(it -> it.getDamagedbookId() == item1).findFirst().orElse(null);
    }
    public static boolean mix(Player player, int item1, int item2) {

        GodBooks combination = forUsedItems(item1, item2);
        if (combination == null)
            return false;
        int pagetoadd = -1;
        int pageindex =-1;
        //check if the used item is a god book page
        for (int page = 0; page < combination.getPages().length; page++) {
            if (item1 == combination.getPages()[page]) {
                pageindex = page;
                pagetoadd = combination.getPages()[page];
            }
        }

        if(pagetoadd == -1)
            return false;

        if(item2 == 3839){
            for (int page = 0; page < player.holybook.length; page++) {
                if (pagetoadd == player.holybook[page]) {//so we have a page already added
                    player.sendMessage("That page is already in your book.");
                    //   System.out.println("page: "+combination.getPages()[page]);
                } else {

                    int finalPagetoadd = pagetoadd;
                    int finalPageindex = pageindex;
                    new DialogueBuilder(player).option("Add page into book?", new DialogueOption("Yes", plr -> complete(player, finalPagetoadd, finalPageindex,item2)),
                            DialogueOption.nevermind()
                    ).send();
                }
            }
        }
        if(item2 == 3843){
            for (int page = 0; page < player.balancebook.length; page++) {
                if (pagetoadd == player.balancebook[page]) {//so we have a page already added
                    player.sendMessage("That page is already in your book.");
                    //    System.out.println("page: "+combination.getPages()[page]);
                } else {

                    int finalPagetoadd = pagetoadd;
                    int finalPageindex = pageindex;
                    new DialogueBuilder(player).option("Add page into book?", new DialogueOption("Yes", plr -> complete(player, finalPagetoadd, finalPageindex,item2)),
                            DialogueOption.nevermind()
                    ).send();
                }
            }
        }
        if(item2 == 3841){
            for (int page = 0; page < player.unholybook.length; page++) {
                if (pagetoadd == player.unholybook[page]) {//so we have a page already added
                    player.sendMessage("That page is already in your book.");
                    //    System.out.println("page: "+combination.getPages()[page]);
                } else {

                    int finalPagetoadd = pagetoadd;
                    int finalPageindex = pageindex;
                    new DialogueBuilder(player).option("Add page into book?", new DialogueOption("Yes", plr -> complete(player, finalPagetoadd, finalPageindex,item2)),
                            DialogueOption.nevermind()
                    ).send();
                }
            }
        }
        return true;
    }

    public static void complete(Player player, int pagetoadd, int pageindex, int book) {
        if (player.getItems().playerHasItem(pagetoadd, 1)) {
            player.getPA().closeAllWindows();
            player.nextChat = -1;
            player.getItems().deleteItem(pagetoadd, 1);
            String bookname = "";

            if(book == 3839){
                player.holybook[pageindex] = pagetoadd;
                bookname = "Holy";
                player.sendMessage("@blu@You add the page to the "+bookname+" book.");
                //check if we completed the book by adding this page
                boolean didwefill = true;
                for (int page = 0; page < player.holybook.length; page++) {
                    if(player.holybook[page] == 0){
                        didwefill = false;
                    }
                }
                if(didwefill){
                    player.getItems().deleteItem(book, 1);
                    player.getDH().sendItemStatement("You completed the "+bookname+" book", 3840);
                    player.nextChat = -1;
                    player.getItems().addItem(3840, 1);
                }
            }
            if(book == 3843) {
                player.balancebook[pageindex] = pagetoadd;
                bookname = "Balance";
                player.sendMessage("@blu@You add the page to the "+bookname+" book.");
                //check if we completed the book by adding this page
                boolean didwefill = true;
                for (int page = 0; page < player.balancebook.length; page++) {
                    if(player.balancebook[page] == 0){
                        didwefill = false;
                    }
                }
                if(didwefill){
                    player.getItems().deleteItem(book, 1);
                    player.getDH().sendItemStatement("You completed the "+bookname+" book", 3844);
                    player.nextChat = -1;
                    player.getItems().addItem(3844, 1);
                }
            }
            if(book == 3841) {
                player.unholybook[pageindex] = pagetoadd;
                bookname = "Zamorak";
                player.sendMessage("@blu@You add the page to the "+bookname+" book.");
                //check if we completed the book by adding this page
                boolean didwefill = true;

                for (int page = 0; page < player.unholybook.length; page++) {
                    if(player.unholybook[page] == 0){
                        didwefill = false;
                    }
                }
                if(didwefill){
                    player.getItems().deleteItem(book, 1);
                    player.getDH().sendItemStatement("You completed the "+bookname+" book", 3842);
                    player.nextChat = -1;
                    player.getItems().addItem(3842, 1);
                }
            }

        }
    }
    public static boolean check(Player c, int item) {
        GodBooks combination = isagodbook(item);
        if (combination == null)
            return false;
        if(combination != null){
            String godbook = "";
            String line1 = "";
            String line2 = "";
            String line3 = "";
            String line4 = "";
            //holy book
            if(combination.getDamagedbookId() == 3839){
                godbook = "Saradomin";
                line1 = c.holybook[0] == 0 ? "Empty": godbook+" page 1";
                line2 = c.holybook[1] == 0 ? "Empty": godbook+" page 2";
                line3 = c.holybook[2] == 0 ? "Empty": godbook+" page 3";
                line4 = c.holybook[3] == 0 ? "Empty": godbook+" page 4";
            }
            if(combination.getDamagedbookId() == 3843){
                godbook = "Guthix";
                line1 = c.balancebook[0] == 0 ? "Empty": godbook+" page 1";
                line2 = c.balancebook[1] == 0 ? "Empty": godbook+" page 2";
                line3 = c.balancebook[2] == 0 ? "Empty": godbook+" page 3";
                line4 = c.balancebook[3] == 0 ? "Empty": godbook+" page 4";
            }
            if(combination.getDamagedbookId() == 3841){
                godbook = "Zamorak";
                line1 = c.unholybook[0] == 0 ? "Empty": godbook+" page 1";
                line2 = c.unholybook[1] == 0 ? "Empty": godbook+" page 2";
                line3 = c.unholybook[2] == 0 ? "Empty": godbook+" page 3";
                line4 = c.unholybook[3] == 0 ? "Empty": godbook+" page 4";
            }
            c.getDH().sendStatement(line1,line2,line3,line4);
            return true;
        }

        //return true;
        return false;
    }
}