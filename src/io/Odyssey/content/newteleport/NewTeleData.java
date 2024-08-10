package io.Odyssey.content.newteleport;


import io.Odyssey.content.teleportation.newest.PortalTeleports;
import io.Odyssey.content.teleportation.newest.ui.ui.CombatTeleportInfo;
import io.Odyssey.model.entity.player.Position;

import static io.Odyssey.content.newteleport.NewTeleInterface.*;
import static io.Odyssey.content.newteleport.TeleDifficulty.*;

public enum NewTeleData{

    /*
    Training Monsters Teleports
     */
    CHICKEN(new Position(3237, 3296,0), "Chicken", trainingteleporticons+" @gre@1-10", TRAINING, EASY),
    CRABS(new Position(2670, 3715), "Rock Crabs", trainingteleporticons+"@yel@10-60", TRAINING, EASY,10553),
    SAND_CRABS(new Position(1795,3465), "Sand Crabs", trainingteleporticons+"@yel@10-60", TRAINING, EASY),
    EXPERIMENTS(new Position(3558,9948), "Experiments", trainingteleporticons+"@yel@10-60", TRAINING, EASY,14135),
    MENANDWOMEN(new Position(3235, 3211), "Lumbridge", trainingteleporticons+"@gre@10-20", TRAINING, EASY),
    ALKHARID(new Position(3292, 3171), "Al Kharid Warriors", trainingteleporticons+"@gre@20-30", TRAINING, EASY,13106),
    CHAOS_DRUID(new Position(3131, 9916), "Chaos Druids", trainingteleporticons+"@yel@30-60", TRAINING, MEDIUM, 12342),
    MOSS_GIANTS(new Position(3156, 9906), "Moss Giants", trainingteleporticons+"@yel@40-60", TRAINING, MEDIUM),


  //  WILDERNESS_SLAYER(new Position(3108, 3513), "Wilderness Slayer", trainingteleporticons+"@yel@60-70", TRAINING, MEDIUM),
//   GARGOYLES(new Position(3108, 3513), "Gargoyles", trainingteleporticons+"@red@80-90", TRAINING, HARD,13623),
    //RUNE_DRAGONS(new Position(1567, 5061), "Rune Dragons", trainingteleporticons+"@red@90-99", TRAINING, HARD,6223),

    /*
    Dungeon Teleports
     */
    SLAYER_1(new Position(3096, 3468), "Edgeville Dungeon", "@gre@Easy", SLAYING, EASY,12342),
    SLAYER_7(new Position(2884, 9796), "Taverley Dungeon", "@gre@Easy", SLAYING, MEDIUM,11573),

    //SLAYER_2(new Position(2404, 9415), "Smoke Devils", "@yel@Medium", SLAYING, MEDIUM),

    SLAYER_17(new Position(3168,3172), "Lumbridge Swamp Caves", "@gre@Easy", SLAYING, MEDIUM),
    SLAYER_5(new Position(2431, 3425), "Stronghold Cave", "@yel@Medium", SLAYING, MEDIUM),
    SLAYER_6(new Position(2805, 10001), "Relleka Dungeon", "@yel@Medium", SLAYING, MEDIUM,10553),
    SLAYER_681(new Position(3740, 9373), "Mos Le'Harmless Cave", "@yel@Medium", SLAYING, MEDIUM),
   SLAYER_8(new Position(2710, 9564), "Brimhaven Dungeon", "@yel@Medium", SLAYING, MEDIUM),
    SLAYER_9(new Position(1567, 5061), "Lithriken Vault", "@red@Hard", SLAYING, HARD,6223),
    SLAYER_3(new Position(3428, 3534), "Slayer Tower", "@red@Hard", SLAYING, MEDIUM,13623),
    SLAYER_4(new Position(1661, 10049), "Catacombs", "@red@Hard", SLAYING, MEDIUM),
    SLAYER_99(new Position(2412,3060), "Smoke Devil Dungeon", "@red@Hard", SLAYING, MEDIUM),
    SLAYER_66(new Position(1772,5366), "Mithril Dragons", "@red@Hard", SLAYING, HARD),
    SLAYER_67(new Position(2130, 5647), "Demonic Gorillas", "@red@Hard", SLAYING, HARD),
    SLAYER_68(new Position(3596, 10291), "Fossil Island Wyverns", "@red@Hard", SLAYING, HARD),
    //		FOSSIL_ISLAND_WYVERNS("Fossil Island Wyverns", Tab.MONSTERS, 63152, new Position(3596, 10291), false, CombatTeleportInfo.FOSSIL_ISLAND_WYVERNS),
   // DEMONIC_GORILLAS("Demonic Gorillas", PortalTeleports.Tab.MONSTERS, 63142, new Position(2130, 5647, 0), CombatTeleportInfo.DEMONIC_GORILLA),

    //		MITHRIL_DRAGONS("Mithril Dragons", Tab.MONSTERS, 63139, new Position(1746, 5323, 0), false, CombatTeleportInfo.MITHRIL_DRAGON),

   // SLAYER_9(new Position(3029, 9582), "Wyvern Dungeon", "@red@Hard", SLAYING, MEDIUM),
    SLAYER_10(new Position(1311, 10205), "Karuulm Dungeon", "@red@Hard", SLAYING, MEDIUM),
   // SLAYER_11(new Position(3596, 10291), "Fossil Island Wyverns", "@red@Hard", SLAYING, MEDIUM),
   // SLAYER_12(new Position(2797, 10015), "Cave Horrors", "@red@Hard", SLAYING, MEDIUM),

    /*
    Bosses Teleports
     */
    BOSSING_13(new Position(2998, 3380), "Giant Mole", "@gre@Easy", BOSSING, EASY,6993),
    BOSSING_211(new Position(3095,9831), "Obor", "@gre@Easy", BOSSING, EASY,12441),
    BOSSING_2111(new Position(3173,9899), "Bryophyta", "@gre@Easy", BOSSING, EASY,12955),
    BOSSING_10(new Position(2271, 4680), "King Black Dragon", "@yel@Medium", BOSSING, MEDIUM,9033),
    BOSSING_6(new Position(2276, 9988), "Kraken", "@yel@Medium", BOSSING, MEDIUM,9116),
    BOSSING_8(new Position(2547, 3758), "Dagannoth Kings", "@yel@Medium", BOSSING, MEDIUM,10042),

    BOSSING_11(new Position(3508, 9493), "Kalphite Queen", "@yel@Medium", BOSSING, MEDIUM,13972),
    BOSSING_101(new Position(2970, 4771), "Abyssal Sire", "@yel@Medium", BOSSING, MEDIUM),
    LIZARDMAN_SHAMAN(new Position(1469, 3687), "Lizardman Shaman", "@yel@Medium", BOSSING, MEDIUM),
    BOSSING_118(new Position(2964, 4382,2), "Corporeal Beast", "@yel@Hard", BOSSING, HARD,10042),
  BOSSING_3(new Position(2275, 4037), "Vorkath", "@red@Hard", BOSSING, HARD,9023),
    BOSSING_311(new Position(3227,6117), "The Gauntlet", "@red@Hard", BOSSING, HARD),
    BOSSING_2(new Position(2202, 3056), "Zulrah", "@red@Hard", BOSSING, HARD),
    BOSSING_5(new Position(2873, 9847), "Cerberus", "@red@Hard", BOSSING, HARD),
  BOSSING_100(new Position(1354, 10259), "Alchemical Hydra", "@red@Hard", BOSSING, HARD),
//    BOSSING_123(new Position(3427,3540,2), "Grotesque Guardians", "@red@Hard", BOSSING, HARD),//todo: fix the map or something idk
    BOSSING_9(new Position(2964, 4382), "Corporeal Beast", "@red@Hard", BOSSING, HARD),
    BOSSING_91(new Position(1665,10045), "Skotizo", "@red@Hard", BOSSING, HARD),
    BOSSING_1(new Position(2916, 3746),"God Wars", "@red@Hard", BOSSING, HARD),
    BOSSING_12(new Position(1645, 3571,1), "Mimic", "@red@Hard", BOSSING, HARD),
    BOSSING_14(new Position(1842, 9914,0), "Sarachnis", "@red@Hard", BOSSING, HARD),
    BOSSING_15(new Position(3808,9744,1), "Nightmare", "@red@Hard", BOSSING, HARD),


    /*
    Minigames Teleports
     */
    MINIGAMES_11(new Position(2541, 4716),"Mage Arena", "@gre@Easy", MINIGAMES, EASY,10057),

   MINIGAMES_1(new Position(2660, 2648),"Pest Control", "@gre@Easy", MINIGAMES, MEDIUM,10537),
  MINIGAMES_10(new Position(2878, 3546),"Warriors Guild", "@yel@Easy", MINIGAMES, MEDIUM,11319),
    MINIGAMES_24(new Position(3565, 3316),"Barrows", "@yel@Easy", MINIGAMES, MEDIUM,14131),

    MINIGAMES_2(new Position(2445, 5176),"Fight Caves", "@yel@Medium", MINIGAMES, MEDIUM,9808),

   MINIGAMES_4(new Position(1254, 3571),"Chambers Of Xeric", "@red@Hard", MINIGAMES, HARD),
    MINIGAMES_5(new Position(3656, 3218), "Theatre Of Blood", "@red@Hard", MINIGAMES, HARD),
  MINIGAMES_6(new Position(3359, 9125), "Tombs of Amascut", "@red@Hard", MINIGAMES, HARD),

    MINIGAMES_7(new Position(2494, 5107),"The Inferno", "@red@Hard", MINIGAMES, MEDIUM),
    /* Test & Add Teles for minigames
    		SHAYZIEN_ASSAULT("Shayzien Assault", Tab.MINIGAMES, 63142, new Position(1461, 3689, 0)),

		CLAN_WARS("Clan Wars", Tab.MINIGAMES, 63143, new Position(3059, 9947, 0)),

		GAMBLE("@blu@Gamble", Tab.MINIGAMES, 63144, new Position(2337, 3869, 0)),

		TOMBSOFAMASCUT("TOMBSOFAMASCUT", Tab.MINIGAMES, 63146, new Position(3359, 9125, 0), false, CombatTeleportInfo.TOMBSOFAMASCUT),

		TOKKUL_PIT("TOKKUL_PIT", Tab.MINIGAMES, 63147, new Position(3340, 4124, 0), false, CombatTeleportInfo.TOKKUL_PIT),

     */
    /*
    Skilling Teleports
     */
    SKILLING_1(new Position(3025, 9821),"Mining Area", "", SKILLING, MEDIUM),
    SKILLING_2(new Position(1654, 3505), "Woodcutting Guild", "", SKILLING, MEDIUM),
    SKILLING_3(new Position(2852, 2953), "Fishing Area", "", SKILLING, MEDIUM),
    SKILLING_4(new Position(1581, 3436), "Hunting Area", "", SKILLING, MEDIUM),
    SKILLING_5(new Position(2593, 4322), "Puro Puro", "", SKILLING, MEDIUM),
    SKILLING_7(new Position(2661, 3307), "Thieving Area", "", SKILLING, MEDIUM),
  SKILLING_8(new Position(2474, 3438), "Gnome Agility Course", "", SKILLING, EASY),
    SKILLING_10(new Position(3222, 3414), "Varrock Agility Course", "", SKILLING, MEDIUM),
    SKILLING_9(new Position(3500, 3493), "Canifis Agility Course", "", SKILLING, MEDIUM),

/*
Wilderness Teleports
 */

    WILDERNESS_1(new Position(3128, 3833), "Revenant cave", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_2(new Position(3184, 3947), "Resource Area", "@red@Dangerous", WILDERNESS, MEDIUM),
   // WILDERNESS_4(new Position(2963, 3916), "Skeletal Wyverns", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_5(new Position(3351, 3659), "East Dragons", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_6(new Position(2976, 3591), "West Dragons", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_7(new Position(3204, 3812), "Lava Dragons", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_8(new Position(3347, 3872), "Rune Fountain", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_9(new Position(3366, 3935), "Wildy Volcano", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_10(new Position(3236, 3628), "Chaos Altar", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_11(new Position(3010, 3631), "Dark Castle", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_14(new Position(3128, 3833), "Venenatis", "@red@Dangerous", WILDERNESS, HARD,13727),
    WILDERNESS_18(new Position(3128, 3833), "Vet'ion", "@red@Dangerous", WILDERNESS, HARD),
    WILDERNESS_19(new Position(3293, 3847), "Callisto", "@red@Dangerous", WILDERNESS, HARD,13473),
    WILDERNESS_12(new Position(3141,3628), "Ferox Enclave", "@red@Dangerous", WILDERNESS, MEDIUM,12600),
    WILDERNESS_13(new Position(3231, 3932), "Scorpion Pit / Scorpia", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_21(new Position(3287, 3899), "The Ruins", "@red@Dangerous", WILDERNESS, MEDIUM),

    WILDERNESS_133(new Position(2984, 3713), "Crazy Archaelogist", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_211(new Position(2981, 3836), "Chaos Fanatic", "@red@Dangerous", WILDERNESS, MEDIUM),
    WILDERNESS_2211(new Position(3281, 3910), "Chaos Elemental", "@red@Dangerous", WILDERNESS, MEDIUM),
  //  WILDERNESS_14(new Position(3229, 3778), "Earth Warriors", "@red@Dangerous", WILDERNESS, MEDIUM),
 //   WILDERNESS_15(new Position(3097, 3814), "Wildy Wyrms", "@red@Dangerous", WILDERNESS, MEDIUM),
   // WILDERNESS_16(new Position(2777, 3837), "Ice StrykeWyrms", "@red@Dangerous", WILDERNESS, MEDIUM),
/*
Cities
 */

    CITIES_1(new Position(3222, 3218),"Lumbridge", "", CITIES, MEDIUM),
    CITIES_2(new Position(3212, 3424), "Varrock", "", CITIES, MEDIUM,12853),
    CITIES_3(new Position(2964, 3378), "Falador", "", CITIES, MEDIUM,11828),
    CITIES_5(new Position(2757, 3477), "Camelot", "", CITIES, MEDIUM,11062),
    CITIES_6(new Position(2822, 3438), "Catherby", "", CITIES, MEDIUM,11317),
    CITIES_7(new Position(2661, 3306), "Ardougne", "", CITIES, MEDIUM,10547),
    CITIES_8(new Position(2928, 3451), "Taverley", "", CITIES, MEDIUM,11573),
    CITIES_9(new Position(2633, 3673), "Rellekka", "", CITIES, MEDIUM,10553),
    CITIES_10(new Position(2606, 3099), "Yanille", "", CITIES, MEDIUM,10288),
    CITIES_11(new Position(2950, 3147), "Karamja", "", CITIES, MEDIUM),
    CITIES_12(new Position(3292, 3176), "Al Kharid", "", CITIES, MEDIUM,13106),
    CITIES_13(new Position(2331, 3171), "LLetya", "", CITIES, MEDIUM),
    CITIES_14(new Position(3492,3485), "Canifis", "", CITIES, MEDIUM,13878),
    CITIES_15(new Position(2465,3495), "Tree Gnome Stronghold", "", CITIES, MEDIUM),
    CITIES_16(new Position(2419, 4448), "Zanaris", "", CITIES, MEDIUM),
    CITIES_17(new Position(2808, 2760), "Ape Atoll", "", CITIES, MEDIUM),
//    MISC_1(new Position(3034, 3690),"", "short description", MISCELLANEOUS, EASY),
//
//    MISC_2(new Position(3235, 3643), "", "short description", MISCELLANEOUS, HARD)
    ;

    NewTeleData(Position tile, String text, String description, int category, TeleDifficulty difficulty) {
        this.tile = tile;
        this.text = text;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.potentialunlockedregion = -1;
    }
    NewTeleData(Position tile, String text, String description, int category, TeleDifficulty difficulty, int unlockedregion) {
        this.tile = tile;
        this.text = text;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.potentialunlockedregion = unlockedregion;
    }
    public final Position tile;
    public String text;
    public String description;
    public final int category;
    public TeleDifficulty difficulty;
    public int potentialunlockedregion;

}


