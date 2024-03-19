package io.Odyssey.content.boosts;

public interface Booster<T> {

    String getDescription();

    boolean applied(T t);

    BoostType getType();

}
