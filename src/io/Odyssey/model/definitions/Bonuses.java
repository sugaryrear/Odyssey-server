package io.Odyssey.model.definitions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bonuses {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Bonuses.class.getName());



    public static Bonuses getbonuses(int id) {
        return bonuses.getOrDefault(id, DEFAULT_BONUSES);
    }
    private static Map<Integer, Bonuses> bonuses = new LinkedHashMap<>();
    public static void loadBonuses(File file) {
        try {
            bonuses = new Gson().fromJson(new FileReader(file), new TypeToken<HashMap<Integer, Bonuses>>() {
            }.getType());

            log.info("Loaded "+bonuses.size()+" equipment bonuses.");
        } catch (FileNotFoundException e) {
            log.info("Could not load bonuses");
        }
    }
    private static final Bonuses DEFAULT_BONUSES = new Bonuses();
    public int stab;
    public int slash;
    public int crush;
    public int range;
    public int mage;
    public int stabdef;
    public int slashdef;
    public int crushdef;
    public int rangedef;
    public int magedef;
    public int str;
    public int rangestr;
    public int magestr;
    public int pray;

    public int[] bonuses() {
        return new int[]{stab, slash, crush, range, mage,stabdef,slashdef,crushdef,rangedef,magedef,str,rangestr,magestr,pray};
    }

    public String[] bonusesAtk() {
        return new String[]{"Stab", "Slash", "Crush", "Range", "Mage"};
    }

    @Override
    public String toString() {
        return "Bonuses{" +
                "stab=" + stab +
                ", slash=" + slash +
                ", crush=" + crush +
                ", range=" + range +
                ", mage=" + mage +
                ", stabdef=" + stabdef +
                ", slashdef=" + slashdef +
                ", crushdef=" + crushdef +
                ", rangedef=" + rangedef +
                ", magedef=" + magedef +
                ", str=" + str +
                ", rangestr=" + rangestr +
                ", magestr=" + magestr +
                ", pray=" + pray +
                '}';
    }

}
