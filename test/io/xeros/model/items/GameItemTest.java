package io.Odyssey.model.items;

import com.google.common.collect.Lists;
import io.Odyssey.ServerState;
import io.Odyssey.model.Items;
import io.Odyssey.test.ServerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameItemTest {

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private static final ServerTest serverTest = new ServerTest(ServerState.PUBLIC);

    @Test
    public void compareByPrice_sorts_high_to_low() {
        List<GameItem> items = Lists.newArrayList(new GameItem(Items.CANNONBALL), new GameItem(Items.ABYSSAL_WHIP));
        items.sort(GameItem::comparePrice);
        assertEquals(Items.ABYSSAL_WHIP, items.get(0).getId());
    }

}