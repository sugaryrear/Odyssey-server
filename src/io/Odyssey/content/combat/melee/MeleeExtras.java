package io.Odyssey.content.combat.melee;

import java.util.Objects;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.core.HitDispatcher;
import io.Odyssey.content.combat.formula.CombatFormula;
import io.Odyssey.content.combat.formula.MeleeMaxHit;
import io.Odyssey.content.combat.formula.rework.MeleeCombatFormula;
import io.Odyssey.content.combat.specials.Specials;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.multiplayersession.duel.DuelSessionRules;
import io.Odyssey.util.Misc;

public class MeleeExtras {

    public static void applySapSpirit(Player c, Player o, int damage) {
        if (!c.prayerActive[CombatPrayer.SAP_SPIRIT])
            return;
        if (damage <= 0)
            return;
        if (o != null) {
            int random = Misc.random(10);

            if (random == 0) {
                o.specAmount -= 1;
                o.getItems().updateSpecialBar();
                o.sendMessage("Your special energy has been drained by 10%");
            }
        }
    }
    public static void applyLeechSpec(Player c, Player o, int damage) {
        if (!c.prayerActive[CombatPrayer.LEECH_SPECIAL])
            return;
        if (damage <= 0)
            return;
        if (o != null) {
            int random = Misc.random(10);

            if (random == 0) {
                o.specAmount -= 1;
                o.getItems().updateSpecialBar();
                o.sendMessage("Your special energy has been drained by 10%");
            }
        }
    }
    public static void applyLeechEnergy(Player c, Player o, int damage) {
        if (!c.prayerActive[CombatPrayer.LEECH_ENERGY])
            return;
        if (damage <= 0)
            return;
        if (o != null) {
            int random = Misc.random(5);

            if (random == 0) {

                int total = o.getRunEnergy() - 1000;
                if (total < 0) {
                    o.setRunEnergy(0, true);
                    //  o.getPA().sendFrame126(Integer.toString(0), 149);
                } else {
                    o.setRunEnergy(o.getRunEnergy() - 1000, true);
                }


                o.specAmount -= 1;
                o.getItems().updateSpecialBar();
                o.sendMessage("Your run energy has been drained.");
            }
        }
    }
    public static void applyDeflect_npc(Player c, int damage, NPC npc) {
        if (damage <= 0)
            return;

        if (npc != null) {
            int random = Misc.random(5);
            if(random == 0){
c.sendMessage("You reflect the damage back!");
                npc.appendDamage(c, damage, Hitmark.HIT);
                c.setUpdateRequired(true);
            }

        }

    }
    public static void applySmite(Player c, Player o, int damage) {
        if (!c.prayerActive[CombatPrayer.SMITE])
            return;
        if (damage <= 0)
            return;
        if (o != null) {
            o.playerLevel[5] -= damage / 4;
            if (o.playerLevel[5] <= 0) {
                o.playerLevel[5] = 0;
                CombatPrayer.resetPrayers(o);
            }
            o.getPA().refreshSkill(5);
        }
    }
    //If active while fighting another player, they will lose 1 Prayer points for every 5 points of damage you do, and you will gain 1 life points for every 5 points of damage you do.
    //Your opponent cannot protect against this Curse.
    public static void applySoulSplit(Player c, Player o, int damage) {
        if (!c.prayerActive[CombatPrayer.SOUL_SPLIT])
            return;
        if (damage <= 0)
            return;
        if (o != null) {
            int howmuchprayertodrain = damage / 5;
            int howmuchhealthtoregain = damage /5;


            o.playerLevel[5] -= howmuchprayertodrain;
            if (o.playerLevel[5] <= 0) {
                o.playerLevel[5] = 0;
                CombatPrayer.resetPrayers(o);
            }
            o.getPA().refreshSkill(5);
            c.heal(howmuchhealthtoregain);

        }
    }
    public static void appendVengeanceNPC(Player c, int damage, NPC npc) {
        if (damage <= 0)
            return;
        if (!c.vengOn)
            return;

        if (npc != null) {
            c.forcedChat("Taste vengeance!");
            c.vengOn = false;

            int vengeanceDamage = (int) ((double) damage * 0.75);
            vengeanceDamage = Math.min(vengeanceDamage, npc.getHealth().getCurrentHealth());
            npc.appendDamage(c, damage, Hitmark.HIT);
        }
        c.setUpdateRequired(true);
    }

    public static void appendVengeance(Player c, Player o, int damage) {
        if (damage <= 0)
            return;
        o.forcedChat("Taste vengeance!");
        o.vengOn = false;
        if ((o.getHealth().getCurrentHealth() - damage) > 0) {
            damage = (int) (damage * 0.75);
            c.appendDamage(o, damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
            c.addDamageTaken(o, damage);
        }
        c.setUpdateRequired(true);
    }

    public static void applyRecoilNPC(Player c, int damage, NPC npc) {
        if (damage <= 0)
            return;
        if (damage > 0 && c.playerEquipment[Player.playerRing] == 2550) {
            int recDamage = damage / 10;
            if (recDamage < 1 && damage > 0) {
                recDamage = 1;
            }
            if (npc.getHealth().getCurrentHealth() <= 0 || npc.isDead()) {
                return;
            }
            npc.appendDamage(c, recDamage, Hitmark.HIT);
            c.recoilHits += recDamage;
            removeRecoil(c);
        }
        if (damage > 0 && c.playerEquipment[Player.playerRing] == 19550 || c.playerEquipment[Player.playerRing] == 19710) {
            int recDamage = damage / 10;
            if (recDamage < 1 && damage > 0) {
                recDamage = 1;
            }
            if (npc.getHealth().getCurrentHealth() <= 0 || npc.isDead()) {
                return;
            }
            npc.appendDamage(c, recDamage, Hitmark.HIT);
            c.recoilHits += recDamage;
            removeRecoil(c);
        }
    }

    public static void applyRecoil(Player attacker, Player defender, int damage) {
        if (damage <= 0)
            return;
        if (Boundary.isIn(attacker, Boundary.DUEL_ARENA)) {
            DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(attacker, MultiplayerSessionType.DUEL);
            if (Objects.nonNull(session)) {
                if (session.getRules().contains(DuelSessionRules.Rule.NO_RINGS)) {
                    return;
                }
            }
        }
        if (defender.playerEquipment[Player.playerRing] == 2550) {
            int recDamage = damage / 10 + 1;
            attacker.appendDamage(defender, recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
            attacker.addDamageTaken(defender, recDamage);
            attacker.setUpdateRequired(true);
            defender.recoilHits += damage;
            removeRecoil(defender);
        }
        if (defender.playerEquipment[Player.playerRing] == 19550 || attacker.playerEquipment[Player.playerRing] == 19710) {
            int recDamage = damage / 10 + 1;
            attacker.appendDamage(defender, recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
            attacker.addDamageTaken(defender, recDamage);
            attacker.setUpdateRequired(true);
            defender.recoilHits += damage;
        }
    }

    public static void removeRecoil(Player c) {
        if (c.recoilHits >= 40) {
            if (c.playerEquipment[Player.playerRing] == 2550) {
                c.getItems().unequipItem(2550, Player.playerRing);
                c.getItems().deleteItem(2550, c.getItems().getInventoryItemSlot(2550), 1);
                c.recoilHits = 0;
                c.sendMessage("Your ring of recoil shatters!");
            }
        } else {
            c.recoilHits++;
        }
    }

    public static void graniteMaulSpecial(Player c, boolean queueSpecialOrSetAttacking) {
        if (c.getHealth().getCurrentHealth() == 0)
            return;
        if (c.playerAttackingIndex > 0) {
            Player o = PlayerHandler.players[c.playerAttackingIndex];
            if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.attacking.getRequiredDistance())) {
                if (c.attacking.attackEntityCheck(o, true)) {
                    double accuracy = MeleeCombatFormula.get().getAccuracy(c, o);
                    boolean isAccurate = accuracy >= HitDispatcher.rand.nextDouble();

                    int damage = 0;
                    if (isAccurate)
                        damage = Misc.random(MeleeMaxHit.calculateBaseDamage(c));
                    if (o.prayerActive[18])
                        damage *= .6;
                    if (o.getHealth().getCurrentHealth() - damage <= 0) {
                        damage = o.getHealth().getCurrentHealth();
                    }
                    if (o.getHealth().getCurrentHealth() > 0 && c.specAmount >= 5) {
                        c.specAmount -= 5;
                        c.getItems().updateSpecialBarAmount();
                        c.getItems().updateSpecialBar();
                        o.appendDamage(c, damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                        c.startAnimation(1667);
                        c.gfx100(340);
                        o.addDamageTaken(c, damage);
                        //c.attackTimer += 1;
                    }
                }
            }
        } else if (c.npcAttackingIndex > 0) {
            // Attacking npc
            NPC npc = NPCHandler.npcs[c.npcAttackingIndex];
            int x = npc.absX;
            int y = npc.absY;
            if (c.goodDistance(c.getX(), c.getY(), x, y, npc.getSize()) && c.attacking.attackEntityCheck(npc, true)) {
                if (npc.getHealth().getCurrentHealth() == 0 || npc.needRespawn || npc.isDead() || npc.applyDead)
                    return;
                int damage = Misc.random(MeleeMaxHit.calculateBaseDamage(c));
                if (npc.getHealth().getCurrentHealth() - damage < 0) {
                    damage = npc.getHealth().getCurrentHealth();
                }
                if (c.specAmount >= 5) {
                    c.specAmount -= 5;
                    c.getItems().updateSpecialBarAmount();
                    c.getItems().updateSpecialBar();
                    npc.appendDamage(c, damage, Hitmark.HIT);
                    c.startAnimation(1667, 6);
                    c.gfx100(340);
                    //c.attackTimer += 1;
                }
            }
        } else if (queueSpecialOrSetAttacking) {
            Entity lastAttackedEntity = c.lastAttackedEntity.get();
            if (lastAttackedEntity != null) {
                if (c.goodDistance(c.getX(), c.getY(), lastAttackedEntity.getX(), lastAttackedEntity.getY(), c.attacking.getRequiredDistance())) {
                    c.attackEntity(lastAttackedEntity);
                    c.faceEntity(lastAttackedEntity);

                    graniteMaulSpecial(c, false);
                    if (c.attackTimer <= 1) {
                        c.attackTimer += 1;
                    }
                    return;
                }
            }

            if (++c.graniteMaulSpecialCharges > 2) {
                c.graniteMaulSpecialCharges = 0;
            } else {
                c.sendMessage(c.graniteMaulSpecialCharges + " specials queued.");
            }
        }
    }

    public static void applyOnHit(Player attacker, Player defender, Damage damage) {

        if (attacker == null || defender == null || damage == null)
            return;

        boolean sucessfulHit = damage.getAmount() > 0;

        if (sucessfulHit) {
            applySapSpirit(attacker, defender, damage.getAmount());
            applyLeechSpec(attacker, defender, damage.getAmount());
            applyLeechEnergy(attacker, defender, damage.getAmount());
            applySmite(attacker, defender, damage.getAmount());
            applySoulSplit(attacker, defender, damage.getAmount());
            if (defender.vengOn) {
                appendVengeance(attacker, defender, damage.getAmount());
            }


            handleRedemption(defender, damage.getAmount());
            applyRecoil(attacker, defender, damage.getAmount());
        }
    }

    public static void handleRedemption(Player defender, int damage) {
        if (defender.prayerActive[CombatPrayer.REDEMPTION]) {
            int prayerLevel = defender.playerLevel[5];
            int max = defender.getHealth().getMaximumHealth();
            int current = defender.getHealth().getCurrentHealth();

            current -= damage;

            if (current > 0 && current <= (max / 10)) {
                defender.gfx0(436);
                defender.heal((int) Math.floor(prayerLevel / 4));
                defender.drainPrayer();
                CombatPrayer.resetPrayer(CombatPrayer.REDEMPTION, defender);

            }
        }
    }
}