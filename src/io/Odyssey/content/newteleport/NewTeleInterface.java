package io.Odyssey.content.newteleport;

import io.Odyssey.content.tutorial.TutorialDialogue;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewTeleInterface {


    private final Player player;

    public NewTeleInterface(Player player) {
        this.player = player;
    }

public static String trainingteleporticons = "<icon=134><icon=135><icon=136>";
    public static final int FAVORITES_TAB = 0;
    public static final int TRAINING = 1;
    static final int SLAYING = 2;
    public static final int BOSSING = 3;
    public static final int SKILLING = 4;
    public static final int MINIGAMES = 5;
    public static final int WILDERNESS = 6;
    public static final int CITIES = 7;
    public static final int MISCELLANEOUS = 8;

    private final NewTeleData[] VALUES = NewTeleData.values();

    public ArrayList<SpecificTeleport> thespecificteleport = new ArrayList<>();


    public void confirmdialog(SpecificTeleport thespecificteleport) {
        player.specificteleport = thespecificteleport;
        //player.getDH().sendDialogues(13100, 1503);

        //add tele timer here
        //check if u need to unlock it..eh..fk it jsut fking put it here till get time to enumerate it ffs godam its dirty ik ik
        //System.out.println("the word: "+thespecificteleport.description);


 //    boolean dowecheckthistele =

//System.out.println("r: "+thespecificteleport.regionunlock);
        //if
        if(!player.unlockedallteles){
            if(thespecificteleport.regionunlock != -1){
                if(!player.regionsunlocked.containsKey(thespecificteleport.regionunlock)){
                    player.sendMessage("You must discover "+ Color.DARK_GREEN.wrap(Region.regionnames.get(thespecificteleport.regionunlock))+" to use this teleport.");
                    return;
                }
            }
        }


        player.getPA().startTeleport(player.specificteleport.tile.getX(), player.specificteleport.tile.getY(),  player.specificteleport.tile.getHeight(), "modern", false);


    }
    int teleportbutton = 88101;
    int textbutton = 88101 + 30 ;
    int descriptionbutton = 88101 + 30 + 30;
    int favoritebutton = 88101 + 30 + 30 + 30;
    public void hidetheindividualteleports() {
        thespecificteleport.clear();
        for(int i = 0 ; i < 30; i ++){
            player.getPA().sendInterfaceComponentMoval(teleportbutton + i,-900, 0);
            player.getPA().sendInterfaceComponentMoval(textbutton+i,-900, 0);
            player.getPA().sendInterfaceComponentMoval(descriptionbutton+i,-900, 0);
            player.getPA().sendInterfaceComponentMoval(favoritebutton+i,-900, 0);

        }
    }

    public int category = 0;
    public void displaythecategories(List<NewTeleData> listofthespecificteleports){
        category = listofthespecificteleports.get(0).category;
        for(int i = 0 ; i < listofthespecificteleports.size(); i++){
            NewTeleData thespecificteleportdata = listofthespecificteleports.get(i);
            boolean favorited = false;
            for(SpecificTeleport tele : player.getnewfavs()){
                if(thespecificteleportdata.text.equalsIgnoreCase(tele.text))
                    favorited =true;
            }
            thespecificteleport.add(new SpecificTeleport(teleportbutton + i, thespecificteleportdata.tile,thespecificteleportdata.text, thespecificteleportdata.description, favorited, favoritebutton+i,thespecificteleportdata.potentialunlockedregion));
          //  System.out.println(i+" fav: "+favoritebutton+" and "+(favoritebutton+i));
        }

    }
    public List<NewTeleData> getalltasksbasedoncategory(int category) {
        List<NewTeleData> tasks = Arrays.stream(VALUES).filter(task -> task.category == category).collect(Collectors.toList());

        return tasks;
    }

    public void open(){
        if(TutorialDialogue.inTutorial(player)){
            TutorialDialogue.finishtut(player);
        }
        player.sendMessage("sendfavorites##");
        drawInterface(88005);

        player.getPA().showInterface(88000);
    }

    public void drawInterface(int button){
        hidetheindividualteleports();

        List<NewTeleData> specificcategory;
        List<SpecificTeleport> allthefavoriteteleportsyouhavesaved;


        if (button == 88005) {
            allthefavoriteteleportsyouhavesaved = player.getnewfavs();
            displaythefavorites(allthefavoriteteleportsyouhavesaved);
        } else {
            specificcategory = player.getnewteleInterface().getalltasksbasedoncategory(button-88005);
            displaythecategories(specificcategory);
        }
        for(int i = 0 ; i < thespecificteleport.size(); i ++){
            player.getPA().sendInterfaceComponentMoval(teleportbutton + i,0, 0);
            player.getPA().sendInterfaceComponentMoval(textbutton+i,0, 0);
            player.getPA().sendInterfaceComponentMoval(descriptionbutton+i,0, 0);
            player.getPA().sendInterfaceComponentMoval(favoritebutton+i,0, 0);

        }
        for(int i = 0 ; i < thespecificteleport.size(); i ++){
            SpecificTeleport data = thespecificteleport.get(i);
            player.getPA().sendString(textbutton+i, data.text);
            player.getPA().sendString(descriptionbutton+i, data.description);
            boolean favorited = data.favorited ? true : false;
            player.getPA().sendChangeSprite(data.favoritebutton, favorited ? (byte) 1 : (byte) 0 );
        }

        player.getPA().sendScrollbarHeight(88050, thespecificteleport.size() * 37);
    }


    //all it has to do is populate the array
    public void displaythefavorites(List<SpecificTeleport> listoffavorites){
        category = FAVORITES_TAB;
        for(int i = 0 ; i < listoffavorites.size(); i++){
            SpecificTeleport thespecificteleportdata = listoffavorites.get(i);
            boolean favorited = thespecificteleportdata.favorited ? true : false;
            thespecificteleport.add(new SpecificTeleport(teleportbutton + i, thespecificteleportdata.tile,thespecificteleportdata.text, thespecificteleportdata.description, favorited, favoritebutton+i,thespecificteleportdata.regionunlock));
        }



    }
}
