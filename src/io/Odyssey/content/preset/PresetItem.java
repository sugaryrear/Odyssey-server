package io.Odyssey.content.preset;


public class PresetItem {

	private final int itemId;
	private final int amount;
	
	public PresetItem(int itemId, int amount) {
		this.itemId = itemId;
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}

	public int getAmount() {
		return amount;
	}
}
