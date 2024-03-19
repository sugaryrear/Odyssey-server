package io.Odyssey.net.discord;

import io.Odyssey.net.discord.Payload;
import io.Odyssey.net.discord.PayloadMap;

public class EmbedAuthor extends Payload {

    private final String name, url, iconUrl;

    public EmbedAuthor(String name, String url, String iconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public void save(PayloadMap map) {
        map.put("name", name);
        map.putIfExists("url", url);
        map.putIfExists("icon_url", iconUrl);
    }
}
