package io.Odyssey.net.discord;

import io.Odyssey.net.discord.Payload;
import io.Odyssey.net.discord.PayloadMap;

public class EmbedField extends Payload {

    private final String name, value;
    private final Boolean inline;

    public EmbedField(String name, String value, Boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Boolean getInline() {
        return inline;
    }

    @Override
    public void save(PayloadMap map) {
        map.put("name", name);
        map.put("value", value);
        map.putIfExists("inline", inline);
    }
}
