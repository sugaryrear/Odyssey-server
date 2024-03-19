package io.Odyssey.util;



import io.Odyssey.content.boosts.Boosts;
import io.Odyssey.model.definitions.ItemDef;

import java.io.File;
import java.util.Scanner;

public class ExamineRepository {



    private String[] items;
    private String[] objects;
    private String[] npcs;
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ExamineRepository.class.getName());
    public ExamineRepository() {
        items = new String[39000];
        objects = new String[50000];
        npcs = new String[20000];

        readFile("etc/cfg/examine/item_examines.txt", items);
        readFile("etc/cfg/examine/object_examines.txt", objects);
        readFile("etc/cfg/examine/npc_examines.txt", npcs);


        //  System.out.println()
        log.info("Loaded all the examines.");
//        log.info("Loaded {} object examines.", objects.length);
//        log.info("Loaded {} npc examines.", npcs.length);
    }

    private void readFile(String path, String[] out) {
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int id = Integer.parseInt(line.substring(0, line.indexOf(':')));
                String examine = line.substring(line.indexOf(':') + 1);
                out[id] = examine;
            }
        } catch (Exception e) {
            //  logger.error("Could not file examines.", e);
        }
    }

//    public String item(Item item) {
//        return item == null ? "Something." : item(item.getId());
//    }

    public String item(int id) {
        if (id < 0) {
            System.err.println("Item id out of range! " + id);
            return "Something.";
        }
//        if (id == 5020) {
//            return "A voucher that converts into 1 vote point for the redeemer.";
//        }
//        if (id == 6990)
//            return "It's a bag of dice.";
//
//        if (id == 6808) {
//            return "It gives off a strange aura.";
//        }
if(ItemDef.forId(id).isNoted()){
    id-=1;
        }
        String examine = items[id];
        return examine == null ? "Something." : examine;
    }

    public String object(int id) {
        if (id < 0) {
            System.err.println("Mapobj id out of range! " + id);
            return "Something.";
        }

        String examine = objects[id];
        return examine == null ? "Something.?" : examine;
    }

    public String npc(int id) {
        if (id < 0) {
            System.err.println("Npc id out of range! " + id);
            return "Something.";
        }

        String examine = npcs[id];
        return examine == null ? "Something." : examine;
    }

}
