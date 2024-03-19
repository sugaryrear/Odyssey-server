package io.Odyssey.content.commands.all;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.Bonus;
import io.Odyssey.model.definitions.Bonuses;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.NewStore_onlogin;

public class Getitemstats extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
       // new NewStore_onlogin(c,true).run();
        String[] args = input.split(" ");
        int itemid = Integer.parseInt(args[0]);
        Bonuses entirebonusesarray = Bonuses.getbonuses(itemid);

//stab, slash, crush, range, mage,stabdef,slashdef,crushdef,rangedef,magedef,str,rangestr,magestr,pray
        //   "stab": 0,
        //    "slash": 0,
        //    "crush": 0,
        //    "range": 0,
        //    "mage": 0,
        //    "stabdef": 0,
        //    "slashdef": 0,
        //    "crushdef": 0,
        //    "rangedef": 0,
        //    "magedef": 0,
        //    "str": 0,
        //    "rangestr": 55,
        //    "magestr": 0,
        //    "pray": 0
        if(entirebonusesarray  != null){
//            c.playerBonus[Bonus.ATTACK_STAB.ordinal()] = entirebonusesarray.bonuses()[0];
//            c.playerBonus[Bonus.ATTACK_SLASH.ordinal()] = entirebonusesarray.bonuses()[1];
//            c.playerBonus[Bonus.ATTACK_CRUSH.ordinal()] = entirebonusesarray.bonuses()[2];
//            c.playerBonus[Bonus.ATTACK_RANGED.ordinal()] = entirebonusesarray.bonuses()[3];
//            c.playerBonus[Bonus.ATTACK_MAGIC.ordinal()] = entirebonusesarray.bonuses()[4];
//            c.playerBonus[Bonus.DEFENCE_STAB.ordinal()] = entirebonusesarray.bonuses()[5];
//            c.playerBonus[Bonus.DEFENCE_SLASH.ordinal()] = entirebonusesarray.bonuses()[6];
//            c.playerBonus[Bonus.DEFENCE_CRUSH.ordinal()] = entirebonusesarray.bonuses()[7];
//            c.playerBonus[Bonus.DEFENCE_RANGED.ordinal()] = entirebonusesarray.bonuses()[8];
//
//            c.playerBonus[Bonus.DEFENCE_MAGIC.ordinal()] = entirebonusesarray.bonuses()[9];
//            c.playerBonus[Bonus.STRENGTH.ordinal()] = entirebonusesarray.bonuses()[10];
//            c.playerBonus[Bonus.RANGED_STRENGTH.ordinal()] = entirebonusesarray.bonuses()[11];
//            c.playerBonus[Bonus.MAGIC_DMG.ordinal()] = entirebonusesarray.bonuses()[12];
//            c.playerBonus[Bonus.PRAYER.ordinal()] = entirebonusesarray.bonuses()[13];
            c.sendMessage("itemstats##"+entirebonusesarray.bonuses()[0]+"##"+entirebonusesarray.bonuses()[1]+"##"+entirebonusesarray.bonuses()[2]+"##"+entirebonusesarray.bonuses()[3]+"##" +
                    ""+entirebonusesarray.bonuses()[4]+"##"+entirebonusesarray.bonuses()[5]+"##"+entirebonusesarray.bonuses()[6]+"##"+entirebonusesarray.bonuses()[6]+"##"+entirebonusesarray.bonuses()[7]+"##" +
                    ""+entirebonusesarray.bonuses()[8]+"##"+entirebonusesarray.bonuses()[9]+"##"+entirebonusesarray.bonuses()[10]+"##"+entirebonusesarray.bonuses()[13]);

        }


    }

}
