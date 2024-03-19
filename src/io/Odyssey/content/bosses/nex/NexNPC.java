
package io.Odyssey.content.bosses.nex;

import com.google.common.collect.Lists;
import io.Odyssey.content.bosses.hydra.CombatProjectile;
import io.Odyssey.content.bosses.nex.attacks.*;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.model.*;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.HealthStatus;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.util.Misc;

import java.util.*;
import java.util.stream.Collectors;

public class NexNPC extends NPC {

    public static NexNPC instance;
    public static final Position SPAWN_POSITION = new Position(2925, 5203);
    public static final Boundary BOUNDARY = new Boundary(2910, 5188, 2940, 5218);
    final int MELEE_ANIM = 9181;
    final int MAGE_ANIM = 9179;
    final int RANGE_ANIM = 9180;
    final int SIPHON_ANIM = 9183;
    private int attackSpeed = 4;
    private boolean isAttacking = false;

    final int NEX = 11280;
    final int CRUOR = 11285;
    final int FUMUS = 11283;
    final int UMBRA = 11284;
    final int GLACIES = 11286;
    public boolean fumusStarted = false;
    public boolean cruorStarted = false;
    public boolean umbraStarted = false;
    public boolean glaciesStarted = false;
    NPC glaciesNPC;
    NPC cruorNPC;
    NPC umbraNPC;
    NPC nexNPC;
    NPC fumusNPC;
    Position fumusSpawnPoint = new Position(2914, 5214);
    Position umbraSpawnPoint = new Position(2936, 5214);
    Position cruorSpawnPoint = new Position(2914, 5192);
    Position glaciesSpawnPoint = new Position(2936, 5192);
    int spawnMageAnimation = 9189;
    List<Player> targets;
    Phase currentPhase;
    Player currenTarget;
    int specialTimer = 75;
    boolean canBeAttacked = true;
    boolean started = false;
    public boolean siphoning = false;

    List<NPC> reavers = new ArrayList<>();
    private int currentPlayersize;
    public NexNPC(int npcId, Position position) {
        super(npcId, position);
        init();
        instance = this;
        loadItems();
        currentPlayersize = PlayerHandler.getPlayers().size();
    }
    private final Map<LootRarity, List<GameItem>> items = new HashMap<>();
    private static final Map<LootRarity, List<GameItem>> itemz = new HashMap<>();

    public void loadItems() {
        if (items.isEmpty()) {
            items.put(LootRarity.COMMON, Arrays.asList(
                    new GameItem(Items.DEATH_RUNE, 325),
                    new GameItem(Items.BLOOD_RUNE, 170),
                    new GameItem(Items.SOUL_RUNE, 227),
                    new GameItem(Items.DRAGON_BOLTS_UNF, 90),
                    new GameItem(Items.CANNONBALL, 298),
                    new GameItem(Items.AIR_RUNE, 1365),
                    new GameItem(Items.FIRE_RUNE, 1655),
                    new GameItem(Items.WATER_RUNE, 1599),
                    new GameItem(Items.ONYX_BOLTS_E, 29),
                    new GameItem(Items.AIR_ORB_NOTED, 20),
                    new GameItem(Items.UNCUT_RUBY_NOTED, 26),
                    new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
                    new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
                    new GameItem(Items.COAL_NOTED, 95),
                    new GameItem(Items.RUNITE_ORE_NOTED, 28),
                    new GameItem(Items.SHARK, 3),
                    new GameItem(Items.PRAYER_POTION4, 1),
                    new GameItem(Items.SARADOMIN_BREW4, 2),
                    new GameItem(Items.SUPER_RESTORE4, 1),
                    new GameItem(Items.COINS, 26748)
            ));

            items.put(LootRarity.RARE, Arrays.asList(
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(19841, 1),
                    new GameItem(26235, 1),

                    new GameItem(26382),
                    new GameItem(26382),
                    new GameItem(26382),

                    new GameItem(26384),
                    new GameItem(26384),
                    new GameItem(26384),

                    new GameItem(26386),
                    new GameItem(26386),
                    new GameItem(26386),

                    new GameItem(26233),
                    new GameItem(26374)

            ));
        }
    }

    static {
        itemz.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(Items.DEATH_RUNE, 325),
                new GameItem(Items.BLOOD_RUNE, 170),
                new GameItem(Items.SOUL_RUNE, 227),
                new GameItem(Items.DRAGON_BOLTS_UNF, 90),
                new GameItem(Items.CANNONBALL, 298),
                new GameItem(Items.AIR_RUNE, 1365),
                new GameItem(Items.FIRE_RUNE, 1655),
                new GameItem(Items.WATER_RUNE, 1599),
                new GameItem(Items.ONYX_BOLTS_E, 29),
                new GameItem(Items.AIR_ORB_NOTED, 20),
                new GameItem(Items.UNCUT_RUBY_NOTED, 26),
                new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
                new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
                new GameItem(Items.COAL_NOTED, 95),
                new GameItem(Items.RUNITE_ORE_NOTED, 28),
                new GameItem(Items.SHARK, 3),
                new GameItem(Items.PRAYER_POTION4, 1),
                new GameItem(Items.SARADOMIN_BREW4, 2),
                new GameItem(Items.SUPER_RESTORE4, 1),
                new GameItem(Items.COINS, 26748)
        ));

        itemz.put(LootRarity.RARE, Arrays.asList(
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(19841, 1),
                new GameItem(26235, 1),

                new GameItem(26382),
                new GameItem(26382),
                new GameItem(26382),

                new GameItem(26384),
                new GameItem(26384),
                new GameItem(26384),

                new GameItem(26386),
                new GameItem(26386),
                new GameItem(26386),

                new GameItem(26233),
                new GameItem(26374)
        ));
    }
    public static ArrayList<GameItem> getAllDrops() {
        ArrayList<GameItem> drops = new ArrayList<>();
        itemz.forEach((lootRarity, gameItems) -> {
            gameItems.forEach(g -> {
                if (!drops.contains(g)) {
                    drops.add(g);
                }
            });
        });
        return drops;
    }
    public void getReward(Player player) {
        for (GameItem randomItem : getRandomItems()) {
            if (player.getInventory().freeInventorySlots() < 1) {
                player.getInventory().addToBank(new ImmutableItem(randomItem));
                player.sendMessage(randomItem.getDef().getName() + ", Has been added to your bank!");
            } else {
                player.getInventory().addOrDrop(new ImmutableItem(randomItem));
            }
            if (items.get(LootRarity.RARE).contains(randomItem)) {
                player.getCollectionLog().handleDrop(player, 11278, randomItem.getId(), randomItem.getAmount(), true);
            }
        }
    }
    public List<GameItem> getRandomItems() {
        List<GameItem> rewards = Lists.newArrayList();
        int rareChance = 10;

        if (Misc.random(1, rareChance) == 1) {
            rewards.add(Misc.getRandomItem(items.get(LootRarity.RARE)));
        } else {
            for (int count = 0; count < 2; count++) {
                rewards.add(Misc.getRandomItem(items.get(LootRarity.COMMON)));
            }
        }
        return rewards;
    }
    public static Map<LootRarity, List<GameItem>> getItems() {
        return itemz;
    }

    public static ArrayList<GameItem> getRareDrops() {
        ArrayList<GameItem> drops = new ArrayList<>();
        List<GameItem> found = getItems().get(LootRarity.RARE);
        for(GameItem f : found) {
            boolean foundItem = false;
            for(GameItem drop : drops) {
                if (drop.getId() == f.getId()) {
                    foundItem = true;
                    break;
                }
            }
            if (!foundItem) {
                drops.add(f);
            }
        }
        return drops;
    }

    /*
     Gets the targets in the area
      */
    public void updateTargets() {
        targets = PlayerHandler.getPlayers().stream().filter(plr ->
                !plr.isDead && BOUNDARY.in(plr)).collect(Collectors.toList());
        System.out.println("targets size is " + targets.size());
    }

    /*
    Initialising Nex
     */
    public void init() {
        currentPhase = Phase.SMOKE;
        setAttacks();
        canBeAttacked = true;
        nexStartupEvent(this);
    }
    public void startNewPhase(Phase newPhase) {
        currentPhase = newPhase;
        canBeAttacked = true;
        setAttacks();
        switch (newPhase) {
            case SMOKE:
                isAttacking = true;
                asNPC().forceChat("Fill my soul with smoke!");
                break;
            case SHADOW:
                asNPC().forceChat("Darken my shadow!");
                asNPC().forceChat("There is... NO ESCAPE!");
                break;
            case BLOOD:
                asNPC().forceChat("Flood my lungs with blood!");
                break;
            case ICE:
                asNPC().forceChat("Infuse me with the power of ice!");
                break;
            case ZAROS:
                asNPC().forceChat("NOW, THE POWER OF ZAROS!");
             asNPC().appendHeal(500, Hitmark.HEAL_PURPLE);
                break;
        }
    }


    @Override
    public boolean canBeAttacked(Entity entity) {
        return canBeAttacked;
    }

    private void meleeAttack(Player target) {
        asNPC().startAnimation (MELEE_ANIM);
        target.appendDamage(Misc.random(0,10), Hitmark.HIT);
    }


    public void sendProjectile(Player target, CombatProjectile projectile, CombatType combatType, int maxDamage) {
        int size = (int) Math.ceil((double) asNPC().getSize() / 2.0);
        int centerX = asNPC().getX() + size;
        int centerY = asNPC().getY() + size;
        int offsetX = (centerY - target.getY()) * -1;
        int offsetY = (centerX - target.getX()) * -1;
        currenTarget.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(),
                projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                int distance = (int) target.getPosition().getAbsDistance(asNPC().getPosition());
                int ticks;
                if(distance <= 5) {
                    ticks = 1;
                } else if (distance > 5 && distance < 9) {
                    ticks = 2;
                } else if(distance >= 9 && distance < 14) {
                    ticks = 3;
                } else {
                    ticks = 4;
                }
                if(container.getTotalTicks() == ticks && target != null){
                    int damage = Misc.random(maxDamage);
                    if(combatType.equals(CombatType.MAGE) && target.protectingMagic())
                        damage *= 0.5;
                    else if(combatType.equals(CombatType.RANGE) && target.protectingRange())
                        damage *= 0.5;
                    target.appendDamage(damage, Hitmark.HIT);
                }
            }
        }, 1);
    }

    private static final CombatProjectile SMOKE_RUSH_PROJECTILE = new CombatProjectile(384, 50, 25, 4, 50, 0, 50);
    private static final CombatProjectile SHADOW_RANGE_PROJECTILE = new CombatProjectile(2012, 50, 25, 4, 50, 0, 50);
    private static final CombatProjectile BASIC_MAGIC_PROJECTILE = new CombatProjectile(2004, 50, 25, 4, 50, 0, 50);

    private void attack() {
        attackSpeed = 5;
        updateTargets();
        currenTarget = getRandomTarget();
       facePosition(currenTarget.getPosition());
        switch (currentPhase) {
            case SMOKE:
                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 2 && Misc.random(2) == 0) {
                    meleeAttack(currenTarget);
                    this.startAnimation(MELEE_ANIM);
                } else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    specialTimer = 75;
                    this.forceChat("Let the virus flow through you!");
                    this.startAnimation(9188);
                    new ChokeAttack(getRandomTarget());
                } else {
                    for (Player player:
                            targets) {
                        this.startAnimation(MAGE_ANIM);
                        sendProjectile(player, SMOKE_RUSH_PROJECTILE, CombatType.MAGE, 10);
                    }
                }
                break;
            case SHADOW:
                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
                    meleeAttack(currenTarget);
                    this.startAnimation(MELEE_ANIM);
                }
                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    specialTimer = 75;
                    this.forceChat("Fear the shadow!");
                    this.startAnimation(9188);
                    new ShadowSmash(targets);
                } else {
                    for (Player player :
                            targets) {
                        sendProjectile(player, SHADOW_RANGE_PROJECTILE, CombatType.RANGE, 10);
                        this.startAnimation(RANGE_ANIM);
                    }
                }
                break;
            case BLOOD:
                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
                    meleeAttack(currenTarget);
                    this.startAnimation(MELEE_ANIM);
                }
                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    specialTimer = 75;
                    new BloodSacrifice(asNPC(), getRandomTarget());
                    this.forceChat("I demand a blood sacrifice!");
                }
                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    specialTimer = 75;
                    spawnBloodReavers();
                    this.forceChat("A siphon will solve this!");
                    nexSiphon(this);
                    this.startAnimation(SIPHON_ANIM);
                } else {
                    new BloodBarrage(this, getRandomTarget(), targets, Misc.random(33));
                    this.startAnimation(MAGE_ANIM);
                }
                break;
            case ICE:
                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
                    meleeAttack(currenTarget);
                    this.startAnimation(9181);
                }
                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    this.forceChat("Contain this!");
                    updateTargets();
                    new Containment(this, targets);
                    this.startAnimation(9186);
                    specialTimer = 75;
                }
                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
                    this.forceChat("Die now, in a prison of ice!");
                    updateTargets();
                    new IcePrison(targets);
                    this.startAnimation(MAGE_ANIM);
                    specialTimer = 75;
                } else {
                    Player target = getRandomTarget();
                    new IceBarrage(target, targets);
                    this.startAnimation(MAGE_ANIM);
                    target.freezeDelay = 15;
                }
                break;
            case ZAROS:
                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
                    meleeAttack(currenTarget);
                    this.startAnimation(9181);
                }
                else {
                    for (Player player:
                            targets) {
                        sendProjectile(player, BASIC_MAGIC_PROJECTILE, CombatType.MAGE, 10);
                        this.startAnimation(MAGE_ANIM);
                    }
                }
                break;
        }
    }

    private void setAttacks() {
       // System.out.println("test1");
        switch (currentPhase) {
            case SMOKE:
                System.out.println("phase: smoke");
                setNpcAutoAttacks(Lists.newArrayList(
                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 0 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                   // System.out.println("test3");
                                    this.forceChat("Let the virus flow through you!");
                                    new ChokeAttack(getRandomTarget());
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),

                        /**
                         * Magic attack
                         */
                        new NPCAutoAttackBuilder()
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(4)
                                .setMaxHit(33)
                                .setMultiAttack(true)
                                .setAnimation(new Animation(MAGE_ANIM))//magic attack
                                .setAttackDelay(6)
                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(384).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
                                .setOnHit(attack -> {
                                    System.out.println("test4");
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                })
                                .setOnHit(attack -> {
                                    System.out.println("test5");
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(385, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                    if(Misc.random(4) == 0)
                                     //   System.out.println("test6");
                                    attack.getVictim().getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(this));
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                                .setCombatType(CombatType.MELEE)
                                .setDistanceRequiredForAttack(1)
                                .setHitDelay(2)
                                .setAnimation(new Animation(MELEE_ANIM))
                                .setMaxHit(30)
                                .setAttackDelay(6)
                                .createNPCAutoAttack()

                ));
                break;
            case SHADOW:
                System.out.println("test777");
                setNpcAutoAttacks(Lists.newArrayList(
                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 0 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                    this.forceChat("Fear the shadow!");
                                    updateTargets();
                                    new ShadowSmash(targets);
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),

                        /**
                         * Magic attack
                         */
                        new NPCAutoAttackBuilder()
                                .setCombatType(CombatType.RANGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(4)
                                .setMaxHit(33)
                                .setMultiAttack(true)
                                .setAnimation(new Animation(RANGE_ANIM))
                                .setAttackDelay(6)
                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(2012).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                                .setCombatType(CombatType.MELEE)
                                .setDistanceRequiredForAttack(1)
                                .setHitDelay(2)
                                .setAnimation(new Animation(MELEE_ANIM))
                                .setMaxHit(30)
                                .setAttackDelay(6)
                                .createNPCAutoAttack()
                ));
                break;
            case BLOOD:
                setNpcAutoAttacks(Lists.newArrayList(
                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                    this.forceChat("I demand a blood sacrifice!");
                                    new BloodSacrifice(this, getRandomTarget());
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                    this.forceChat("A siphon will solve this!");
                                    spawnBloodReavers();
                                    nexSiphon(this);
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),



                        /**
                         * Magic attack
                         */
                        new NPCAutoAttackBuilder()
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(4)
                                .setMaxHit(33)
                                .setMultiAttack(false)
                                .setAnimation(new Animation(MAGE_ANIM))
                                .setAttackDelay(6)
                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(-1).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                })
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(377, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                    new BloodBarrage(this, attack.getVictim().asPlayer(), targets, attack.getCombatHit().getDamage());
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                                .setCombatType(CombatType.MELEE)
                                .setDistanceRequiredForAttack(1)
                                .setHitDelay(2)
                                .setAnimation(new Animation(MELEE_ANIM))
                                .setMaxHit(30)
                                .setAttackDelay(6)
                                .createNPCAutoAttack()

                ));
                break;
            case ICE:
                setNpcAutoAttacks(Lists.newArrayList(
                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(6) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                    this.forceChat("Contain this!");
                                    updateTargets();
                                    new Containment(this, targets);
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(6) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(2)
                                .setAnimation(new Animation(-1))
                                .setMaxHit(0)
                                .setAttackDelay(2)
                                .setOnAttack(attack -> {
                                    this.forceChat("Die now, in a prison of ice!");
                                    updateTargets();
                                    new IcePrison(targets);
                                    specialTimer = 75;
                                })
                                .createNPCAutoAttack(),



                        /**
                         * Magic attack
                         */
                        new NPCAutoAttackBuilder()
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(4)
                                .setMaxHit(33)
                                .setMultiAttack(false)
                                .setAnimation(new Animation(MAGE_ANIM))
                                .setAttackDelay(6)
                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(-1).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                })
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(369, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                    new IceBarrage(attack.getVictim().asPlayer(), targets);
                                    attack.getVictim().asPlayer().freezeDelay = 15;
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                                .setCombatType(CombatType.MELEE)
                                .setDistanceRequiredForAttack(1)
                                .setHitDelay(2)
                                .setAnimation(new Animation(MELEE_ANIM))
                                .setMaxHit(30)
                                .setAttackDelay(6)
                                .createNPCAutoAttack()

                ));
                break;
            case ZAROS:
                setNpcAutoAttacks(Lists.newArrayList(
                        /**
                         * Magic attack
                         */
                        new NPCAutoAttackBuilder()
                                .setCombatType(CombatType.MAGE)
                                .setDistanceRequiredForAttack(20)
                                .setHitDelay(4)
                                .setMaxHit(33)
                                .setMultiAttack(false)
                                .setAnimation(new Animation(MAGE_ANIM))
                                .setAttackDelay(6)
                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(2010).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                })
                                .setOnHit(attack -> {
                                    attack.getVictim().asPlayer().startGraphic(new Graphic(1473, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
                                    attack.getVictim().asPlayer().prayerPoint = attack.getVictim().asPlayer().prayerPoint - 5;
                                })
                                .createNPCAutoAttack(),

                        new NPCAutoAttackBuilder()
                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                                .setCombatType(CombatType.MELEE)
                                .setDistanceRequiredForAttack(1)
                                .setHitDelay(2)
                                .setAnimation(new Animation(MELEE_ANIM))
                                .setMaxHit(40)
                                .setAttackDelay(6)
                                .createNPCAutoAttack()

                ));
                break;
        }

    }

    /*
    Gets a random target
     */
    Player getRandomTarget() {
        updateTargets();
        return targets.get(Misc.random(targets.size() - 1));
    }

    /*
    Gets the closest player to nex
     */
    void getNearestTarget(NPC npc) {
        updateTargets();
        int nearestDistance = 100;
        for (Player p : targets) {
            if (npc.getPosition().getAbsDistance(p.getPosition()) < nearestDistance) {
                nearestDistance = (int) npc.getPosition().getAbsDistance(p.getPosition());
                currenTarget = p;
            }
        }
    }

    private void spawnBloodReavers() {
        updateTargets();
        int amountToSpawn = 1;
        if(amountToSpawn > 8)
            amountToSpawn = 8;

        if(reavers.size() > 0) {
            for (NPC npc:
                    reavers) {
                npc.appendHeal(npc.getHealth().getCurrentHealth(), Hitmark.HEAL_PURPLE);
                reavers.remove(npc);
            }
        }

        for(int i = 0; i < amountToSpawn; i++) {
            reavers.add(NPCSpawning.spawnNpc(11293, targets.get(i).absX, targets.get(i).absY, targets.get(i).heightLevel, 0, 13));
        }
    }

    private void nexSiphon(NPC npc) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                npc.startAnimation(SIPHON_ANIM);
                siphoning = true;
                if(container.getTotalTicks() == 8) {
                    npc.startAnimation(-1);
                    siphoning = false;
                }
            }
        }, 1);
    }

    private void nexStartupEvent(NPC npc) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                switch (container.getTotalTicks()) {
//                    case 3:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Fumus!");
//                        npc.facePosition(fumusSpawnPoint);
//                        break;
//                    case 4:
//                        fumusNPC = NPCSpawning.spawnNpc(FUMUS, 2914, 5214, getHeight(), 0, 13);
//                        npc.getInstance().add(fumusNPC);
//                        break;
//                    case 6:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Umbra!");
//                        npc.facePosition(umbraSpawnPoint);
//                        break;
//                    case 7:
//                        umbraNPC = NPCSpawning.spawnNpc(UMBRA, 2936, 5214, npc.heightLevel, 0, 13);
//                        npc.getInstance().add(umbraNPC);
//                        break;
//                    case 9:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Cruor!");
//                        npc.facePosition(cruorSpawnPoint);
//                        break;
//                    case 10:
//                        cruorNPC = NPCSpawning.spawnNpc(CRUOR, 2914, 5192, npc.heightLevel, 0, 13);
//                        npc.getInstance().add(cruorNPC);
//                        break;
//                    case 12:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Glacies!");
//                        npc.facePosition(glaciesSpawnPoint);
//                        break;
//                    case 13:
//                        glaciesNPC = NPCSpawning.spawnNpc(GLACIES, 2936, 5192, npc.heightLevel, 0, 13);
//                        npc.getInstance().add(glaciesNPC);
//                        break;
                    case 14:
                        started = false;
                        canBeAttacked = true;
                        startNewPhase(currentPhase);
                        break;
                }
            }
        }, 1);
    }


    @Override
    public void process() {

        if(isAttacking) {
            attackSpeed--;
            if(attackSpeed <= 0) {
                attack();
            }
        }

        if(getHealth().getCurrentHealth() <= 0)
            onDeath();

        if(currentPhase == Phase.SMOKE &&
                getHealth().getCurrentHealth() <= 1900 && !fumusStarted) {
            forceChat("Fumus, don't fail me!");
           facePosition(fumusSpawnPoint);
                                    fumusNPC = NPCSpawning.spawnNpc(FUMUS, 2914, 5214, getHeight(), 0, 13);
                       getInstance().add(fumusNPC);
          canBeAttacked = false;
            //maybe spawn them here?
            fumusStarted = true;
        }
        else if(currentPhase == Phase.SHADOW &&
                getHealth().getCurrentHealth() <= 1500 && !umbraStarted) {
            forceChat("Umbra, don't fail me!");
            facePosition(umbraSpawnPoint);
                                    umbraNPC = NPCSpawning.spawnNpc(UMBRA, 2936, 5214, getHeight(), 0, 13);
                getInstance().add(umbraNPC);
          canBeAttacked = false;
            umbraStarted = true;
        } else if(currentPhase == Phase.BLOOD &&
                getHealth().getCurrentHealth() <= 1300 && !cruorStarted) {
            forceChat("Cruor, don't fail me!");
            facePosition(cruorSpawnPoint);
                                    cruorNPC = NPCSpawning.spawnNpc(CRUOR, 2914, 5192, getHeight(), 0, 13);
getInstance().add(cruorNPC);
        canBeAttacked = false;
            cruorStarted = true;
        } else if(currentPhase == Phase.ICE &&
                getHealth().getCurrentHealth() <= 650 && !glaciesStarted) {
            forceChat("Glacies, don't fail me!");
            facePosition(glaciesSpawnPoint);
                                    glaciesNPC = NPCSpawning.spawnNpc(GLACIES, 2936, 5192, getHeight(), 0, 13);
           getInstance().add(glaciesNPC);
       canBeAttacked = false;
            glaciesStarted = true;
        }

        if(fumusStarted && fumusNPC.getHealth().getCurrentHealth() <= 0) {
            startNewPhase(Phase.SHADOW);
        }
        if(umbraStarted && umbraNPC.getHealth().getCurrentHealth() <= 0) {
            startNewPhase(Phase.BLOOD);
        }
        if(cruorStarted && cruorNPC.getHealth().getCurrentHealth() <= 0) {
            startNewPhase(Phase.ICE);
        }
        if(glaciesStarted && glaciesNPC.getHealth().getCurrentHealth() <= 0) {
            startNewPhase(Phase.ZAROS);
        }
        for (NPC npc:
                reavers) {
            if(npc.getHealth().getCurrentHealth() <= 0 || npc.isDead)
                reavers.remove(npc);
        }
        if(!started) {
            updateTargets();
            for (Player p : targets) {
                if (p.getPosition().getAbsDistance(p.getPosition()) < 7) {
                    started = true;
                }
            }
        }
        specialTimer--;
    }
    public void onDeath() {
        //nexNPC.applyDead = true;
        removeNpcs();
        asNPC().startAnimation(9184);
        asNPC().forceChat("Taste my wrath!");
        new Wrath(this, targets);
        this.startGraphic(new Graphic(2013));
        super.onDeath();
        for (Player player : getInstance().getPlayers()) {
     //   for (Player player : PlayerHandler.getPlayers()) {
            getReward(player);
            player.getPA().movePlayer(2905, 5203, 0);
            //nexNPC.applyDead = true;
        }
    }
    public void removeNpcs() {
        if (fumusNPC != null && !fumusNPC.isDead) {
            fumusNPC.unregister();
        }

        if (cruorNPC != null && !cruorNPC.isDead) {
            cruorNPC.unregister();
        }

        if (glaciesNPC != null && !glaciesNPC.isDead) {
            glaciesNPC.unregister();
        }

        if (umbraNPC != null && !umbraNPC.isDead) {
            umbraNPC.unregister();
        }

        if (this.asNPC() != null && !this.isDead) {
            this.unregister();
        }

        for (NPC reaver : reavers) {
            if (reaver != null && !reaver.isDead) {
                reaver.unregister();
            }
        }
    }
    /*
        Enum of nex phases
         */
    enum Phase {
        SMOKE,
        SHADOW,
        BLOOD,
        ICE,
        ZAROS
    }
}


//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class NexNPC extends NPC {
//
//    public static NexNPC nexnpc;
//
//    public static boolean isSpawned() {
//        return nexnpc != null;
//    }
//
//    public static final Position SPAWN_POSITION = new Position(2925, 5203, 0);
//
//    final int MELEE_ANIM = 9181;
//    final int MAGE_ANIM = 9179;
//    final int RANGE_ANIM = 9180;
//    final int SIPHON_ANIM = 9183;
//    private int attackSpeed = 4;
//    private boolean isAttacking = false;
//
//    static final int NEX = 11278;
//    final int CRUOR = 11285;
//    final int FUMUS = 11283;
//    final int UMBRA = 11284;
//    final int GLACIES = 11286;
//    public boolean fumusStarted = false;
//    public boolean cruorStarted = false;
//    public boolean umbraStarted = false;
//    public boolean glaciesStarted = false;
//    NPC glaciesNPC;
//    NPC cruorNPC;
//    NPC umbraNPC;
//    NPC nexNPC;
//    NPC fumusNPC;
//    Position fumusSpawnPoint = new Position(2914, 5214, 0);
//    Position umbraSpawnPoint = new Position(2936, 5214, 0);
//    Position cruorSpawnPoint = new Position(2914, 5192, 0);
//    Position glaciesSpawnPoint = new Position(2936, 5192, 0);
//    int spawnMageAnimation = 9189;
//  public List<Player> targets;
//   public Phase currentPhase;
//    Player currenTarget;
//    int specialTimer = 75;
//    boolean canBeAttacked = true;
//    boolean started = false;
//    public boolean siphoning = false;
//
//    List<NPC> reavers = new ArrayList<>();
//
//    public NexNPC(int npcId, Position position) {
//        super(npcId, position);
//        nexnpc = this;
//        resetForSpawn();
//        loadItems();
//
//
//    }
//
//    public void resetForSpawn() {
//       // walkingType = 1;
//        getBehaviour().setWalkHome(false);
//        getBehaviour().setAggressive(true);
//
//        if (getNpcId() == Npcs.NEX) {
//            getBehaviour().setRespawn(true);
//        } else {
//            getBehaviour().setRespawn(false);
//        }
//
//    }
//    private final Map<LootRarity, List<GameItem>> items = new HashMap<>();
//    private static final Map<LootRarity, List<GameItem>> itemz = new HashMap<>();
//
//    public void loadItems() {
//        if (items.isEmpty()) {
//            items.put(LootRarity.COMMON, Arrays.asList(
//                    new GameItem(Items.DEATH_RUNE, 325),
//                    new GameItem(Items.BLOOD_RUNE, 170),
//                    new GameItem(Items.SOUL_RUNE, 227),
//                    new GameItem(Items.DRAGON_BOLTS_UNF, 90),
//                    new GameItem(Items.CANNONBALL, 298),
//                    new GameItem(Items.AIR_RUNE, 1365),
//                    new GameItem(Items.FIRE_RUNE, 1655),
//                    new GameItem(Items.WATER_RUNE, 1599),
//                    new GameItem(Items.ONYX_BOLTS_E, 29),
//                    new GameItem(Items.AIR_ORB_NOTED, 20),
//                    new GameItem(Items.UNCUT_RUBY_NOTED, 26),
//                    new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
//                    new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
//                    new GameItem(Items.COAL_NOTED, 95),
//                    new GameItem(Items.RUNITE_ORE_NOTED, 28),
//                    new GameItem(Items.SHARK, 3),
//                    new GameItem(Items.PRAYER_POTION4, 1),
//                    new GameItem(Items.SARADOMIN_BREW4, 2),
//                    new GameItem(Items.SUPER_RESTORE4, 1),
//                    new GameItem(Items.COINS, 26748)
//            ));
//
//            items.put(LootRarity.RARE, Arrays.asList(
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(19841, 1),
//                    new GameItem(26235, 1),
//
//                    new GameItem(26382),
//                    new GameItem(26382),
//                    new GameItem(26382),
//
//                    new GameItem(26384),
//                    new GameItem(26384),
//                    new GameItem(26384),
//
//                    new GameItem(26386),
//                    new GameItem(26386),
//                    new GameItem(26386),
//
//                    new GameItem(26233),
//                    new GameItem(26374)
//
//            ));
//        }
//    }
//
//    static {
//        itemz.put(LootRarity.COMMON, Arrays.asList(
//                new GameItem(Items.DEATH_RUNE, 325),
//                new GameItem(Items.BLOOD_RUNE, 170),
//                new GameItem(Items.SOUL_RUNE, 227),
//                new GameItem(Items.DRAGON_BOLTS_UNF, 90),
//                new GameItem(Items.CANNONBALL, 298),
//                new GameItem(Items.AIR_RUNE, 1365),
//                new GameItem(Items.FIRE_RUNE, 1655),
//                new GameItem(Items.WATER_RUNE, 1599),
//                new GameItem(Items.ONYX_BOLTS_E, 29),
//                new GameItem(Items.AIR_ORB_NOTED, 20),
//                new GameItem(Items.UNCUT_RUBY_NOTED, 26),
//                new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
//                new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
//                new GameItem(Items.COAL_NOTED, 95),
//                new GameItem(Items.RUNITE_ORE_NOTED, 28),
//                new GameItem(Items.SHARK, 3),
//                new GameItem(Items.PRAYER_POTION4, 1),
//                new GameItem(Items.SARADOMIN_BREW4, 2),
//                new GameItem(Items.SUPER_RESTORE4, 1),
//                new GameItem(Items.COINS, 26748)
//        ));
//
//        itemz.put(LootRarity.RARE, Arrays.asList(
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(19841, 1),
//                new GameItem(26235, 1),
//
//                new GameItem(26382),
//                new GameItem(26382),
//                new GameItem(26382),
//
//                new GameItem(26384),
//                new GameItem(26384),
//                new GameItem(26384),
//
//                new GameItem(26386),
//                new GameItem(26386),
//                new GameItem(26386),
//
//                new GameItem(26233),
//                new GameItem(26374)
//        ));
//    }
//    public static ArrayList<GameItem> getAllDrops() {
//        ArrayList<GameItem> drops = new ArrayList<>();
//        itemz.forEach((lootRarity, gameItems) -> {
//            gameItems.forEach(g -> {
//                if (!drops.contains(g)) {
//                    drops.add(g);
//                }
//            });
//        });
//        return drops;
//    }
//    public void getReward(Player player) {
//        for (GameItem randomItem : getRandomItems()) {
//            if (player.getInventory().freeInventorySlots() < 1) {
//                player.getInventory().addToBank(new ImmutableItem(randomItem));
//                player.sendMessage(randomItem.getDef().getName() + ", Has been added to your bank!");
//            } else {
//                player.getInventory().addOrDrop(new ImmutableItem(randomItem));
//            }
//            if (items.get(LootRarity.RARE).contains(randomItem)) {
//                player.getCollectionLog().handleDrop(player, 11278, randomItem.getId(), randomItem.getAmount(), true);
//            }
//        }
//    }
//    public List<GameItem> getRandomItems() {
//        List<GameItem> rewards = Lists.newArrayList();
//        int rareChance = 10;
//
//        if (Misc.random(1, rareChance) == 1) {
//            rewards.add(Misc.getRandomItem(items.get(LootRarity.RARE)));
//        } else {
//            for (int count = 0; count < 2; count++) {
//                rewards.add(Misc.getRandomItem(items.get(LootRarity.COMMON)));
//            }
//        }
//        return rewards;
//    }
//    public static Map<LootRarity, List<GameItem>> getItems() {
//        return itemz;
//    }
//
//    public static ArrayList<GameItem> getRareDrops() {
//        ArrayList<GameItem> drops = new ArrayList<>();
//        List<GameItem> found = getItems().get(LootRarity.RARE);
//        for(GameItem f : found) {
//            boolean foundItem = false;
//            for(GameItem drop : drops) {
//                if (drop.getId() == f.getId()) {
//                    foundItem = true;
//                    break;
//                }
//            }
//            if (!foundItem) {
//                drops.add(f);
//            }
//        }
//        return drops;
//    }
//
//    /*
//     Gets the targets in the area
//      */
//    public void updateTargets() {
//        targets = PlayerHandler.getPlayers().stream().filter(plr ->
//               Boundary.NEX.in(plr)).collect(Collectors.toList());
//        if(targets == null || targets.size() == 0)
//            onDeath(false);
//
//    }
//
//    /*
//    Initialising Nex
//     */
//    public void init() {
//
//        currentPhase = Phase.SMOKE;
//        setAttacks();
//        canBeAttacked = true;
//        nexStartupEvent(this);
//
//    }
//    public void startNewPhase(Phase newPhase) {
//        currentPhase = newPhase;
//        canBeAttacked = true;
//        setAttacks();
//        switch (newPhase) {
//            case SMOKE:
//                isAttacking = true;
//                asNPC().forceChat("Fill my soul with smoke!");
//                break;
//            case SHADOW:
//                asNPC().forceChat("Darken my shadow!");
//                asNPC().forceChat("There is... NO ESCAPE!");
//            break;
//            case BLOOD:
//                asNPC().forceChat("Flood my lungs with blood!");
//                break;
//            case ICE:
//                asNPC().forceChat("Infuse me with the power of ice!");
//                break;
//            case ZAROS:
//                asNPC().forceChat("NOW, THE POWER OF ZAROS!");
//                asNPC().appendHeal(500, Hitmark.HEAL_PURPLE);
//                break;
//        }
//    }
//
//
//    @Override
//    public boolean canBeAttacked(Entity entity) {
//        return canBeAttacked;
//    }
//
//    private void meleeAttack(Player target) {
//        asNPC().startAnimation (MELEE_ANIM);
//        target.appendDamage(Misc.random(0,33), Hitmark.HIT);
//    }
//
//
//    public void sendProjectile(Player target, CombatProjectile projectile, CombatType combatType, int maxDamage) {
//        int size = (int) Math.ceil((double) asNPC().getSize() / 2.0);
//        int centerX = asNPC().getX() + size;
//        int centerY = asNPC().getY() + size;
//        int offsetX = (centerY - target.getY()) * -1;
//        int offsetY = (centerX - target.getX()) * -1;
//        currenTarget.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(),
//                projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
//        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
//            @Override
//            public void execute(CycleEventContainer container) {
//                int distance = (int) target.getPosition().getAbsDistance(asNPC().getPosition());
//                int ticks;
//                if(distance <= 5) {
//                    ticks = 1;
//                } else if (distance > 5 && distance < 9) {
//                    ticks = 2;
//                } else if(distance >= 9 && distance < 14) {
//                    ticks = 3;
//                } else {
//                    ticks = 4;
//                }
//                if(container.getTotalTicks() == ticks && target != null){
//                    int damage = Misc.random(maxDamage);
//                    if(combatType.equals(CombatType.MAGE) && target.protectingMagic())
//                        damage *= 0.5;
//                    else if(combatType.equals(CombatType.RANGE) && target.protectingRange())
//                        damage *= 0.5;
//                    target.appendDamage(damage, Hitmark.HIT);
//                }
//            }
//        }, 1);
//    }
//
//    private static final CombatProjectile SMOKE_RUSH_PROJECTILE = new CombatProjectile(384, 50, 25, 4, 50, 0, 50);
//    private static final CombatProjectile SHADOW_RANGE_PROJECTILE = new CombatProjectile(2012, 50, 25, 4, 50, 0, 50);
//    private static final CombatProjectile BASIC_MAGIC_PROJECTILE = new CombatProjectile(2004, 50, 25, 4, 50, 0, 50);
//
//    private void attack() {
//        attackSpeed = 5;
//        updateTargets();
//        currenTarget = getRandomTarget();
//        if(currenTarget == null) {
//            onDeath(false);
//            return;
//        }
//        switch (currentPhase) {
//            case SMOKE:
//                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
//                    meleeAttack(currenTarget);
//                    this.startAnimation(MELEE_ANIM);
//                } else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    specialTimer = 75;
//                    this.forceChat("Let the virus flow through you!");
//                    this.startAnimation(9188);
//                    new ChokeAttack(getRandomTarget());
//                } else {
//                    for (Player player:
//                            targets) {
//                        this.startAnimation(MAGE_ANIM);
//                        sendProjectile(player, SMOKE_RUSH_PROJECTILE, CombatType.MAGE, 33);
//                    }
//                }
//                break;
//            case SHADOW:
//                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
//                    meleeAttack(currenTarget);
//                    this.startAnimation(MELEE_ANIM);
//                }
//                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    specialTimer = 75;
//                    this.forceChat("Fear the shadow!");
//                    this.startAnimation(9188);
//                    new ShadowSmash(targets);
//                } else {
//                    for (Player player :
//                            targets) {
//                        sendProjectile(player, SHADOW_RANGE_PROJECTILE, CombatType.RANGE, 35);
//                        this.startAnimation(RANGE_ANIM);
//                    }
//                }
//                break;
//            case BLOOD:
//                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
//                    meleeAttack(currenTarget);
//                    this.startAnimation(MELEE_ANIM);
//                }
//                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    specialTimer = 75;
//                    new BloodSacrifice(asNPC(), getRandomTarget());
//                    this.forceChat("I demand a blood sacrifice!");
//                }
//                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    specialTimer = 75;
//                    spawnBloodReavers();
//                    this.forceChat("A siphon will solve this!");
//                    nexSiphon(this);
//                    this.startAnimation(SIPHON_ANIM);
//                } else {
//                    new BloodBarrage(this, getRandomTarget(), targets, Misc.random(33));
//                    this.startAnimation(MAGE_ANIM);
//                }
//                break;
//            case ICE:
//                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
//                    meleeAttack(currenTarget);
//                    this.startAnimation(9181);
//                }
//                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    this.forceChat("Contain this!");
//                    updateTargets();
//                    new Containment(this, targets);
//                    this.startAnimation(9186);
//                    specialTimer = 75;
//                }
//                else if(Misc.random(0, 10) == 0 && specialTimer <= 0) {
//                    this.forceChat("Die now, in a prison of ice!");
//                    updateTargets();
//                    new IcePrison(targets);
//                    this.startAnimation(MAGE_ANIM);
//                    specialTimer = 75;
//                } else {
//                    Player target = getRandomTarget();
//                    if(target == null)
//                        return;
//                    new IceBarrage(target, targets);
//                    this.startAnimation(MAGE_ANIM);
//                    target.freezeDelay = 15;
//                }
//                break;
//            case ZAROS:
//                if(currenTarget.getPosition().getAbsDistance(asNPC().getPosition()) < 1 && Misc.random(2) == 0) {
//                    meleeAttack(currenTarget);
//                    this.startAnimation(9181);
//                }
//                else {
//                    for (Player player:
//                            targets) {
//                        sendProjectile(player, BASIC_MAGIC_PROJECTILE, CombatType.MAGE, 40);
//                        this.startAnimation(MAGE_ANIM);
//                    }
//                }
//                break;
//        }
//    }
//
//    private void setAttacks() {
//     //  System.out.println("test1");
//        switch (currentPhase) {
//            case SMOKE:
//           //     System.out.println("test2");
//                setNpcAutoAttacks(Lists.newArrayList(
//                        new NPCAutoAttackBuilder()//a random attack
//                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 0 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    System.out.println("test3");
//                                    this.forceChat("Let the virus flow through you!");
//                                    new ChokeAttack(getRandomTarget());
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//                        /**
//                         * Magic attack
//                         */
//                        new NPCAutoAttackBuilder()
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(4)
//                                .setMaxHit(33)
//                                .setMultiAttack(true)
//                                .setAnimation(new Animation(MAGE_ANIM))//magic attack
//                                .setAttackDelay(6)
//                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(384).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
//                                .setOnHit(attack -> {
//                                    System.out.println("test4");
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                })
//                                .setOnHit(attack -> {
//                                    System.out.println("test5");
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(385, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                    if(Misc.random(4) == 0)
//                                        System.out.println("test6");
//                                    attack.getVictim().getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(this));
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
//                                .setCombatType(CombatType.MELEE)
//                                .setDistanceRequiredForAttack(1)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(MELEE_ANIM))
//                                .setMaxHit(30)
//                                .setAttackDelay(6)
//                                .createNPCAutoAttack()
//
//                ));
//                break;
//            case SHADOW:
//                System.out.println("test777");
//                setNpcAutoAttacks(Lists.newArrayList(
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 0 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    this.forceChat("Fear the shadow!");
//                                    updateTargets();
//                                    new ShadowSmash(targets);
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//                        /**
//                         * Magic attack
//                         */
//                        new NPCAutoAttackBuilder()
//                                .setCombatType(CombatType.RANGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(4)
//                                .setMaxHit(33)
//                                .setMultiAttack(true)
//                                .setAnimation(new Animation(RANGE_ANIM))
//                                .setAttackDelay(6)
//                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(2012).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
//                                .setCombatType(CombatType.MELEE)
//                                .setDistanceRequiredForAttack(1)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(MELEE_ANIM))
//                                .setMaxHit(30)
//                                .setAttackDelay(6)
//                                .createNPCAutoAttack()
//                ));
//                break;
//            case BLOOD:
//                setNpcAutoAttacks(Lists.newArrayList(
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    this.forceChat("I demand a blood sacrifice!");
//                                    new BloodSacrifice(this, getRandomTarget());
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(10) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    this.forceChat("A siphon will solve this!");
//                                    spawnBloodReavers();
//                                    nexSiphon(this);
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//
//
//                        /**
//                         * Magic attack
//                         */
//                        new NPCAutoAttackBuilder()
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(4)
//                                .setMaxHit(33)
//                                .setMultiAttack(false)
//                                .setAnimation(new Animation(MAGE_ANIM))
//                                .setAttackDelay(6)
//                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(-1).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                })
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(377, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                    new BloodBarrage(this, attack.getVictim().asPlayer(), targets, attack.getCombatHit().getDamage());
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
//                                .setCombatType(CombatType.MELEE)
//                                .setDistanceRequiredForAttack(1)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(MELEE_ANIM))
//                                .setMaxHit(30)
//                                .setAttackDelay(6)
//                                .createNPCAutoAttack()
//
//                ));
//                break;
//            case ICE:
//                setNpcAutoAttacks(Lists.newArrayList(
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(6) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    this.forceChat("Contain this!");
//                                    updateTargets();
//                                    new Containment(this, targets);
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(6) == 2 && specialTimer <= 0)//&& !isImmune(attack.getEntity().asPlayer()))
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(-1))
//                                .setMaxHit(0)
//                                .setAttackDelay(5)
//                                .setOnAttack(attack -> {
//                                    this.forceChat("Die now, in a prison of ice!");
//                                    updateTargets();
//                                    new IcePrison(targets);
//                                    specialTimer = 75;
//                                })
//                                .createNPCAutoAttack(),
//
//
//
//                        /**
//                         * Magic attack
//                         */
//                        new NPCAutoAttackBuilder()
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(4)
//                                .setMaxHit(33)
//                                .setMultiAttack(false)
//                                .setAnimation(new Animation(MAGE_ANIM))
//                                .setAttackDelay(6)
//                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(-1).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                })
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(369, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                    new IceBarrage(attack.getVictim().asPlayer(), targets);
//                                    attack.getVictim().asPlayer().freezeDelay = 15;
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
//                                .setCombatType(CombatType.MELEE)
//                                .setDistanceRequiredForAttack(1)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(MELEE_ANIM))
//                                .setMaxHit(30)
//                                .setAttackDelay(6)
//                                .createNPCAutoAttack()
//
//                ));
//                break;
//            case ZAROS:
//                setNpcAutoAttacks(Lists.newArrayList(
//                        /**
//                         * Magic attack
//                         */
//                        new NPCAutoAttackBuilder()
//                                .setCombatType(CombatType.MAGE)
//                                .setDistanceRequiredForAttack(20)
//                                .setHitDelay(4)
//                                .setMaxHit(33)
//                                .setMultiAttack(false)
//                                .setAnimation(new Animation(MAGE_ANIM))
//                                .setAttackDelay(6)
//                                .setProjectile(new ProjectileBaseBuilder().setProjectileId(2010).setCurve(16).setSpeed(50).setSendDelay(3).createProjectileBase())
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(85, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                })
//                                .setOnHit(attack -> {
//                                    attack.getVictim().asPlayer().startGraphic(new Graphic(1473, Graphic.GraphicHeight.HIGH));//85 if fail 140 is hit
//                                    attack.getVictim().asPlayer().prayerPoint = attack.getVictim().asPlayer().prayerPoint - 5;
//                                })
//                                .createNPCAutoAttack(),
//
//                        new NPCAutoAttackBuilder()
//                                .setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
//                                .setCombatType(CombatType.MELEE)
//                                .setDistanceRequiredForAttack(1)
//                                .setHitDelay(2)
//                                .setAnimation(new Animation(MELEE_ANIM))
//                                .setMaxHit(40)
//                                .setAttackDelay(6)
//                                .createNPCAutoAttack()
//
//                ));
//                break;
//        }
//
//    }
//
//    /*
//    Gets a random target
//     */
//    Player getRandomTarget() {
//        updateTargets();
//if(targets.size()> 0)
//    return targets.get(Misc.random(targets.size() - 1));
//        //return targets == null ? null : targets.get(Misc.random(targets.size() - 1));
//        return null;
//    }
//
//    /*
//    Gets the closest player to nex
//     */
//    void getNearestTarget(NPC npc) {
//        updateTargets();
//        int nearestDistance = 100;
//        for (Player p : targets) {
//            if (npc.getPosition().getAbsDistance(p.getPosition()) < nearestDistance) {
//                nearestDistance = (int) npc.getPosition().getAbsDistance(p.getPosition());
//                currenTarget = p;
//            }
//        }
//    }
//
//    private void spawnBloodReavers() {
//        updateTargets();
//        int amountToSpawn = targets.size() - 1;
//        if(amountToSpawn > 8)
//            amountToSpawn = 8;
//
//        if(reavers.size() > 0) {
//            for (NPC npc:
//                    reavers) {
//                npc.appendHeal(npc.getHealth().getCurrentHealth(), Hitmark.HEAL_PURPLE);
//                reavers.remove(npc);
//            }
//        }
//
//        for(int i = 0; i < amountToSpawn; i++) {
//            reavers.add(NPCSpawning.spawnNpc(11293, targets.get(i).absX, targets.get(i).absY, targets.get(i).heightLevel, 0, 13));
//        }
//    }
//
//    private void nexSiphon(NPC npc) {
//        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
//            @Override
//            public void execute(CycleEventContainer container) {
//                npc.startAnimation(SIPHON_ANIM);
//                siphoning = true;
//                if(container.getTotalTicks() == 8) {
//                    npc.startAnimation(-1);
//                    siphoning = false;
//                }
//            }
//        }, 1);
//    }
//
//    private void nexStartupEvent(NPC npc) {
//        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
//            @Override
//            public void execute(CycleEventContainer container) {
//                switch (container.getTotalTicks()) {
//                    case 3:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Fumus!");
//                        npc.facePosition(fumusSpawnPoint);
//                        break;
//                    case 4:
//                        fumusNPC = NPCSpawning.spawnNpc(FUMUS, 2914, 5214, getHeight(), 1, 13);
//                        fumusNPC.getBehaviour().setRespawn(false);
//                        fumusNPC.needRespawn = false;
//                     //  nexnpc.getInstance().add(fumusNPC);
//                        break;
//                    case 6:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Umbra!");
//                        npc.facePosition(umbraSpawnPoint);
//                        break;
//                    case 7:
//                        umbraNPC = NPCSpawning.spawnNpc(UMBRA, 2936, 5214, getHeight(), 1, 13);
//                        umbraNPC.getBehaviour().setRespawn(false);
//                        umbraNPC.needRespawn = false;
//                    //    nexnpc.getInstance().add(umbraNPC);
//                        break;
//                    case 9:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Cruor!");
//                        npc.facePosition(cruorSpawnPoint);
//                        break;
//                    case 10:
//                        cruorNPC = NPCSpawning.spawnNpc(CRUOR, 2914, 5192, getHeight(), 1, 13);
//                        cruorNPC.getBehaviour().setRespawn(false);
//                        cruorNPC.needRespawn = false;
//                     //   nexnpc.getInstance().add(cruorNPC);
//                        break;
//                    case 12:
//                        npc.startAnimation(spawnMageAnimation);
//                        npc.forceChat("Glacies!");
//                        npc.facePosition(glaciesSpawnPoint);
//                        break;
//                    case 13:
//                        glaciesNPC = NPCSpawning.spawnNpc(GLACIES, 2936, 5192, getHeight(), 1, 13);
//                        glaciesNPC.getBehaviour().setRespawn(false);
//                        glaciesNPC.needRespawn = false;
//                     //   nexnpc.getInstance().add(glaciesNPC);
//                        break;
//                    case 14:
//                        started = false;
//                        canBeAttacked = true;
//                        startNewPhase(currentPhase);
//                        break;
//                }
//            }
//        }, 1);
//    }
//
//
//    @Override
//    public void process() {
//if(currentPhase == null)
//    return;
//        if(isAttacking) {
//            attackSpeed--;
//            if(attackSpeed <= 0) {
//                attack();
//            }
//        }
//
//        if(getHealth().getCurrentHealth() <= 0)
//            onDeath(false);
//
//        if(currentPhase == Phase.SMOKE &&
//                getHealth().getCurrentHealth() <= 3300) {
//            forceChat("Fumus, don't fail me!");
//            canBeAttacked = false;
//            fumusStarted = true;
//        }
//        else if(currentPhase == Phase.SHADOW &&
//                getHealth().getCurrentHealth() <= 2700) {
//            forceChat("Umbra, don't fail me!");
//            canBeAttacked = false;
//            umbraStarted = true;
//        } else if(currentPhase == Phase.BLOOD &&
//                getHealth().getCurrentHealth() <= 1300) {
//            forceChat("Cruor, don't fail me!");
//            canBeAttacked = false;
//            cruorStarted = true;
//        } else if(currentPhase == Phase.ICE &&
//                getHealth().getCurrentHealth() <= 650) {
//            forceChat("Glacies, don't fail me!");
//            canBeAttacked = false;
//            glaciesStarted = true;
//        }
//
//        if(fumusStarted && fumusNPC.getHealth().getCurrentHealth() <= 0) {
//            startNewPhase(Phase.SHADOW);
//        }
//        if(umbraStarted && umbraNPC.getHealth().getCurrentHealth() <= 0) {
//            startNewPhase(Phase.BLOOD);
//        }
//        if(cruorStarted && cruorNPC.getHealth().getCurrentHealth() <= 0) {
//            startNewPhase(Phase.ICE);
//        }
//        if(glaciesStarted && glaciesNPC.getHealth().getCurrentHealth() <= 0) {
//            startNewPhase(Phase.ZAROS);
//        }
//        for (NPC npc:
//                reavers) {
//            if(npc.getHealth().getCurrentHealth() <= 0 || npc.isDead)
//                reavers.remove(npc);
//        }
//        if(!started) {
//            updateTargets();
//            for (Player p : targets) {
//                if (p.getPosition().getAbsDistance(p.getPosition()) < 7) {
//                    started = true;
//                }
//            }
//        }
//        specialTimer--;
//    }
//    public void onDeath(boolean giverewards) {
//        removeNpcs();
//        resetForSpawn();
////        if(giverewards){
////            PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(p -> {
//////                if (!eventCompleted) {
//////                    p.sendMessage("@blu@AngelOfDeath event was ended before she was killed!");
//////                } else {
//////                    if (p.getNexAngelOfDeathDamageCounter() >= 200) {
//////                        p.sendMessage("@blu@AngelOfDeath has been killed!");
//////                    } else {
//////                        p.sendMessage("@blu@You were not active enough to receive a reward.");
//////                    }
//////                }
////                this.damageTaken.contain
////                getReward(p);
////            });
//
//            Entity killer = calculateKiller();
//
//        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(plr -> {
//
//                    if (killer != null && killer.equals(plr)) {
//                        getReward(plr);
//                    } else {
//                        //here?
//                    }
//
//                });
//
//
//
//        }
////        if(!giverewards){
////            removeNpcs();
////            super.onDeath();
////
////            return;
////        }
////        removeNpcs();
////        asNPC().startAnimation(9184);
////        asNPC().forceChat("Taste my wrath!");
////        new Wrath(this, targets);
////        this.startGraphic(new Graphic(2013));
////        super.onDeath();
//
//
//
////    @Override
////    public void onDeath() {
////        System.out.println("here>?");
//// //onDeath(true);
////            resetForSpawn();
////
////
////    }
//    public void removeNpcs() {
////        System.out.println("remove npc");
//        currentPhase = null;
//        if (fumusNPC != null && !fumusNPC.isDead) {
//            fumusNPC.unregister();
//        }
//
//        if (cruorNPC != null && !cruorNPC.isDead) {
//            cruorNPC.unregister();
//        }
//
//        if (glaciesNPC != null && !glaciesNPC.isDead) {
//            glaciesNPC.unregister();
//        }
//
//        if (umbraNPC != null && !umbraNPC.isDead) {
//            umbraNPC.unregister();
//        }
//
//        if (this.asNPC() != null && !this.isDead) {
//            this.unregister();
//        }
//
//        for (NPC reaver : reavers) {
//            if (reaver != null && !reaver.isDead) {
//                reaver.unregister();
//            }
//        }
//    }
//    /*
//        Enum of nex phases
//         */
//    enum Phase {
//        SMOKE,
//        SHADOW,
//        BLOOD,
//        ICE,
//        ZAROS
//    }
//}
