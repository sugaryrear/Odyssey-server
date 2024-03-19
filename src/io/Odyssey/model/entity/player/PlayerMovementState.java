package io.Odyssey.model.entity.player;

public class PlayerMovementState {

	public static PlayerMovementState getDefault() {
		return new PlayerMovementStateBuilder().createPlayerMovementState();
	}

	private final boolean allowClickToMove;
	private final boolean runningEnabled;

	private final boolean crawlingEnabled;
	private final boolean locked;

	public PlayerMovementState(boolean allowClickToMove, boolean runningEnabled, boolean crawlEnabled, boolean locked) {
		this.allowClickToMove = allowClickToMove;
		this.runningEnabled = runningEnabled;
		this.crawlingEnabled = crawlEnabled;
		this.locked = locked;
	}
	public boolean isAllowClickToMove() {
		return allowClickToMove;
	}

	public boolean isRunningEnabled() {
		return runningEnabled;
	}
	public boolean isCrawlingEnabled() {
		return crawlingEnabled;
	}
	public boolean isLocked() {
		return locked;
	}
}








