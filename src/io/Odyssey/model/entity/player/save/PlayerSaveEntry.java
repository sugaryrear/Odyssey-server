package io.Odyssey.model.entity.player.save;

import java.util.List;

import io.Odyssey.model.entity.player.Player;

public interface PlayerSaveEntry {

    List<String> getKeys(Player player);

    boolean decode(Player player, String key, String value);

    String encode(Player player, String key);

    void login(Player player);

}
