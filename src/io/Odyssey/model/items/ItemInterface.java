package io.Odyssey.model.items;

import io.Odyssey.model.definitions.ItemDef;

public interface ItemInterface {

    default ItemDef getDef() {
        return ItemDef.forId(getId());
    }

    int getId();

    int getAmount();

}
