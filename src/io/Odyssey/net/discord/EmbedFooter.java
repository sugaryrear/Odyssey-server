package io.Odyssey.net.discord;

import io.Odyssey.net.discord.Payload;
import io.Odyssey.net.discord.PayloadMap;

public class EmbedFooter extends Payload {

    private final String text, iconUrl;

    public EmbedFooter(String text, String iconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
    }

    public String getText() {
        return text;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public void save(PayloadMap map) {
        map.put("text", text);
        map.putIfExists("icon_url", iconUrl);
    }
}
