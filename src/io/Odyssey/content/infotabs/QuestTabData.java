package io.Odyssey.content.infotabs;

public enum QuestTabData {
    INFO(35018),
    BUTTON_TAB(35070),
    ACHIEVEMENTS(35119),
    EVENTS(35135);

    private final int tabID;

    QuestTabData(int tabID) {
        this.tabID = tabID;
    }

    public int getTabID() {
        return tabID;
    }
}
