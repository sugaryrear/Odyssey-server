package io.Odyssey.net.discord;

public abstract class Payload {

    public abstract void save(PayloadMap map);

    public final PayloadObject toObject() {
        return new PayloadObject(this);
    }

}
