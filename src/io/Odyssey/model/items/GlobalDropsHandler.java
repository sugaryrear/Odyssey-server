package io.Odyssey.model.items;


import io.Odyssey.Server;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.util.Misc;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GlobalDropsHandler extends Event<Object> {
    private static final Logger logger = LogManager.getLogger(GlobalDropsHandler.class);
    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Misc.toCycles(1, TimeUnit.MINUTES);

    public static Optional<GlobalDrop> getGlobalItem(int id, int x, int y) {
        List<GlobalDrop> globalitems = globalDrops.stream().filter(finditem(id,x,y)).collect(Collectors.toList());
        if (globalitems.isEmpty()) {
            return Optional.empty();
        }

        return globalitems.stream().findFirst();
    }
    private static Predicate<GlobalDrop> finditem(int id, int x, int y) {
        return emblem -> emblem.getId() == id && emblem.getX() == x && emblem.getY() == y;
    }
    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public GlobalDropsHandler() {
        super("globaldrops", INTERVAL);
    }


    @Override
    public void execute() {

        for (GlobalDrop drop : globalDrops) {
            if (drop.isTaken()) {
            for (int i = 0; i < PlayerHandler.players.length; i++) {
                Player client = PlayerHandler.players[i];

                if (client != null) {

                    if (client.distanceToPoint(drop.getX(), drop.getY()) <= 60) {
                        //if (!Server.itemHandler.itemExists(client,drop.getId(), drop.getX(), drop.getY(), drop.getH())) {
                        //   Optional<GlobalDropsHand
                        //   ler.GlobalDrop> globalitem = GlobalDropsHandler.getGlobalItem(drop.getX(),drop.getY(),drop.getH());
                        //   if (drop.isTaken()) {
                        //  GroundItem item = Server.itemHandler.getGroundItem_globaldrop(client, drop.getId(), drop.getX(), drop.getY(), drop.getH());
                        //  if (item != null) {
                        if (!Server.itemHandler.itemExists_globaldrop(drop.getId(), drop.getX(),drop.getY(), drop.getH())) {

                            Server.itemHandler.createUnownedGroundItem_globaldrop(new GameItem(drop.getId(),drop.getAmount()), new Position(drop.getX(),drop.getY(),drop.getH()));
                            drop.setTaken(false);

                        } else {
                            // System.out.println("item exists?");
                        }


  }
                    }
                }
            }
        }
    }

    /**
     * Loads all the items when a player changes region
     */

    public static void load(Player client) {
        for(GlobalDrop drop : globalDrops) {
         if(!drop.isTaken()) {
            if(client.distanceToPoint(drop.getX(), drop.getY()) <= 60) {
                //if (!Server.itemHandler.itemExists(client,drop.getId(), drop.getX(), drop.getY(), drop.getH())) {//do we even need this
                //   Optional<GlobalDropsHandler.GlobalDrop> globalitem = GlobalDropsHandler.getGlobalItem(drop.getX(),drop.getY(),drop.getH());

                // GroundItem item = Server.itemHandler.getGroundItem_globaldrop(client, drop.getId(), drop.getX(), drop.getY(), drop.getH());
//                    if (item != null) {
                if (!Server.itemHandler.itemExists_globaldrop(drop.getId(), drop.getX(),drop.getY(), drop.getH())) {
                    // client.sendMessage("item isnt null.");
                    Server.itemHandler.createUnownedGroundItem_globaldrop(new GameItem(drop.getId(), drop.getAmount()), new Position(drop.getX(), drop.getY(), drop.getH()));

                    // return;
                }
            }
   }
        }
    }
    /**
     * holds all the global drops
     */
    public static List<GlobalDrop> globalDrops = new ArrayList<GlobalDrop>();



    /**
     * loads the items
     */
    public static void initialize() {


        String Data;
        BufferedReader Checker = null;
        try {
            Checker = new BufferedReader(new FileReader("etc/cfg/item/globaldrops.txt"));
            while ((Data = Checker.readLine()) != null) {
                if (Data.startsWith("#"))
                    continue;
                String[] args = Data.split(":");
                globalDrops.add(new GlobalDrop(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),  Integer.parseInt(args[4])));

            }
            Checker.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Loaded {globalDrops.size()} global drops." );

    }

    /**
     * Holds each drops data
     * @author Stuart
     *
     */
    public static class GlobalDrop {
        /**
         * cord x
         */
        int x;
        /**
         * cord y
         */
        int y;
        /**
         * item id
         */
        int h;
        int id;
        /**
         * item amount
         */
        int amount;
        /**
         * has the item been taken
         */
        boolean taken = false;
        /**
         * Time it was taken at
         */
        long takenAt;

        /**
         * Sets the drop arguments
         * @param a item id
         * @param b item amount
         * @param c cord x
         * @param d cord y
         */
        public GlobalDrop(int a, int b, int c, int d, int h) {
            this.id = a;
            this.amount = b;
            this.x = c;
            this.y = d;
            this.h = h;
        }

        /**
         * get cord x
         * @return
         */
        public int getX() {
            return this.x;
        }

        /**
         * get cord x
         * @return
         */
        public int getY() {
            return this.y;
        }

        /**
         * get cord h
         * @return
         */
        public int getH() {
            return this.h;
        }


        /**
         * get the item id
         * @return
         */
        public int getId() {
            return this.id;
        }

        /**
         * get the item amount
         * @return
         */
        public int getAmount() {
            return this.amount;
        }

        /**
         * has the drop already been taken?
         * @return
         */
        public boolean isTaken() {
            return this.taken;
        }

        /**
         * set if or not the drop has been taken
         * @param a true yes false no
         */
        public void setTaken(boolean a) {
            this.taken = a;
        }

        /**
         * set the time it was picked up
         * @param a
         */
        public void setTakenAt(long a) {
            this.takenAt = a;
        }

        /**
         * get the time it was taken at
         * @return
         */
        public long getTakenAt() {
            return this.takenAt;
        }

    }
}
