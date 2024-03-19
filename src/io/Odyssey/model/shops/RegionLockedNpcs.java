package io.Odyssey.model.shops;

public enum RegionLockedNpcs {
//    VARROCK(new int[]{2883, 2880}, 12853),
//    FALADOR(new int[]{3214,5897,5896}, 11828),
//    SOPHANEM(new int[]{3891}, 13099),
//  //  APE_ATOLL(new int[]{7120,5250}, 11051),
//    PRIFFDINAS(new int[]{9156}, 12895),
//    RELEKKA(new int[]{3935}, 10553),
//    PORT_SARIM(new int[]{2892}, 12082),
//   // ZANARIS(new int[]{3201}, 9797),
//    AL_KHARID(new int[]{2878,2876,2879,2874}, 13106),
//    BARBARIAN_VILLAGE(new int[]{2872}, 12341),
//    SHILO_VILLAGE(new int[]{405}, 11310),
//    CANIFIS(new int[]{402}, 13878)
    ;


    public int[] npcIds;
    public int region;

    RegionLockedNpcs(int[] npcIds, int region) {
        this.npcIds = npcIds;
        this.region = region;
    }

    public static RegionLockedNpcs getEnumByNpcId(int npcId) {
        for (RegionLockedNpcs location : values()) {
            for (int id : location.npcIds) {
                if (npcId == id) {
                    return location;
                }
            }
        }
        return null; // Return null if there's no match
    }
    }