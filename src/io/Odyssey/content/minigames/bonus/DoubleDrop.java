package io.Odyssey.content.minigames.bonus;

import java.util.Calendar;

public class DoubleDrop {

    public static boolean isDoubleDrop() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
    }
}
