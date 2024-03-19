package io.Odyssey.model.controller;

import io.Odyssey.content.party.PartyInterface;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

import java.util.Set;

//this is the default controller when you are NOT in any of the other controllers. be careful here.
public class DefaultController implements Controller {

    @Override
    public String getKey() {
        return ControllerRepository.DEFAULT_CONTROLLER_KEY;
    }

    @Override
    public Set<Boundary> getBoundaries() {
        return null;
    }

    @Override
    public void added(Player player) {
        PartyInterface.close(player);
        player.getPA().showOption(3, 0, "null");

    }

    @Override
    public void removed(Player player) { }

    @Override
    public boolean onPlayerOption(Player player, Player clicked, String option) {
        return false;
    }

    @Override
    public boolean canMagicTeleport(Player player) {
        return true;
    }

    @Override
    public void onLogin(Player player) { }

    @Override
    public void onLogout(Player player) { }
}
