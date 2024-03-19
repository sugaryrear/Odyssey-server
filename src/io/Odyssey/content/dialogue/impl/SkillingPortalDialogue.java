package io.Odyssey.content.dialogue.impl;

import com.google.common.collect.Lists;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.entity.player.mode.Mode;
import io.Odyssey.model.entity.player.mode.ModeRevertType;
import io.Odyssey.model.entity.player.mode.ModeType;
import io.Odyssey.model.items.ImmutableItem;

import java.util.List;
import java.util.function.Consumer;

public class SkillingPortalDialogue extends DialogueBuilder {



    public SkillingPortalDialogue(Player player) {
        super(player);
        setNpcId(-1)
                .option(new DialogueOption("Skilling Island", p -> player.getPA().startTeleport(3803, 3538, 0, "modern", false)),
                        new DialogueOption("Hunter Area", p -> player.getPA().startTeleport(3560, 4010, 0, "modern", false)),
                        new DialogueOption("Farming", p -> player.getPA().startTeleport(3053, 3301, 0, "modern", false)));

    }
}
