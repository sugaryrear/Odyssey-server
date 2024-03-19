package io.Odyssey.model.world.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import io.Odyssey.Server;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.wilderness.SpiderWeb;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason MacKeigan
 * @date Dec 18, 2014, 12:14:09 AM
 */
public class GlobalObjects {

    private static final Logger logger = LoggerFactory.getLogger(GlobalObjects.class);

    /**
     * A collection of all existing objects
     */
    Queue<GlobalObject> objects = new LinkedList<>();

    /**
     * A collection of all objects to be removed from the game
     */
    Queue<GlobalObject> remove = new LinkedList<>();

    /**
     * Adds a new global object to the game world
     *
     * @param object the object being added
     */
    public void add(GlobalObject object) {
        updateObject(object, object.getObjectId());
        objects.add(object);
       // logger.debug("Add object {}", object);

        if (object.getRestoreId() > 0 && object.getTicksRemaining() > 0) {
            // assuming that this object needs to be removed - why would it try to remove restore id lol?
            if (object.getRestoreId() != -1) {
                object.getRegionProvider().get(object.getX(), object.getY()).removeObject(object.getRestoreId(), object.getX(), object.getY(), object.getHeight(), object.getType(), object.getFace());
                object.getRegionProvider().get(object.getX(), object.getY()).removeWorldObject(object.withId(object.getRestoreId()));
            }
        }
        if (object.getObjectId() == -1) {
            object.getRegionProvider().get(object.getX(), object.getY()).removeObject(object.getObjectId(), object.getX(), object.getY(), object.getHeight(), object.getType(), object.getFace());
            object.getRegionProvider().get(object.getX(), object.getY()).removeWorldObject(object);

        } else {
            object.getRegionProvider().get(object.getX(), object.getY()).addObject(object.getObjectId(), object.getX(), object.getY(), object.getHeight(), object.getType(), object.getFace());
            object.getRegionProvider().get(object.getX(), object.getY()).addWorldObject(object);

        }
    }
    public void add_door(GlobalObject object) {
        add(object);//add the object you clicked

        updateObject(object, -1);//makes it "removed" from the world
        objects.remove(object);//actually removes the object you added!
        // logger.debug("Add object {}", object);


        object.getRegionProvider().get(object.getX(), object.getY()).removeObject(object.getObjectId(), object.getX(), object.getY(), object.getHeight(), object.getType(), object.getFace());
        object.getRegionProvider().get(object.getX(), object.getY()).removeWorldObject(object);

    }
    public void remove(int id, int x, int y, int height) {
        remove(id, x, y, height, null);
    }

    /**
     * Removes a global object from the world. If the object is present in the game, we find the reference to that object and add it to the remove list.
     *
     * @param id     the identification value of the object
     * @param x      the x location of the object
     * @param y      the y location of the object
     * @param height the height of the object
     */
    public void remove(int id, int x, int y, int height, InstancedArea instance) {
        Optional<GlobalObject> existing = objects.stream().filter(o -> o.getObjectId() == id && o.getX() == x && o.getY() == y && o.getHeight() == height && o.getInstance() == instance).findFirst();
        if (existing.isPresent()) {
            remove(existing.get());
        } else {
           // System.out.println("it couldnt: "+id);
            logger.debug("Attempted to remove object but no object exists: id={}, x={}, y={}, height={}, instance={}", id, x, y, height, instance);
        }
    }

    public void remove(int id, InstancedArea instance) {
        List<GlobalObject> remove = objects.stream().filter(o -> o.getObjectId() == id && o.getInstance() == instance)
                .collect(Collectors.toList());
        remove.forEach(it -> {
            remove(it);
            logger.debug("Removed object id={}, instance={}", id, instance);
        });
    }

    /**
     * Removes a global object from the world based on object reference
     *
     * @param object the global object
     */
    public void remove(GlobalObject object) {
        if (!objects.contains(object)) {
         //  System.out.println("Trying to remove an object:  id: "+object.getObjectId()+" obj: x: "+object.getX()+"  y:"+object.getY()+" ");
            return;
        }
        updateObject(object, -1);
        remove.add(object);
        if (object.getObjectId() != -1) {
            object.getRegionProvider().get(object.getX(), object.getY()).removeObject(object.getObjectId(), object.getX(), object.getY(), object.getHeight(), object.getType(), object.getFace());
        }
    }

    public void replace(GlobalObject remove, GlobalObject add) {
        remove(remove);
        add(add);
        logger.debug("Replace {} with {}", remove, add);
    }

    /**
     * Determines if an object exists in the game world
     *
     * @param id     the identification value of the object
     * @param x      the x location of the object
     * @param y      the y location of the object
     * @param height the height location of the object
     * @return true if the object exists, otherwise false.
     */
    public boolean exists(int id, int x, int y, int height) {
        return objects.stream().anyMatch(object -> object.getObjectId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height);
    }

    public boolean exists(int id, int height) {
        return objects.stream().anyMatch(object -> object.getObjectId() == id && object.getHeight() == height);
    }

    /**
     * Determines if any object exists in the game world at the specified location
     *
     * @param x      the x location of the object
     * @param y      the y location of the object
     * @param height the height location of the object
     * @return true if the object exists, otherwise false.
     */
    public boolean anyExists(int x, int y, int height) {
        return objects.stream().anyMatch(object -> object.getX() == x && object.getY() == y && object.getHeight() == height);
    }

    /**
     * Determines if an object exists in the game world
     *
     * @param id the identification value of the object
     * @param x  the x location of the object
     * @param y  the y location of the object
     * @return true if the object exists, otherwise false.
     */
    public boolean exists(int id, int x, int y) {
        return exists(id, x, y, 0);
    }

    public GlobalObject get(int id, int x, int y, int height) {
        Optional<GlobalObject> obj = objects.stream().filter(object -> object.getObjectId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height)
                .findFirst();
        return obj.orElse(null);

    }

    /**
     * All global objects have a unique value associated with them that is referred to as ticks remaining. Every six hundred milliseconds each object has their amount of ticks
     * remaining reduced. Once an object has zero ticks remaining the object is replaced with it's counterpart. If an object has a tick remaining value that is negative, the object
     * is never removed unless indicated otherwise.
     */
    public void pulse() {
        if (objects.size() == 0) {
            return;
        }
        Queue<GlobalObject> updated = new LinkedList<>();
        GlobalObject object;
        objects.removeAll(remove);
        remove.clear();
        while ((object = objects.poll()) != null) {
            if (object.getInstance() != null && object.getInstance().isDisposed()) {
                logger.debug("Remove global object because instance disposed {}", object);
                continue;
            }
            if (object.getTicksRemaining() < 0) {
                updated.add(object);
                continue;
            }
            object.removeTick();
            if (object.getTicksRemaining() == 0) {//careful here. removing an object that is -1 instead of something else is dangerous...
             //   System.out.println("here gotta remove the object?");
//                if (object.getObjectId() == SpiderWeb.RESTORE_ID) {
                    /**
                     * You have to delete both world and custom object before placing down a new 1. Method should work without issues.
                     */
                  GlobalObject obj = object;
                    /**
                     * Sets objectId
                     */
                   obj.setId(object.getRestoreId() == -1 ? object.getObjectId(): object.getRestoreId());
                    /**
                     * Adds new object
                     */
                    if(object.getRestoreId() == -1)
                        remove(obj);
                    else
                    add(obj);

//                } else {
                   placeObject(object, object.getRestoreId());//
               // }
                updateObject(object, object.getRestoreId());
            } else {
                updated.add(object);
            }
        }
        objects.addAll(updated);
    }

    /**
     * Updates a single global object with a new object id in the game world for every player within a region.
     *
     * @param object   the new global object
     * @param objectId the new object id
     */
    public void updateObject(final GlobalObject object, final int objectId) {
        List<Player> players = PlayerHandler.nonNullStream().filter(Objects::nonNull)
                .filter(player -> player.distanceToPoint(object.getX(), object.getY()) <= 60 && player.heightLevel == object.getHeight() && object.getInstance() == player.getInstance()
                ).collect(Collectors.toList());
        players.forEach(player -> {
            player.getPA().object(objectId, object.getX(), object.getY(), object.getFace(), object.getType(), true);
        });
    }

    /**
     * Don't use this. This is for clipping + clicking support.
     */
    public void placeObject(final GlobalObject object, final int objectId) {
        object.getRegionProvider().get(object.getX(), object.getY()).removeWorldObject(object);
        if (objectId != -1) {
            object.getRegionProvider().get(object.getX(), object.getY()).addWorldObject(object.withId(objectId));
        }
    }

    /**
     * Updates all region objects for a specific player
     *
     * @param player the player were updating all objects for
     */
    public void updateRegionObjects(Player player) {
        objects.stream().filter(Objects::nonNull).filter(object -> player.distanceToPoint(object.getX(), object.getY()) <= 60 && object.getHeight() == player.heightLevel && object.getInstance() == player.getInstance())
                .forEach(object -> player.getPA().object(object.getObjectId(), object.getX(), object.getY(), object.getFace(), object.getType(), true));
        loadCustomObjects(player);
    }
    private void loadCustomObjects(Player player) {
        checkGWD(player);
        checkRFDChest(player);
    }
    public void checkRFDChest(Player player) {
     if(Boundary.isIn(player, Boundary.LUMB_BASEMENT))
     if(player.getQuesting().getQuestList().get(2).isQuestCompleted()){
            player.getPA().checkObjectSpawn(12309, 3219, 9623, player.heightLevel,1,10);
     }
    }
    public void checkGWD(Player player) {//OUTSIDE_GWD
        if(Boundary.isIn_dontcareheight(player, Boundary.OUTSIDE_GWD) ){
            if(player.learnedGWDentrance)
                player.getPA().checkObjectSpawn(26418, 2917, 3745, player.heightLevel,0,10);
        }

      //  else
          //  player.getPA().checkObjectSpawn(26419, 2917, 3745, 0,10);
if(Boundary.isIn_dontcareheight(player, Boundary.GODWARS_MAIN_AREA) ){
    if(player.firstropesaradomingwd){
        if(player.heightLevel == 2)
            player.getPA().checkObjectSpawn(26372, 2913, 5300, player.heightLevel,2,10);
        if(player.heightLevel == 1)
            player.getPA().checkObjectSpawn(26370, 2914, 5300, player.heightLevel,2,10);//rope back up to main dungeon
    }
    if(player.secondropesaradomingwd){
        if(player.heightLevel == 1)
            player.getPA().checkObjectSpawn(26376, 2920, 5274, player.heightLevel,3,10);
        if(player.heightLevel == 0)
            player.getPA().checkObjectSpawn(26374, 2920, 5274, player.heightLevel,3,10);//rope back up to 1st level
    }
}

     //   else
          //  player.getPA().checkObjectSpawn(26561, 2913, 5300, 3,10);


    }

    /**
     * Loads all object information from a simple text file
     *
     * @throws IOException an exception likely to occur from file non-existence or during reading protocol
     */
    public void loadGlobalObjectFile() throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(Server.getDataDirectory() + "/cfg/obj/global_objects.cfg")))) {
            String line = null;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }
                String[] data = line.split("\t");
                if (data.length != 6) {
                    continue;
                }
                int id, x, y, height, face, type;
                try {
                    id = Integer.parseInt(data[0]);
                    x = Integer.parseInt(data[1]);
                    y = Integer.parseInt(data[2]);
                    height = Integer.parseInt(data[3]);
                    face = Integer.parseInt(data[4]);
                    type = Integer.parseInt(data[5]);
                } catch (NumberFormatException nfe) {
                    System.out.println("WARNING: Unable to load object from file." + lineNumber);
                    lineNumber++;
                    continue;
                }
                add(new GlobalObject(id, x, y, height, face, type, -1));
                lineNumber++;
            }
        }
    }

    /**
     * This is a convenience method that should only be referenced when testing game content on a private host. This should not be referenced during the active game.
     *
     * @throws IOException
     */
    public void reloadObjectFile(Player player) throws IOException {
        objects.clear();
        loadGlobalObjectFile();
        updateRegionObjects(player);
    }

    @Override
    public String toString() {
        List<GlobalObject> copy = new ArrayList<>(objects);
        long matches = objects.stream().filter(o -> copy.stream().anyMatch(m -> m.getX() == o.getX() && m.getY() == o.getY())).count();
        StringBuilder sb = new StringBuilder();
        sb.append("GlobalObjects: <size: " + objects.size() + ", same spot: " + matches + "> [");
        sb.append("\n");
        for (GlobalObject object : objects) {
            if (object == null) {
                continue;
            }
            sb.append("\t<id: " + object.getObjectId() + ", x: " + object.getX() + ", y: " + object.getY() + ">\n");
        }
        sb.append("]");
        return sb.toString();
    }

}
