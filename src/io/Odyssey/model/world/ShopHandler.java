package io.Odyssey.model.world;

import com.google.common.base.Preconditions;
import io.Odyssey.Configuration;
import io.Odyssey.model.definitions.ShopDef;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.shops.ShopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Shops
 **/

public class ShopHandler {

	public static int MaxShops = 420;
	public static int MaxShopItems = 400;
	public static int MaxShowDelay = 10;
	public static int MaxSpecShowDelay = 60;
	public static int[][] ShopItems = new int[MaxShops][MaxShopItems];
	public static int[][] ShopItemsN = new int[MaxShops][MaxShopItems];
	public static int[][] ShopItemsDelay = new int[MaxShops][MaxShopItems];
	public static int[][] ShopItemsSN = new int[MaxShops][MaxShopItems];
	public static int[] ShopItemsStandard = new int[MaxShops];
	public static String[] ShopName = new String[MaxShops];
	public static int[] ShopSModifier = new int[MaxShops];
	public static long[][] shopItemsRestock = new long[MaxShops][MaxShopItems];
	public ShopHandler() {
		for (int i = 0; i < MaxShops; i++) {
			for (int j = 0; j < MaxShopItems; j++) {
					ResetItem(i, j);
				ShopItemsSN[i][j] = 0;
			}
			ShopItemsStandard[i] = 0;
			ShopSModifier[i] = 0;
			ShopName[i] = "";
		}
	}

	public static void load() {
		ShopDef.getDefinitions().values().forEach(shop -> {
			addShop(shop.getId(), shop.getName(), shop.getItems());
		});
	}

	public static int addShopAnywhere(String name, List<ShopItem> items) {
		for (int i = 1; i < ShopName.length; i++) {
			if (ShopName[i].length() == 0 && ShopItems[i][0] == 0) {
				addShop(i, name, items);
				return i;
			}
		}

		throw new IllegalStateException("No open shop slot.");
	}
	
	public static void addShop(int id, String name, List<ShopItem> items) {
		Preconditions.checkState(id > 0, "Shop id must be more than zero.");
		ShopName[id] = name;
		int itemIndex = 0;
		for (GameItem item : items) {
			ShopItems[id][itemIndex] = item.getId();
			ShopItemsN[id][itemIndex] = item.getAmount();
			ShopItemsSN[id][itemIndex] = item.getAmount();
			ShopItemsStandard[id]++;
			itemIndex++;
		}
	}

	public static List<GameItem> getShopItems(int shopId) {
		ArrayList<GameItem> list = new ArrayList<>();
		for (int i = 0; i < ShopItems[shopId].length; i++) {
			int id = ShopItems[shopId][i];
			int amount = ShopItems[shopId][i];
			if (id > 0) {
				list.add(new GameItem(id, amount));
			}
		}
		return list;
	}
	public static int restockTimeItem(int itemId) {
		switch (itemId) {
			case 590:
				return 10000;
			default:
				return 10;
		}

	}

	public void process() {
		boolean DidUpdate = false;
		for (int i = 1; i <= MaxShops - 1; i++) {//get each specific shop as 'i'
			for (int j = 0; j < MaxShopItems; j++) {//get the specific item as 'j' because a shop of 5 items has 395 empty slots for example.
				if (ShopItems[i][j] > 0) {//does the slot have an item ?
					if (ShopItemsDelay[i][j] >= restockTimeItem(ShopItems[i][j])) {//so set specific show delay  when u buy it?
					//	if (j <= ShopItemsStandard[i] && ShopItemsN[i][j] <= ShopItemsSN[i][j]) {

				 // if (ShopItemsN[i][j] < ShopItemsSN[i][j] && ShopItemsSN[i][j] != -1 && (System.currentTimeMillis() - shopItemsRestock[i][j]) > restockTimeItem(ShopItems[i][j])) {//here prolly yeah.
						if (ShopItemsN[i][j] < ShopItemsSN[i][j] && ShopItemsSN[i][j] != -1) {
					  ShopItemsN[i][j] += 1;
					  ShopItemsDelay[i][j] = 1;
					  ShopItemsDelay[i][j] = 0;
					//  System.out.println("cur: "+System.currentTimeMillis() );
					  shopItemsRestock[i][j] = System.currentTimeMillis();
					//  System.out.println("curss: "+shopItemsRestock[i][j] );
					  DidUpdate = true;
					  //} else {
					  //		for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					  //					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1) || c.myShopId == 2) {
					  //						IsIn = true;
					  //						break;
					  //					}
					  //				}
//							ShopItemsN[i][j] += 1;
//							ShopItemsDelay[i][j] = 1;
//							ShopItemsDelay[i][j] = 0;
//							DidUpdate = true;
					  //}
					  //	if(ShopItemsStandard[i] != ShopItems)
					  //if the item isnt part of the original stock
				  } else if( ShopItemsSN[i][j] == -1){//i guess this is the solution
//					  ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
//					  ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
//					  ShopHandler.shopItemsRestock[c.myShopId][fromSlot] = 300000;
//					  if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
//						  ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
//					  }
					  				//	  if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {

					 // }
					  ShopItemsN[i][j] -= 1;
					  ShopItemsDelay[i][j] = 1;
					  ShopItemsDelay[i][j] = 0;
					  shopItemsRestock[i][j] = System.currentTimeMillis();
					  if( ShopItemsN[i][j] ==0)
						  ShopItems[i][j] = 0;

					  //ShopItems[i][j] = 0;
					  //shopItemsRestock[i][j] = System.currentTimeMillis();
//					  DidUpdate = true;
						} else if (ShopItemsDelay[i][j] >= MaxSpecShowDelay) {

						ShopItemsDelay[i][j] = 0;

							DidUpdate = true;
						} else{
						//	System.out.println("what happens here?");
						}
					} else {
						//System.out.println("what happens here2 because it hasnt delaye show it. LOOK: IF IT DOESNT HAPPEN IT DOESNT UPDATE!!!?");//this is what happens when u first sell the item to the general store
					}
					ShopItemsDelay[i][j]++;
				}
			}
			if (DidUpdate == true) {
				for (int k = 1; k < Configuration.MAX_PLAYERS; k++) {
					if (PlayerHandler.players[k] != null) {
						if (PlayerHandler.players[k].isShopping == true && PlayerHandler.players[k].myShopId == i) {
							PlayerHandler.players[k].updateShop = true;
							PlayerHandler.players[k].updateshop(i);
						}
					}
				}
				DidUpdate = false;
			}
		}
	}
	public void ResetItem(int ShopID, int ArrayID) {
		ShopItems[ShopID][ArrayID] = 0;
		ShopItemsN[ShopID][ArrayID] = 0;
		ShopItemsDelay[ShopID][ArrayID] = 0;
	}
}
