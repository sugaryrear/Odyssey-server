package io.Odyssey.model.items;

import io.Odyssey.content.event.eventcalendar.EventCalendarWinnerSelect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class EquipmentInfo {
    private static final EquipmentInfo INSTANCE = new EquipmentInfo();
    public static EquipmentInfo getInstance() {
        return INSTANCE;
    }
    public EquipmentInfo(){

    }
    public Map<Integer, Integer> requirementsFor(int id) {
        return itemRequirements.get(id);
    }

    private final Map<Integer, Map<Integer, Integer>> itemRequirements = new LinkedHashMap<>();
    public  void loadEquipmentRequirements() {
        File file = new File("./etc/cfg/item/requirements.txt");
        try (Scanner scanner = new Scanner(file)) {
            int numdef = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int id = Integer.parseInt(line.split(":")[0]);

                String reqs = line.split(":")[1];
                Map<Integer, Integer> map = new HashMap<>();
                for (String req : reqs.split(",")) {
                    int lvl = Integer.parseInt(req.split("=")[0]);
                    int needed = Integer.parseInt(req.split("=")[1]);
                    map.put(lvl, needed);
                }

                itemRequirements.put(id, map);
                numdef++;
            }

            // logger.info("Loaded {} item requirements.", numdef);
        } catch (FileNotFoundException e) {
            //  logger.error("Could not load item requirements.", e);
        }
    }
}
