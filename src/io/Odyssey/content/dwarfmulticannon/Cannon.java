package io.Odyssey.content.dwarfmulticannon;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.weapon.AttackStyle;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Items;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.Projectile;
import io.Odyssey.model.ProjectileBaseBuilder;
import io.Odyssey.model.collisionmap.PathChecker;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.Odyssey.content.dwarfmulticannon.CannonConstants.*;

public class Cannon {

    private static final Logger logger = LoggerFactory.getLogger(Cannon.class);

    private static HashSet<Boundary> BLOCKED_BOUNDARIES = new HashSet<>(Arrays.asList(
            Boundary.MAGE_ARENA
    ));

    public static void attemptPlace(Player player) {
        if (Arrays.stream(PROHIBITED_CANNON_AREAS).anyMatch(boundary -> boundary.in(player)) && !Arrays.stream(ALLOWED_REV_AREAS).anyMatch(boundary -> boundary.in(player))) {
            player.sendMessage("You can't place a cannon in this area.");
        } else if ((player.getInstance() != null || player.getHeight() > 3) && !Boundary.CORPOREAL_BEAST_LAIR.in(player) && !Boundary.CRYSTAL_CAVE_AREA.in(player)) {
            player.sendMessage("You can't place a cannon inside an instance.");
        } else if (Boundary.CRYSTAL_CAVE_AREA.in(player) && player.getPosition().getHeight() == 4) {
            player.sendMessage("You can't place a cannon in this cave.");
        } else if (player.getCannon() != null) {
            player.sendMessage("You already have a cannon.");
        } else if (!Arrays.stream(CANNON_PIECES).allMatch(piece -> player.getItems().playerHasItem(piece))) {
            player.sendMessage("Your missing a cannon piece!");
        } else if (!CannonRepository.hasDistanceFromOtherCannons(player.getPosition())) {
            player.sendMessage("You can't place your cannon this close to another cannon.");
        } else {
            // Clipping check
            Optional<Boundary> inBlockedBoundary = BLOCKED_BOUNDARIES.stream().filter(boundary -> boundary.in(player)).findAny();
            if (inBlockedBoundary.isPresent()) {
                player.sendMessage("You cannot place a cannon here.");
                return;
            }

            for (int x = player.getPosition().getX(); x < player.getPosition().getX() + 2; x++) {
                for (int y = player.getPosition().getY(); y < player.getPosition().getY() + 2; y++) {
                    if (player.getRegionProvider().getClipping(x, y, player.getHeight()) != 0
                            && !player.getRegionProvider().isOccupiedByNpc(x, y, player.getHeight())) {
                        player.sendMessage("You don't have enough space to place a cannon here.");
                        return;
                    }
                }
            }

            player.setCannon(new Cannon(player.getPosition()));
            player.getCannon().place(player);
        }
    }

    public static boolean clickObject(Player player, int objectId, Position position, int option) {
        if (objectId == COMPLETE_CANNON_OBJECT_ID) {
            if (!CannonRepository.exists(position)) {
                logger.error("No cannon exists but is being clicked: " + position);
            }

            if (player.getCannon() != null && player.getCannon().getPosition().equals(position)) {
                if (option == 1) {
                    player.getCannon().load(player);
                } else if (option == 2) {
                    player.getCannon().pickup(player, true);
                } else if (option == 3) {
                    player.getCannon().unload(player);
                }
            } else {
                player.sendMessage("This isn't your cannon.");
            }

            return true;
        }

        return false;
    }

    public static boolean clickItem(Player player, int itemId) {
        if (itemId == Items.CANNON_BASE) {
            Cannon.attemptPlace(player);
            return true;
        }

        return false;
    }

    /**
     * Cannon identifier.
     */
    private static int globalCannonIdentifier = 0;

    /**
     * Unique id to identify the cannon instance.
     */
    private final int identifier;

    /**
     * The amount of balls in the cannon.
     */
    private int cannonballsLoaded = 0;

    /**
     * Is the cannon rotating and shooting?
     */
    private boolean operating = false;

    /**
     * Cannon position;
     */
    private final Position position;

    /**
     * The cannon game object.
     */
    private final GlobalObject object;

    /**
     * State of the cannon rotation.
     */
    private CannonRotationState rotationState = CannonRotationState.NORTH;

    public Cannon(Position position) {
        this.position = position;
        identifier = globalCannonIdentifier++;
        rotation = Rotation.NORTH;
        rotationState = CannonRotationState.NORTH;
        object = new GlobalObject(COMPLETE_CANNON_OBJECT_ID, getPosition(), 0, 10);
    }

    public boolean encroaches(Position position) {
        List<Position> local = getPosition().getTiles(CANNON_SIZE);
        List<Position> other = position.getTiles(CANNON_SIZE);
        return local.stream().anyMatch(other::contains);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cannon cannon = (Cannon) o;
        return identifier == cannon.identifier &&
                Objects.equals(position, cannon.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, position);
    }

    private void place(Player player) {
        if (CannonRepository.add(this)) {
            Arrays.stream(CANNON_PIECES).forEach(piece -> player.getItems().deleteItem(piece, 1));
            player.startAnimation(PLACING_ANIMATION);
            Server.getGlobalObjects().add(getObject());
            //     load(player);
        } else {
            player.setCannon(null);
            player.sendMessage("You can't set a cannon there!");
        }
    }

    private void load(Player player) {
        int ballType = player.getItems().getInventoryCount(Items.GRANITE_CANNONBALL) > 0 ? Items.GRANITE_CANNONBALL : Items.CANNONBALL;
        int balls = player.getItems().getInventoryCount(ballType);

        if (player.usingGraniteCannonballs && cannonballsLoaded > 0 && ballType == Items.CANNONBALL) {
            player.sendMessage("You cannot mix cannonball types in your cannon.");
            return;
        }
        if (player.getItems().getInventoryCount(Items.GRANITE_CANNONBALL) > 0) {
            ballType = Items.GRANITE_CANNONBALL;
            balls = player.getItems().getInventoryCount(ballType);
            player.usingGraniteCannonballs = true;
        } else {
            player.usingGraniteCannonballs = false;
        }

        int max = getCannonMaxAmmoCount(player);
        int adding = max - cannonballsLoaded;
        if (adding > balls) {
            adding = balls;
        }

        if (cannonballsLoaded == max) {
            player.sendMessage("Your cannon is already fully loaded.");
        } else if (adding > 0) {
            player.getItems().deleteItem(ballType, adding);
            cannonballsLoaded += adding;
            rotation = Rotation.NORTH;
            operating = true;
            tick(player);
            player.sendMessage("You load " + adding + " cannonballs into your cannon.");
        } else {
            player.sendMessage("You don't have any cannonballs to load.");
        }

    }

    private void unload(Player player) {
        int item = Items.CANNONBALL;
        if (player.usingGraniteCannonballs) {
            item = Items.GRANITE_CANNONBALL;
        }
        if (cannonballsLoaded > 0) {
            if (player.getItems().hasRoomInInventory(item, cannonballsLoaded)) {
                player.getItems().addItem(item, cannonballsLoaded);
                cannonballsLoaded = 0;
            } else {
                player.getItems().sendItemToAnyTab(item, cannonballsLoaded);
                cannonballsLoaded = 0;
            }
        } else {
            player.sendMessage("Your cannon is empty.");
        }
    }

    public void pickup(Player player, boolean manual) {
        if (manual) {
            if (player.getItems().freeSlots() < 4) {
                player.sendMessage("You need at least 4 spaces to pick this up.");
                return;
            }
            Arrays.stream(CANNON_PIECES).forEach(it -> player.getItems().addItem(it, 1));
        } else {
            Arrays.stream(CANNON_PIECES).forEach(it -> player.getItems().addItemUnderAnyCircumstance(it, 1));
        }

        if (cannonballsLoaded > 0) {
            int item = Items.CANNONBALL;
            if (player.usingGraniteCannonballs) {
                item = Items.GRANITE_CANNONBALL;
            }
            if (player.getItems().hasRoomInInventory(item, cannonballsLoaded)) {
                player.getItems().addItem(item, cannonballsLoaded);
            } else {
                player.getItems().sendItemToAnyTab(item, cannonballsLoaded);
            }
        }

        cannonballsLoaded=0;
        CannonRepository.remove(this);
        Server.getGlobalObjects().remove(getObject());
        Server.getGlobalObjects().updateObject(getObject(), -1);
        player.setCannon(null);
    }

    public void tick(Player player) {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {

                if (cannonballsLoaded <= 0) {
                    container.stop();
                    return;
                }


                if (operating && player.distance(getPosition()) <= 18) {

                    rotate(player);
                    shoot(player);
                }
            }
        }, 2);// 1 or 2


    }
    private enum Rotation {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
    }
    private Rotation rotation;

    private void rotate(Player player) {
        //  int next = (rotationState.ordinal() + 1) % CannonRotationState.values().length;
        // rotationState = CannonRotationState.values()[next];
        // Server.playerHandler.sendObjectAnimation(getObject(), rotationState.getAnimationId());
        if (rotation == null) {
            rotation = Rotation.NORTH;
            rotate(player);
           shoot(player);
            return;
        }
        int next = (rotationState.ordinal() + 1) % CannonRotationState.values().length;
        rotationState = CannonRotationState.values()[next];
        switch (rotation) {
            case NORTH: // north
                rotation = Rotation.NORTH_EAST;
                break;
            case NORTH_EAST: // north-east
                rotation = Rotation.EAST;
                break;
            case EAST: // east
                rotation = Rotation.SOUTH_EAST;
                break;
            case SOUTH_EAST: // south-east
                rotation = Rotation.SOUTH;
                break;
            case SOUTH: // south
                rotation = Rotation.SOUTH_WEST;
                break;
            case SOUTH_WEST: // south-west
                rotation = Rotation.WEST;
                break;
            case WEST: // west
                rotation = Rotation.NORTH_WEST;
                break;
            case NORTH_WEST: // north-west
                rotation = null;
                break;
        }
        rotate_shoot();
    }
    private void rotate_shoot() {
        switch (rotation) {
            case NORTH: // north
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 516);
                // player.getPA().sendObjectAnimation_cannon(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset, 516, 10, -1);
                break;
            case NORTH_EAST: // north-east
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 517);

                // player.getPA().sendObjectAnimation_cannon(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset, 517, 10, -1);
                break;
            case EAST: // east
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 518);

                //  player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset, 518, 10, -1);
                break;
            case SOUTH_EAST: // south-east
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 519);

                // player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset,519, 10, -1);
                break;
            case SOUTH: // south
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 520);

                //  player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset,520, 10, -1);
                break;
            case SOUTH_WEST: // south-west
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 521);

                //  player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset,521, 10, -1);
                break;
            case WEST: // west
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 514);

                //    player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset, 514, 10, -1);
                break;
            case NORTH_WEST: // north-west
                Server.playerHandler.sendObjectAnimation_cannon(getObject(), 515);

                //  player.getPA().objectAnim(cannon2.getPosition().getX()+xoffset, cannon2.getPosition().getY()+yoffset,515, 10, -1);
                rotation = null;
                break;
        }
    }
    private void shoot(Player player) {
        List<NPC> npcs = getShootableNpcs(player);
        if (!npcs.isEmpty()) {
            if (cannonballsLoaded > 0) {
                for (NPC npc : npcs) {
                    int maxDamage = MAX_DAMAGE;
                    if (player.usingGraniteCannonballs) {
                        maxDamage = MAX_GRANITE_DAMAGE;
                    }
                    if (npc.getNpcId() == Npcs.CORPOREAL_BEAST) {
                        maxDamage /= 2;
                    }
                    int damage = Misc.random(maxDamage);

                    Projectile.createTargeted(getPosition().getCenterPosition(3), 2, npc, new ProjectileBaseBuilder().setProjectileId(PROJECTILE_ID).setCurve(0).setSendDelay(2).createProjectileBase()).send(null);

                    int nX = getPosition().getX();
                    int nY = getPosition().getY();
                    //int offX = (pY - nY) * -1;
                    //  int offY = (pX - nX) * -1;

                    int distance = (int) getPosition().getDistance(nX, nY);
                    int hitDelay = (int) (2 + Math.ceil((double) (1 + distance) / 3));
//System.out.println("hitd: "+hitDelay);

                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            player.getPA().addSkillXPMultiplied(damage * 2, Skill.RANGED.getId(), true);
                            npc.appendDamage(player, damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                           // if (npc.isAutoRetaliate()) {
                                npc.attackEntity(player);
                          //  }
                            container.stop();
                        }
                    }, hitDelay);

                    cannonballsLoaded--;
                    if (cannonballsLoaded == 0) {
                        break;
                    }
                }
            } else {
                operating = false;
                player.sendMessage("Your cannon has run out of ammo.");
            }
        }
    }

    public List<NPC> getShootableNpcs(Player player) {
        List<NPC> possibleTargets = Arrays.stream(NPCHandler.npcs).filter(npc -> npc != null && !npc.isDead && npc.heightLevel == getPosition().getHeight()
                && npc.distance(getPosition()) <= 12         && npc.getNpcId() != 101 /*can't shoot rock crabs that are still as "rocks" */
                && player.attacking.attackEntityCheck(npc, false)
                && PathChecker.raycast(player, npc, true)).collect(Collectors.toList());

        int rotationStateIndex = (rotationState.ordinal() + 1) % CannonRotationState.values().length;
        List<NPC> targets = Lists.newArrayList();
        for (NPC local : possibleTargets) {
            int cannonX = getPosition().getX();
            int cannonY = getPosition().getY();
            int localX = local.getPosition().getX();
            int localY = local.getPosition().getY();

            switch (CannonRotationState.values()[rotationStateIndex]) {
                case NORTH:
                    if (localY > cannonY && localX >= cannonX - 1 && localX <= cannonX + 1)
                        targets.add(local);
                    break;
                case NORTH_EAST:
                    if (localX >= cannonX + 1 && localY >= cannonY + 1)
                        targets.add(local);
                    break;
                case EAST:
                    if (localX > cannonX && localY >= cannonY - 1 && localY <= cannonY + 1)
                        targets.add(local);
                    break;
                case SOUTH_EAST:
                    if (localY <= cannonY - 1 && localX >= cannonX + 1)
                        targets.add(local);
                    break;
                case SOUTH:
                    if (localY < cannonY && localX >= cannonX - 1 && localX <= cannonX + 1)
                        targets.add(local);
                    break;
                case SOUTH_WEST:
                    if (localX <= cannonX - 1 && localY <= cannonY - 1)
                        targets.add(local);
                    break;
                case WEST:
                    if (localX < cannonX && localY >= cannonY - 1 && localY <= cannonY + 1)
                        targets.add(local);
                    break;
                case NORTH_WEST:
                    if (localX <= cannonX - 1 && localY >= cannonY + 1)
                        targets.add(local);
                    break;
            }
        }

        return targets.stream().limit(2).collect(Collectors.toList());
    }

    private GlobalObject getObject() {
        return object;
    }

    public Position getPosition() {
        return position;
    }

    private int getCannonMaxAmmoCount(Player player) {
        if (player.amDonated >= 1000)
            return 60;
        if (player.amDonated >= 250)
            return 50;
        if (player.amDonated >= 250)
            return 45;
        if (player.amDonated >= 100)
            return 40;
        if (player.amDonated >= 25)
            return 35;
        return 30;
    }
}
