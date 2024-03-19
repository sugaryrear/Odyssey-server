package io.Odyssey.model.definitions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.Odyssey.Server;

import static io.Odyssey.model.definitions.Bonuses.loadBonuses;

public final class ItemStats {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ItemStats.class.getName());
    private static Map<Integer, ItemStats> itemStatsMap;
  //  private static Map<Integer, Bonuses> bonuses = new LinkedHashMap<>();
//    public Bonuses getbonuses(int id) {
//        return bonuses.getOrDefault(id, DEFAULT_BONUSES);
//    }
//    private static final Bonuses DEFAULT_BONUSES = new Bonuses();
//    public static class Bonuses {
//
//        public int stab;
//        public int slash;
//        public int crush;
//        public int range;
//        public int mage;
//        public int stabdef;
//        public int slashdef;
//        public int crushdef;
//        public int rangedef;
//        public int magedef;
//        public int str;
//        public int rangestr;
//        public int magestr;
//        public int pray;
//
//        public int[] bonuses() {
//            return new int[]{stab, slash, crush, range, mage};
//        }
//
//        public String[] bonusesAtk() {
//            return new String[]{"Stab", "Slash", "Crush", "Range", "Mage"};
//        }
//
//        @Override
//        public String toString() {
//            return "Bonuses{" +
//                    "stab=" + stab +
//                    ", slash=" + slash +
//                    ", crush=" + crush +
//                    ", range=" + range +
//                    ", mage=" + mage +
//                    ", stabdef=" + stabdef +
//                    ", slashdef=" + slashdef +
//                    ", crushdef=" + crushdef +
//                    ", rangedef=" + rangedef +
//                    ", magedef=" + magedef +
//                    ", str=" + str +
//                    ", rangestr=" + rangestr +
//                    ", magestr=" + magestr +
//                    ", pray=" + pray +
//                    '}';
//        }
//
//    }
    private static int missing;
    public static ItemList[] ItemList;

//    public static void loadBonuses(File file) {
//        try {
//            bonuses = new Gson().fromJson(new FileReader(file), new TypeToken<HashMap<Integer, Bonuses>>() {
//            }.getType());
//
//            log.info("Loaded "+bonuses.size()+" equipment bonuses.");
//        } catch (FileNotFoundException e) {
//            log.info("Could not load bonuses");
//        }
//    }


    public static void load() {
        ItemList = new ItemList[40000];
        for (int i = 0; i < 40000; i++) {
            ItemList[i] = null;
        }
        try {

            List<String> stackableData = Files.readAllLines(Paths.get("./etc/", "cfg/item/", "note_ids.dat"));
            for (String data : stackableData) {
                int id = Integer.parseInt(data.split("\t")[0]);
                int counterpart = Integer.parseInt(data.split("\t")[1]);
                if (ItemList[id] == null) {
                    ItemList[id] = new ItemList(id);
                }
                ItemList[id].setCounterpartId(counterpart);
            }
            // System.out.println("counterpart: "+ItemList[4151].getCounterpartId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fr = new FileReader(new File(Server.getDataDirectory() + "/cfg/item/item_stats.json"))) {
            itemStatsMap = new Gson().fromJson(fr, new TypeToken<Map<Integer, ItemStats>>() {
            }.getType());
            log.info("Loaded " + itemStatsMap.size() + " item stats.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loadBonuses(new File(Server.getDataDirectory() + "/cfg/item/bonuses.json"));
    }

    public static ItemStats forId(int itemId) {
        if (!itemStatsMap.containsKey(itemId)) {
            return DEFAULT;
        }
        return itemStatsMap.get(itemId);
    }

    static final ItemEquipmentStats DEFAULT_STATS = ItemEquipmentStats.builder().slot(-1).aspeed(4).build();
    static final ItemStats DEFAULT = builder().build();
    private final String name;
    private final Boolean quest;
    private final Boolean equipable;
    private final Double weight;
    private final ItemEquipmentStats equipment;

    public ItemEquipmentStats getEquipment() {
        return equipment == null ? DEFAULT_STATS : equipment;
    }

    ItemStats(final String name, final Boolean quest, final Boolean equipable, final Double weight, final ItemEquipmentStats equipment) {
        this.name = name;
        this.quest = quest;
        this.equipable = equipable;
        this.weight = weight;
        this.equipment = equipment;
    }

    public static class ItemStatsBuilder {

        private String name;

        private Boolean quest;

        private Boolean equipable;

        private Double weight;

        private ItemEquipmentStats equipment;

        ItemStatsBuilder() {
        }

        public ItemStats.ItemStatsBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public ItemStats.ItemStatsBuilder quest(final Boolean quest) {
            this.quest = quest;
            return this;
        }

        public ItemStats.ItemStatsBuilder equipable(final Boolean equipable) {
            this.equipable = equipable;
            return this;
        }

        public ItemStats.ItemStatsBuilder weight(final Double weight) {
            this.weight = weight;
            return this;
        }

        public ItemStats.ItemStatsBuilder equipment(final ItemEquipmentStats equipment) {
            this.equipment = equipment;
            return this;
        }

        public ItemStats build() {
            return new ItemStats(this.name, this.quest, this.equipable, this.weight, this.equipment);
        }

        @Override
    public String toString() {
            return "ItemStats.ItemStatsBuilder(name=" + this.name + ", quest=" + this.quest + ", equipable=" + this.equipable + ", weight=" + this.weight + ", equipment=" + this.equipment + ")";
        }
    }

    public static ItemStats.ItemStatsBuilder builder() {
        return new ItemStats.ItemStatsBuilder();
    }

    public String getName() {
        return this.name;
    }

    public Boolean getQuest() {
        return this.quest;
    }

    public Boolean getEquipable() {
        return this.equipable;
    }

    public Double getWeight() {
        return this.weight;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ItemStats)) return false;
        final ItemStats other = (ItemStats) o;
        final Object this$quest = this.getQuest();
        final Object other$quest = other.getQuest();
        if (this$quest == null ? other$quest != null : !this$quest.equals(other$quest)) return false;
        final Object this$equipable = this.getEquipable();
        final Object other$equipable = other.getEquipable();
        if (this$equipable == null ? other$equipable != null : !this$equipable.equals(other$equipable)) return false;
        final Object this$weight = this.getWeight();
        final Object other$weight = other.getWeight();
        if (this$weight == null ? other$weight != null : !this$weight.equals(other$weight)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$equipment = this.getEquipment();
        final Object other$equipment = other.getEquipment();
        if (this$equipment == null ? other$equipment != null : !this$equipment.equals(other$equipment)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $quest = this.getQuest();
        result = result * PRIME + ($quest == null ? 43 : $quest.hashCode());
        final Object $equipable = this.getEquipable();
        result = result * PRIME + ($equipable == null ? 43 : $equipable.hashCode());
        final Object $weight = this.getWeight();
        result = result * PRIME + ($weight == null ? 43 : $weight.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $equipment = this.getEquipment();
        result = result * PRIME + ($equipment == null ? 43 : $equipment.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ItemStats(name=" + this.getName() + ", quest=" + this.getQuest() + ", equipable=" + this.getEquipable() + ", weight=" + this.getWeight() + ", equipment=" + this.getEquipment() + ")";
    }
}
