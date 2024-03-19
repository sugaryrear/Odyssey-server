package io.Odyssey.content.dwarfmulticannon;

import io.Odyssey.model.Direction;

/**
 * The eight directions a cannon can face, in chronological order (note: do
 * not change order, order is crucial!).
 */
enum CannonRotationState {
    NORTH(Direction.NORTH, 515),
    NORTH_EAST(Direction.NORTH_EAST, 516),
    EAST(Direction.EAST, 517),
    SOUTH_EAST(Direction.SOUTH_EAST, 518),
    SOUTH(Direction.SOUTH, 519),
    SOUTH_WEST(Direction.SOUTH_WEST, 520),
    WEST(Direction.WEST, 521),
    NORTH_WEST(Direction.NORTH_WEST, 514);

    private final Direction direction;
    private final int animationId;


    CannonRotationState(Direction direction, int animationId) {
        this.direction = direction;
        this.animationId = animationId;
    }

    public int getAnimationId() {
        return animationId;
    }
}

