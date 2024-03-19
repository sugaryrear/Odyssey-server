package io.Odyssey.content.minigames.TombsOfAmascut.instance;

import io.Odyssey.model.Items;

    public enum TombsOfAmascutChestSupplies {
        STAM(Items.STAMINA_POTION4, 1),
        PRAYER(Items.PRAYER_POTION4, 2),
        BREW(Items.SARADOMIN_BREW4, 3),
        RESTORE(Items.SUPER_RESTORE4, 3),
        POTATO(Items.TUNA_POTATO, 1),
        SHARK(Items.SHARK, 1),
        TURTLE(Items.SEA_TURTLE, 2),
        MANTA(Items.MANTA_RAY, 2),
        ;

        private final int itemId;
        private final int cost;

        TombsOfAmascutChestSupplies(int itemId, int cost) {
            this.itemId = itemId;
            this.cost = cost;
        }

        public int getItemId() {
            return itemId;
        }

        public int getCost() {
            return cost;
        }
}
