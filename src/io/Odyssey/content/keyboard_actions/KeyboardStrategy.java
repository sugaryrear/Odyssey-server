package io.Odyssey.content.keyboard_actions;

import io.Odyssey.model.entity.player.Player;

@FunctionalInterface
public interface KeyboardStrategy {
    void execute(Player player);
}
