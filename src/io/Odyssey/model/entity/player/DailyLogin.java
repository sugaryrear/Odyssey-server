package io.Odyssey.model.entity.player;

import io.Odyssey.util.Misc;

import java.util.Calendar;

public class DailyLogin {

    static Calendar cal = Calendar.getInstance();
    static int year = cal.get(Calendar.YEAR);
    static int date = cal.get(Calendar.DAY_OF_MONTH);
    static int month = cal.get(Calendar.MONTH)+1;

    private Player c;

    public DailyLogin(Player client) {
        this.c = client;
    }
    public static int getLastDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month -1, date);
        int maxDay = calendar.getActualMaximum(date);
        return maxDay;
    }
    public void IncreaseStreak() {
        c.LastLoginYear = year;
        c.LastLoginDate = date;
        c.LastLoginMonth = month;
        c.LoginStreak++;
        String dayordays = c.LoginStreak > 1 ? "days" : "day";
        int rewarditem = 995;
        int rewardAmount = 1;
        String rewardMessage = "";
        String rewardmessagecont = "";


        switch(c.LoginStreak) {
            case 2: //2nd day
                rewardAmount = 500;
                c.getItems().sendItemToAnyTab(995,250_000);
                rewardMessage = ",as a reward "+ Misc.insertCommasToNumber(Integer.toString(rewardAmount))+" gold has been added to";
                rewardmessagecont = "your bank account.";
                break;
            case 5: //5 logins in a row
                rewardAmount = 1500;
                c.getItems().sendItemToAnyTab(995,500_000);
                rewardMessage = ",as a reward "+Misc.insertCommasToNumber(Integer.toString(rewardAmount))+" gold has been added to";
                rewardmessagecont = "your bank account.";

                break;
            case 10: //10 logins
                rewardAmount = 3500;
                c.getItems().sendItemToAnyTab(995,1_000_000);
                rewardMessage = ",as a reward "+Misc.insertCommasToNumber(Integer.toString(rewardAmount))+" gold has been added to";
                rewardmessagecont = "your bank account.";

                break;
        }
        String defaultmessage = "You have logged in for @blu@"+c.LoginStreak+"@bla@ "+dayordays+" in a row"+(rewardMessage.length() > 0 ? rewardMessage : ".")+" ";
        c.sendMessage(defaultmessage);
        if(rewardMessage.length()>0)
            c.sendMessage(rewardmessagecont);
    }
    public void RefreshDates() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        date = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH)+1;
    }
    public void LoggedIn() {
        RefreshDates();
        if ((c.LastLoginYear == year) && (c.LastLoginMonth == month) && (c.LastLoginDate == date)) {
           // c.sendMessage("You last logged in today.");
            return;
        }
        if ((c.LastLoginYear == year) && (c.LastLoginMonth == month) && (c.LastLoginDate == (date - 1)))
            IncreaseStreak();
        else if ((c.LastLoginYear == year) && ((c.LastLoginMonth + 1) == month) && (1 == date))
            IncreaseStreak();
        else if ((c.LastLoginYear == year-1) && (1 == month) && (1 == date))
            IncreaseStreak();
        else {
            c.LoginStreak = 0;
            IncreaseStreak();
        }
    }
}
