package io.Odyssey.content.dialogue.types;

import io.Odyssey.content.dialogue.DialogueAction;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueObject;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class PlayerDialogue extends DialogueObject {

    /**
     * This array contains the child id where the dialogue
     * statement starts for player dialogues.
     */
    private static final int[] PLAYER_DIALOGUE_ID = {
            971,
            976,
            982,
            989
    };

    private final String[] text;
    private final DialogueExpression expression;

    public PlayerDialogue(DialogueBuilder context, DialogueExpression expression, String[] text) {
        super(context, true);
        this.text = text;
        this.expression = expression;
    }

    @Override
    public void send(Player player) {
        int startDialogueChildId = PLAYER_DIALOGUE_ID[text.length - 1];
        int headChildId = startDialogueChildId - 2;
        player.getPA().sendPlayerHeadOnInterface(headChildId);
    //    player.getPA().sendInterfaceAnimation(headChildId, expression.getAnimation());
        player.getPA().sendString(startDialogueChildId - 1, player.getDisplayName());
        for (int i = 0; i < text.length; i++) {
            player.getPA().sendString(startDialogueChildId + i, text[i]);
        }
        player.getPA().sendChatboxInterface(startDialogueChildId - 3);
    }

    @Override
    public void handleAction(Player player, DialogueAction action) {
        if (action == DialogueAction.CLICK_TO_CONTINUE) {
            getContext().sendNextDialogue();
        }
    }
}
