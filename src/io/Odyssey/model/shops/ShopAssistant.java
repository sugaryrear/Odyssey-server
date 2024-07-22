package io.Odyssey.model.shops;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.fireofexchange.FireOfExchange;
import io.Odyssey.content.lootbag.LootingBag;
import io.Odyssey.content.questing.hftd.HftdQuest;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.definitions.ShopDef;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.model.world.ShopHandler;
import io.Odyssey.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ShopAssistant {

	public static final int SHOP_INTERFACE_ID = 64000;
	public static final int SHOP_INTERFACE_ID2 = 3824;

	private final Player c;

	public ShopAssistant(Player client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS) && ShopID != 147) {
			c.sendMessage("You cannot do this right now.");
			return;
		}
	//		System.out.println("s:"+c.getMode().getType());
		if (!c.getMode().isShopAccessible(ShopID)) {

				c.sendMessage("Your game mode does not permit you to access this shop.");
				c.getPA().closeAllWindows();
				return;

		}
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
			c.sendMessage("You should stop what you are doing before opening a shop.");
			return;
		}

		setScrollHeight(ShopID);
		c.getPA().resetScrollPosition(64015);
		c.nextChat = 0;
		c.dialogueOptions = 0;
		c.getItems().sendInventoryInterface(3823);
		for(int i = 0 ;i <85;i++)
			c.getPA().sendFrame126("", 127_000+i);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;



		c.getPA().sendFrame248(SHOP_INTERFACE_ID, 3822);
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 64003);


	}

	private void setScrollHeight(int shopId) {
		int size = ShopHandler.getShopItems(shopId).size();
		//System.out.println("size: "+size);
		int defaultHeight = 253;
		//int rowHeight = (int) Math.ceil(size / 10.0) * 46;
		int rowHeight = size * 6;
		c.getPA().setScrollableMaxHeight(64015, Math.max(rowHeight, defaultHeight));
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Configuration.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true && PlayerHandler.players[i].myShopId == c.myShopId && i != c.getIndex()) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}
	static int[] storesthatdontchangeshopvalues = {9, 77,292,293,165,121};//yes. multi currency shop is retarded fuck that shit lol.

	static int[] storesthatcanhavesomedonatorpriceditems = {292,293,165,9};
	static int[] shop2buttons = {213,214};
	static int[] shop3buttons = {291,292,293,210,211,212};
	public static int[] mageshop = {291,292,293};
	public static int[] rangeshop = {210,211,212};
	public static int[] meleeshop = {213,214};
	/**
	 * what happens after a shop is opened
	 * @param ShopID
	 */
	public void resetShop(int ShopID) {
		// synchronized (c) {

		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		if (ShopID == 80) {
			c.getPA().sendInterfaceHidden(0, 64017);
		//	c.getPA().sendFrame126("PKP: " + Misc.insertCommas(Integer.toString(c.pkp)), 64019);
		} else {
			c.getPA().sendInterfaceHidden(1, 64017);
		}
		c.getPA().sendInterfaceHidden(1, 65017);
		c.getPA().sendInterfaceHidden(1, 65020);
		c.getPA().sendInterfaceHidden(1, 65023);


		if (c.getOutStream() != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeUnsignedWord(64016);
			c.getOutStream().writeUnsignedWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < TotalItems; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
					if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
					} else {
						c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
					}
					if (ShopHandler.ShopItems[ShopID][i] > Configuration.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
						ShopHandler.ShopItems[ShopID][i] = Configuration.ITEM_LIMIT;
					}
					c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}


		//what shop should we draw?
	int amt = 0;

		boolean generalshops = true;


		if(IntStream.of(storesthatdontchangeshopvalues).anyMatch(id -> id == ShopID))
			generalshops = false;
		//else

		//System.out.println("total: "+TotalItems);

		//System.out.println("general: "+generalshops);

		if(generalshops){
			for (int i = 0; i < TotalItems; i++){
				amt = getBuyFromShopPriceGeneralStore(ShopID,ShopHandler.ShopItems[ShopID][i] - 1,i);
				c.getPA().sendFrame126(amt == 0 ? "1" : ""+Misc.formatCoins(amt), 127_000+i);
			}
		} else {
			for (int i = 0; i < TotalItems; i++){
				amt = getBuyFromShopPrice(ShopID,ShopHandler.ShopItems[ShopID][i] - 1);
				c.getPA().sendFrame126(amt == 0 ? "1" : ""+Misc.formatCoins(amt), 127_000+i);
			}
		}
//				if(TotalItems > 85){
//			return;
//		}
for(int i = TotalItems >= 85 ? 0 : TotalItems; i < 85 ; i++){//adjust this. dont have more than 85 items in a shop for now.
	c.getPA().sendFrame126("", 127_000+i);
}


	}

	public static int getBuyFromShopPrice(int shopId, int itemId) {
		ShopDef def = ShopDef.get(shopId);
		if (def != null) {//if the price ISNT set in the shop
			int definitionPrice = def.getPrice(itemId);
			if (definitionPrice != Integer.MAX_VALUE && definitionPrice != 0)
				return definitionPrice;
		}

		return getItemShopValue(itemId);///default price from data
	}
	public static int getBuyFromShopPriceGeneralStore(int shopId, int itemId, int Slot) {
//		ShopDef def = ShopDef.get(shopId);
//		if (def != null) {//if the price ISNT set in the shop
//			int definitionPrice = def.getPrice(itemId);
//			if (definitionPrice != Integer.MAX_VALUE && definitionPrice != 0)
//				return definitionPrice;
//		}
		//System.out.println("uhh: "+ShopHandler.ShopItemsN[shopId][Slot]);
		//todo change 130 based on shopid
		//
	double value = Math.max((getItemShopValue(itemId) * (130 - 3 * ShopHandler.ShopItemsN[shopId][Slot]))/100, (30 * getItemShopValue(itemId))/100);
//ShopValue = Math.max((ShopValue * (130 - 3 * ShopHandler.ShopItemsN[c.myShopId][removeSlot]))/100, (30 * ShopValue)/100);
//		if(ShopHandler.ShopItemsN[shopId][Slot] == 0)
//			value = 0;

		return (int) value;///default price from data
	}

	public static int getItemShopValue(int itemId) {
		//System.out.println("item id: "+itemId);
		return ItemDef.forId(itemId).getShopValue();
	}

	/**
	 * buy item from shop (Shop Price)
	 **/
	public void buyFromShopPrice(int removeId, int removeSlot) {
		if (c.myShopId == 0)
			return;


		String itemName = ItemAssistant.getItemName(removeId);

		if(ShopHandler.ShopItemsN[c.myShopId][removeSlot] == 0){
			c.sendMessage(itemName + " is out of stock.");
			return;
		}
//		if (c.myShopId == FireOfExchangeBurnPrice.SHOP_ID) {
//			String formattedPrice = Misc.formatCoins(FireOfExchangeBurnPrice.getBurnPrice(c, removeId, false));
//			c.sendMessage("You can exchange {} for <col=255>{}</col> exchange points.", itemName, formattedPrice);
//			return;
//		}

		int ShopValue = (int) Math.floor(getBuyFromShopPrice(c.myShopId, removeId));
	//	System.out.println("shopv:"+ShopValue+" and n: "+ShopHandler.ShopItemsN[c.myShopId][removeSlot]+" or: "+ShopHandler.ShopItems[c.myShopId][removeSlot]);
		//if (c.myShopId != 29) {
			//			String amt = getBuyFromShopPrice(ShopID,ShopHandler.ShopItems[ShopID][i] - 1) == -1 ? "free" :""+Misc.formatCoins(getBuyFromShopPrice(ShopID,ShopHandler.ShopItems[ShopID][i] - 1));
			////ShopItemsN

		//yep all "currency stores" stores such as coins and tokkull obey laws of supply and demand.
ShopValue = Math.max((ShopValue * (130 - 3 * ShopHandler.ShopItemsN[c.myShopId][removeSlot]))/100, (30 * ShopValue)/100);
			//System.out.println("item:");
			//ShopValue *=1.27;
	//	}
	//	ShopValue *= 1.00;
		ShopValue = c.getMode().getModifiedShopPrice(c.myShopId, removeId, ShopValue);
		String ShopAdd = "";


		if (c.myShopId == 40) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " mage arena points.");
			return;
		}
		if (c.myShopId == 83) {
			c.sendMessage("You cannot buy items from this shop.");
			return;
		}
		if (c.myShopId == 44) {
			if (ItemDef.forId(removeId).getName().contains("head")) {
				c.sendMessage("This product cannot be purchased.");
				return;
			}
		}
		if (c.myShopId == 82) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " Assault points.");
			return;
		}
		if (c.myShopId == 118) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " Raid points.");
			return;
		}
		if (c.myShopId == 44) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " slayer points.");
			return;
		}
		if (c.myShopId == 13) {
			c.sendMessage("Jossik will switch " + itemName + " for " + getSpecialItemValue(removeId) + " rusty casket.");
			return;
		}
		if (c.myShopId == 19) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " steel arrows.");
			return;
		}
		if (c.myShopId == 10) {
			int price = getSpecialItemValue(removeId);
			if (price == 0) {
				c.sendMessage(itemName + " is free.");
				return;
			}
			c.sendMessage(itemName + ": currently costs [ <col=a30027>" +  Misc.insertCommas(price) + " </col>] Slayer Points.");
			return;
		}
		if (c.myShopId == 120) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " prestige points.");
			return;
		}
		if (c.myShopId == 77) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Vote points.");
			return;
		}
		if (c.myShopId == 300) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] prestige Points.");
			return;
		}
		if (c.myShopId == 80) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + Misc.insertCommas(getSpecialItemValue(removeId)) + " @bla@] PKP Points.");
			return;
		}
		if (c.myShopId == 121) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + Misc.insertCommas(getSpecialItemValue(removeId)) + " @bla@] Boss Points.");
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Christmas Points.");
			return;
		}
		if (c.myShopId == 171) {
			c.sendMessage(itemName + "@bla@: currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Exchange Points.");
			return;
		}
		if (c.myShopId == 172 || c.myShopId == 173) {
			c.sendMessage(itemName + ": currently exchanges for  [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Exchange Points.");
			return;
		}
		if (c.myShopId == 131) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Tournament Points.");
			return;
		}
		if (c.myShopId == 176) {
			c.sendMessage(itemName + ": currently costs [ @red@" + getSpecialItemValue(removeId) + " @bla@] Reward Tokens.");
			return;
		}
		if (c.myShopId == 119) {
			c.sendMessage(itemName + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Blood Money points.");
			return;
		}
		if (c.myShopId == 78) {
			c.sendMessage(itemName + ": currently costs [@lre@" + getSpecialItemValue(removeId) + " @bla@] Achievement Points.");
			return;
		}
		if (c.myShopId == 75) {
			c.sendMessage(itemName + ": currently costs [@gre@ " + getSpecialItemValue(removeId) + " @bla@] PC points.");
			return;
		}
		if (c.myShopId == 9) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " Donator points.");
			return;
		}

		if (c.myShopId == 18) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " marks of grace.");
			return;
		}
		if (c.myShopId == 115) {
			c.sendMessage(itemName + ": is completeley free.");
			return;
		}
		if (c.myShopId == 116) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " blood money.");
			return;
		}
		if (c.myShopId == 117) {
			c.sendMessage(itemName + ": currently costs " + getSpecialItemValue(removeId) + " blood money.");
			return;
		}

			if (IntStream.of(storesthatcanhavesomedonatorpriceditems).anyMatch(id -> id == c.myShopId)) {
			if(ShopHandler.ShopItemsN[c.myShopId][removeSlot] > 1_800_000_000 && ShopHandler.ShopItemsN[c.myShopId][removeSlot] < 1_900_000_000) {
				c.sendMessage(itemName + ": currently costs " + ShopValue + " Donator points.");
				return;
			}
		}
		if (c.myShopId == 29 || c.myShopId == 241) {
//			if (c.getRechargeItems().hasItem(11136)) {
//				ShopValue = (int) (ShopValue * 0.95);
//			}
//			if (c.getRechargeItems().hasItem(11138)) {
//				ShopValue = (int) (ShopValue * 0.9);
//			}
//			if (c.getRechargeItems().hasItem(11140)) {
//				ShopValue = (int) (ShopValue * 0.85);
//			}
//			if (c.getRechargeItems().hasItem(13103)) {
//				ShopValue = (int) (ShopValue * 0.75);
//			}
			c.sendMessage(itemName + ": currently costs " + Misc.insertCommas(ShopValue) + " tokkul" + ShopAdd);
			return;
		}
String amt = ShopValue <= 0 ? "1" : Misc.insertCommas(ShopValue);
		//System.out.println("amt: "+ShopHandler.ShopItemsN[c.myShopId][removeSlot]);
		//if(ShopHandler.ShopItemsN[c.myShopId][removeSlot] == -1)
			if(getBuyFromShopPrice(c.myShopId,ShopHandler.ShopItems[c.myShopId][removeSlot] - 1) == -1)
		c.sendMessage(itemName + " is free.");
		else
		c.sendMessage(itemName + ": currently costs " + amt + " coins (" + Misc.formatCoins(ShopValue <= 0 ? 1 :ShopValue) + ")");
	}

	public int getSpecialItemValue(int id) {
		ShopDef shopDef = ShopDef.getDefinitions().get(c.myShopId);

		switch (c.myShopId) {
			case 293://doesn't matter , set it in the actual .yml file as "cost"
				switch(id) {
					case 13235://eternal boots
						return 10;
				}
				break;
			case 19:
				switch(id) {
					case 10499:
						return 75;
				}

				break;
			//prestige shop
			case 300:
				switch(id) {
					case 10933:
					case 10939:
					case 10940:
					case 10941:
						return 30;
				}
				break;
			case 13: // Jossik quest shop
				return 1;
			case 15:
				switch(id) {
					case 12887:
					case 12888:
					case 12889:
					case 12890:
					case 12891:
					case 12892:
					case 12893:
					case 12894:
					case 12895:
					case 12896:
						return 80;
				}
			case 176:
				switch(id) {
					case 26850:
					case 26852:
					case 26854:
						return 50;

				}
				break;
			case 120:
				switch(id) {
					case 13317:
					case 13318:
					case 13319:
						return 30;
					case 4079:
						return 25;
					case 1037:
						return 75;

				}
				break;
			case 82:
				switch (id) {
					case 10548:
						return 30;
					case 10551:
						return 100;
					case 11898:
					case 11897:
					case 11896:
						return 25;
					case 11899:
					case 11900:
						return 25;
					case 11937:
					case 11936:
						return 10;
				}
				break;


			case 121: // Boss points shop
				return shopDef.getPrice(id);

			case 171: //Exchange Points
				return FireOfExchange.getExchangeShopPrice(id);
			case 172: //Exchange shop showcase
				switch (id) {
					case 4722://barrows start
					case 4720:
					case 4716:
					case 4718:
					case 4714:
					case 4712:
					case 4708:
					case 4710:
					case 4736:
					case 4738:
					case 4732:
					case 4734:
					case 4753:
					case 4755:
					case 4757:
					case 4759:
					case 4745:
					case 4747:
					case 4749:
					case 4751:
					case 4724:
					case 4726:
					case 4728:
					case 4730:// all barrows complete
					case 11836://bandos boots
						return 400;
					case 2577://ranger boots
					case 6585://fury
						return 2000;
					case 4151://whip
						return 750;
					case 6737://b ring
					case 6733://archer ring

					case 6731://seers ring

						return 1850;
					case 11834://bcp
					case 11832://tassets
					case 11826://army helm
					case 11828://army plate
					case 11830://arma leg
					case 13239:// primordials ect +
					case 13237://pegasion
					case 13235://eternal
						return 7500;
					case 13576://d warhammer
						return 14000;
					case 11802://ags
						return 12000;
					case 20784://claws
						return 17000;
					case 13265://abby dagger
					case 11808://zgs
					case 11804://bgs
					case 11806://sgs
						return 5000;
					case 13263://bludgon
					case 19552://zenyte brace
					case 19547://anguish
					case 19553://torture

						return 8500;
					case 12002: //occult
						return 800;
					case 12809://sara blessed
					case 12006://tent whip
						return 5000;
					case 11284://dfs
					case 19478://light ballista
					case 12853://amulet of damned
						return 6500;
					case 19481://heavy ballista
						return 13000;
					case 12821://spectral
					case 12825://arcane
						return 25000;
					case 12817://ely
						return 45000;
					case 11785://arma crossbow
						return 6500;
					case 21902:
						return 8500;
					case 21012://dhcb
						return 9000;
					case 12924://blowpipe
					case 12926:
						return 20000;
					case 11770://seers i
					case 11771://archer i
					case 11772://warrior i
					case 11773://b ring i
						return 5000;
					case 20997://t bow
						return 100000;
					case 12806://malediction
					case 12807://odium
						return 6000;
				}
				break;
			case 173: //Exchange shop showcase 2
				switch (id) {
					case 22322:
					case 21015:
					case 21003:
					case 12902:
					case 13196:
					case 13198:
					case 12929:
						return 9000;
					case 20517:
					case 20520:
					case 20595:
						return 1200;
					case 20095://ankou start
					case 20098:
					case 20101:
					case 20104:
					case 20107://ankou end
					case 20080://mummy start
					case 20083:
					case 20086:
					case 20089:
					case 20092://mummy end
						return 4750;
					case 12603:
					case 12605:
						return 1850;
					case 21902:
						return 7000;
					case 21006:
						return 10000;
					case 21018://justiciar + ancestral
					case 21021:
					case 21024:
					case 22326:
					case 22327:
					case 22328:
						return 25000;

				}
				break;

			case 119: //Blood Money Shop
				switch(id) {
					case 13307:
						return 1;
					case 1526:
					case 222:
					case 236:
					case 224:
					case 9737:
					case 232:
					case 226:
					case 240:
					case 244:
					case 6050:
					case 6052:
					case 3139:
					case 6694:
					case 246:
					case 1976:
					case 2971:
					case 248:
					case 9437:
						return 1;
					case 7409:
						return 750;
					case 20708:
					case 20704:
					case 20706:
					case 20710:
					case 12013:
					case 12014:
					case 12015:
					case 12016:
					case 10941:
					case 10939:
					case 10940:
					case 10933:
					case 13258:
					case 13259:
					case 13260:
					case 13261:
					case 13646:
					case 13642:
					case 13640:
					case 13644:
						return 500;

				}
				break;

//			case 80: //PK POINTS SHOP
//				switch (id) {
//					case 2996:
//						return 1;
//					case 13066:
//						return 1;
//					case 12004:
//						return 1600;
//					case 11802:
//						return 2400;
//					case 11804:
//					case 11806:
//					case 11808:
//						return 1400;
//					case 13576:
//						return 3000;
//					case 13267:
//						return 1800;
//					case 13263:
//						return 2200;
//					case 12765:
//					case 12766:
//					case 12768:
//					case 12767:
//						return 1000;
//					case 21012:
//						return 3000;
//					case 11785:
//						return 2500;
//					case 19481:
//						return 2000;
//					case 12806:
//					case 12807:
//						return 2000;
//					case 12924:
//						return 3000;
//					case 12855:
//					case 12856:
//						return 600;
//					case 12804:
//						return 1500;
//					case 12851:
//						return 1000;
//					case 12526:
//						return 350;
//					case 12849:
//						return 300;
//					case 12608:
//					case 12610:
//					case 12612:
//						return 225;
//					case 20716:
//						return 1550;
//					case 12846:
//						return 1000;
//					case 12786:
//						return 400;
//					case 12791:
//					case LootingBag.LOOTING_BAG:
//						return 250;
//					case 7462:
//						return 100;
//				}
//				break;

//			case 10: //slayer Shop
//				switch (id) {
//					case Items.ENCHANTED_GEM:
//						return 0;
//					case Items.FIGHTER_TORSO:
//						return 400;
//					case Items.PENANCE_SKIRT:
//						return 200;
//					case Items.FIGHTER_HAT:
//					case Items.FREMENNIK_KILT:
//					case Items.RUNNER_HAT:
//					case Items.HEALER_HAT:
//					case Items.RANGER_HAT:
//					case Items.PENANCE_GLOVES:
//						return 100;
//
//
//					case 7462:
//						return 100;
//					case 7509:
//						return 65;
//					case 405:
//						return 275;
//					case 4081:
//						return 475;
//					case 20581:
//						return 25;
//					case 23943:
//						return 300;
//					case 21183:
//						return 1700;
//					case 23824:
//						return 4;
//					case 13438:
//						return 1000;
//					case 11887:
//						return 10;
//				}
//				break;
			case 117:
				switch (id) {
					case 4716:
					case 4720:
					case 4722:
					case 4718: //Dharok
						return 120;
					case 4724:
					case 4726:
					case 4728:
					case 4730: //Guthan
					case 4745:
					case 4747:
					case 4749:
					case 4751: //Torag
					case 4753:
					case 4755:
					case 4757:
					case 4759: //Verac
						return 100;
					case 4708:
					case 4710:
					case 4712:
					case 4714: //Ahrim
					case 4732:
					case 4734:
					case 4736:
					case 4738: //Karil
						return 200;
					case 12006: //Abyssal tentacle
						return 400;
					case 13263: //Bludgeon
						return 3500;
					case 13271: //Abyssal dagger
						return 800;
					case 19481: //Ballista
						return 1500;
					case 12902: //Toxic staff
						return 1000;
					case 12924: //Blowpipe
						return 3000;
					case 11286: //Visage
						return 100;
					case 11785: //Armadyl crossbow
						return 2500;
					case 13227: //Crystals
					case 13229:
					case 13231:
					case 13233:
						return 1500;
					case 12695: //Super combat
						return 1;
					case 12929: //Serp helm
						return 1200;
					case 12831: //Blessed shield
						return 800;
					case 19529: //Zenyte shard
						return 1500;
					case 11832: //Bandos chest
					case 11834: //Bandos tassets
					case 11826: //Armadyl helm
					case 11828: //Armadyl chest
					case 11830: //Armadyl skirt
						return 700;
					case 6737: //Berseker ring
						return 500;
					case 6735: //Warrior ring
						return 50;
					case 6733: //Archers ring
						return 150;
					case 6731: //Seers ring
						return 150;
					case 12603: //Tyrannical ring
					case 12605: //Treasonous ring
						return 200;
					case 12853: //Amulet of the damned
						return 700;
					case 6585: //Fury
						return 150;
					case 11802: //Ags
						return 2000;
					case 11804: //Bgs
						return 200;
					case 11806: //Sgs
						return 1000;
					case 11808: //Zgs
						return 300;
					case 13576: //Dwh
						return 3000;
					case 11235: //Dbow
						return 150;
					case 11926: //Odium ward
					case 11924: //Malediction ward
						return 700;
					case 10551: //Torso
						return 150;
					case 10548: //Fighter hat
						return 100;
					case 11663: //Void mage helm
					case 11665: //Void melee helm
					case 11664: //Void ranger helm
					case 8842: //Void gloves
						return 50;
					case 8839: //Void top
					case 8840: //Void bottom
						return 75;
				}
				break;

//			case 2:
//				switch (id) {
//					case 527:
//						return 256;
//				}
//				break;
			case 240:
				switch (id) {
					case 6571: //uncut onyx
						return 300_000;

					case 12831: //Blessed spirit shield
						return 25;

				}
				break;
			case 116:
				if (ItemDef.forId(id).getName().contains("dharok")) {
					return 20;
				}
				if (ItemDef.forId(id).getName().contains("guthan") ||
						ItemDef.forId(id).getName().contains("torag") ||
						ItemDef.forId(id).getName().contains("verac") ||
						ItemDef.forId(id).getName().contains("karil")) {
					return 12;
				}
				if (ItemDef.forId(id).getName().contains("ahrim")) {
					return 14;
				}

				switch (id) {
					case 12695: //Super combat
						return 5;

					case 12831: //Blessed spirit shield
						return 25;

					case 11772: //Warriors ring
					case 12692: //Treasonous ring
					case 12691: //Tyrannical ring
						return 50;

					case 12924: //Blowpipe
					case 11770: //Rings
					case 11771:
					case 11773:
					case 12851: //Amulet of damned
					case 12853: //Amulet of damned
						return 75;

					case 11235: //Dbow
						return 150;

					case 12929: //Serp helmets
					case 13196:
					case 13198:
					case 13235: //Cerb boots
					case 13237:
					case 13239:
					case 19553: //Amulet of torture
					case 19547: //Anguish
						return 250;

					case 12807: //Wards
					case 12806:
						return 300;

					case 11804: //Godswords
					case 11806:
					case 11808:
						return 500;

					case 12902: //Tsotd
					case 13271: //Abyssal dagger
						return 800;

					case 11802: //Armadyl godsword
					case 13576: //D warhammer
					case 13263: //Abyssal bludgeon
						return 1000;

					case 19481: //Heavy ballista
						return 1500;

					case 12821: //Spectral
						return 2000;

					case 12825: //Arcane
						return 2500;

					case 12817: //Elysian
						return 3500;
				}
				break;
		}

		switch (id) {


		}
		//return Integer.MAX_VALUE;
		return (int) Math.floor(getBuyFromShopPrice(c.myShopId, id));
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId) {
		if (c.myShopId == 0)
			return;

		boolean CANNOT_SELL = !ItemDef.forId(removeId).isTradable();

		// Can't sell anglerfish
		if (removeId == 13441 || removeId == 13442) {
			c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + ".");
			return;
		}

		switch (c.myShopId) {
			case 9:
			case 77:

				c.sendMessage("You cannot sell items to this store.");
				return;
		}
		if (CANNOT_SELL) {
			c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + ".");
			return;
		}
		//
		boolean IsIn = false;
//make sure this shop HAS the item we are trying to sell...or it's a general store (2)
		for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
			if (removeId == (ShopHandler.ShopItems[c.myShopId][i] - 1) || c.myShopId == 2) {
				IsIn = true;
				break;
			}
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell that item to this store.");
			return;
		}

			double ShopValue = 0;
			String ShopAdd = "";


				ShopValue = (int) Math.floor(getItemShopValue(removeId));


				if (ShopValue >= 1_000_000)
					ShopAdd = " (" + Misc.formatCoins((long) ShopValue) + ")";
				int instock=0;
					for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
						if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
							instock = 	ShopHandler.ShopItemsN[c.myShopId][j];
							break;
						}
					}

					double buysatprice = 40.0;//perfect

		ShopValue = ((buysatprice - 3.0 * Math.min(instock,10)) / 100.0) * ShopValue;
if(ShopValue ==0)
	ShopValue = 1;

			String coinsorsomethingelse = c.myShopId == 29 || c.myShopId == 241 ? "Tokkul" : "coins";
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + Misc.insertCommas((int) ShopValue) + " "+coinsorsomethingelse+"" + ShopAdd + ".");

		}


	/**
	 * Selling items back to a store
	 * @param itemID
	 * 					itemID that is being sold
	 * @param fromSlot
	 * 					fromSlot the item currently is located in
	 * @param amount
	 * 					amount that is being sold
	 * @return
	 * 					true is player is allowed to sell back to the store,
	 * 					else false
	 */
	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.myShopId == 0 || !c.isInterfaceOpen(SHOP_INTERFACE_ID) && !c.isInterfaceOpen(SHOP_INTERFACE_ID2))
			return false;
//		if (Configuration.DISABLE_SHOP_SELL) {
//			c.sendMessage("Selling to shops is disabled atm.");
//			return false;
//		}
		if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
			c.sendMessage("You cannot do this right now.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return false;
		}
//		if (!c.getMode().isShopAccessible(c.myShopId)) {
//
//			c.sendMessage("Your game mode does not permit you to access this shop.");
//			c.getPA().closeAllWindows();
//			return false;
//
//		}
//		if (!c.getMode().isItemSellable(c.myShopId, itemID)) {
//			c.sendMessage("Your game mode does not permit you to sell this item to the shop.");
//			return false;
//		}
//		if (Configuration.DISABLE_FOE && c.myShopId == FireOfExchange.FOE_SHOP_ID) {
//			c.sendMessage("Fire of Exchange has been temporarily disabled.");
//			return false;
//		}
//		if (c.myShopId == FireOfExchangeBurnPrice.SHOP_ID)
//			return false;
		//if (c.myShopId != 115) {
			if (itemID == 995) {
				c.sendMessage("You can't sell this item.");
				return false;
			}
		//}
/**
 *
 * add shops you can never sell items to here.
 */
		switch (c.myShopId) {
			case 9:
			case 77:
				c.sendMessage("You cannot sell items to this store.");
				return false;
		}
		boolean CANNOT_SELL = !ItemDef.forId(itemID).isTradable();

			if (CANNOT_SELL) {
				c.sendMessage("You can't sell " + ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}

		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
				boolean IsIn = false;
//checks if the item is in stock of that store
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1) || c.myShopId == 2) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell that item to this store.");
					return false;
				}

			boolean noted = ItemDef.forId(c.playerItems[fromSlot] - 1).isNoted();
			boolean stackable = ItemDef.forId(c.playerItems[fromSlot] - 1).isStackable();
			if (amount > c.playerItemsN[fromSlot] && (noted || stackable)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID) && !noted && !stackable) {
				amount = c.getItems().getItemAmount(itemID);
			}

			int TotPrice2 = 0;
			int TotPrice3 = 0;



			TotPrice2 = (int) Math.floor(getItemShopValue(itemID));

//shops obey laws of supply and demand.
		//	if(c.myShopId == 2){
				int instock = 0;
				//				ShopValue = ((40.0 - 3.0 * Math.min(instock,10)) / 100.0) * ShopValue;
				for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
						instock = 	ShopHandler.ShopItemsN[c.myShopId][j];
						break;
					}
				}
			double buysatprice = 40.0;
			//the 40.0 needs to be changed BASED ON WHAT STORE WE ARE SELLING TO! GENERAL STORE (2) = 40.0 - buys at 40% of the value!
			//todo bandit general store has HIGHER VALUE that is buys at! very important.

//			if(c.myShopId == 2)
//				buysatprice = 40.0;

				TotPrice2 = (int) (((buysatprice - 3.0 * Math.min(instock,10)) / 100.0) * TotPrice2);

		//	}

			TotPrice3 = (int) Math.floor(getSpecialItemValue(itemID));

			TotPrice2 = TotPrice2 * amount;
			//TotPrice4 = TotPrice4 * amount;
			if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(995)) {
				if ((ItemDef.forId(itemID).isStackable() || ItemDef.forId(itemID).isNoted()) && c.getItems().playerHasItem(itemID, amount)) {//if its stackable (coins) or noted (like manta ray)
					c.getItems().deleteItemNoSave(itemID,c.getItems().getInventoryItemSlot(itemID),amount);
					logShop("sell", itemID, amount);
//for example you would add tokkull shops here etc.
					//oh lmao - so basically just yeah. thats what makes like bettys store work. its NOT one of these ids !
					if (c.myShopId != 12 && c.myShopId != 2 && c.myShopId != 29 && c.myShopId != 241 && c.myShopId != 44 && c.myShopId != 18  && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {//add all shops that DONT use coins as currency here
						c.getItems().addItem(995, TotPrice2);
						logShop("received", 995, TotPrice2);
					} else if (c.myShopId == 29 || c.myShopId == 241) { //tokkul shop
						c.getItems().addItem(6529, TotPrice2);
						//logShop("received", 6529, TotPrice2);
						//addShopItem(itemID, amount);
					} else if (c.myShopId == 18) {
					//	c.getItems().addItem(11849, TotPrice2);
						logShop("received", 11849, TotPrice2);
					} else if (c.myShopId == 2) {//general store
						c.getItems().addItem(995, TotPrice2);
					//	System.out.println("never gets here?");
					//	logShop("received", 995, TotPrice2);
						//addShopItem(itemID, amount);
					}  else if (c.myShopId == 116) {
					//	c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.30));
					//	logShop("received", 13307, (int) Math.ceil(TotPrice3 *= 0.30));
					} else if (c.myShopId == 44) {
						//if (!ItemDef.forId(itemID).getName().contains("head")) {
							//return false;
							//	} else {
						//	c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
						//	c.getQuestTab().updateInformationTab();
						//}
					}
					addShopItem(itemID, amount);
				//	System.out.println("gets her mult?");
					ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
					ShopHandler.shopItemsRestock[c.myShopId][fromSlot] = 60000;
					c.getItems().sendInventoryInterface(3823);
					resetShop(c.myShopId);
					updatePlayerShop();
					return false;
				} else {
					if (c.myShopId != 12 && c.myShopId != 2 && c.myShopId != 29 && c.myShopId != 241 && c.myShopId != 44 && c.myShopId != 18  && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {
						c.getItems().addItem(995, TotPrice2);
						logShop("received", 995, TotPrice2);
					} else if (c.myShopId == 29 || c.myShopId == 241) {//tokkuul shops
						c.getItems().addItem(6529, TotPrice2);
						logShop("received", 995, TotPrice2);
					//	addShopItem(itemID, amount);
					} else if (c.myShopId == 18) {
						c.getItems().addItem(11849, TotPrice2);
						logShop("received", 995, TotPrice2);
					} else if (c.myShopId == 2) {//general store add item?
						c.getItems().addItem(995, TotPrice2);
						logShop("received", 995, TotPrice2);
					//	addShopItem(itemID, amount);
					} else if (c.myShopId == 116) {
						c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.60));
						logShop("received", 13307, (int) Math.ceil(TotPrice3 *= 0.60));
					} else if (c.myShopId == 44) {
						if (!ItemDef.forId(itemID).getName().contains("head")) {
							return false;
						} else {
						//	c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
						//	c.getQuestTab().updateInformationTab();
						}
					}
					addShopItem(itemID, amount);
					if (!ItemDef.forId(itemID).isNoted()) {
						logShop("sell", itemID, amount);
						c.getItems().deleteItem2(itemID, amount);
					}

				}
			} else {
				c.sendMessage("You don't have enough space in your inventory.");
				c.getItems().sendInventoryInterface(3823);
				return false;
			}
			//}


		//	System.out.println("gets her?");
			ShopHandler.shopItemsRestock[c.myShopId][fromSlot] = 600000;
			c.getItems().sendInventoryInterface(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			PlayerSave.saveGame(c);
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
	//	System.out.println("her111e?");
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}

//		if (ItemDef.forId(itemID).isNoted()) {
//			//System.out.println("here?");
//			return false;
//
//		}
		//System.out.println("here?");
//		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
//			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
//				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
//				//System.out.println("hereadd?");
//				Added = true;
//			}
//		}

		for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
				ShopHandler.ShopItemsN[c.myShopId][j] += amount;
				Added = true;

			}
		}


		if (Added == false) {
			//System.out.println("whats this: "+ShopHandler.ShopItems.length);
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {//400 max items in a shop so just loop through it lmao. what if... hmm max??? yep do something when u try to sell MAX - just reset store lmfao
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {//add to first slot
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsSN[c.myShopId][i] = -1;
					//ShopHandler.ShopItemsDelay[c.myShopId][i] = 111110;
				//ShopHandler.ShopItemsStandard[c.myShopId]++;

//1698658667534 - 1698658613956
					//			ShopItems[id][itemIndex] = item.getId();
					//			ShopItemsN[id][itemIndex] = item.getAmount();
					//			ShopItemsSN[id][itemIndex] = item.getAmount();
					//			ShopItemsStandard[id]++;
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Buying item(s) from a store
	 * @param itemID
	 * 					itemID that the player is buying
	 * @param fromSlot
	 * 					fromSlot the items is currently located in
	 * @param amount
	 * 					amount of items the player is buying
	 * @return
	 * 					true if the player is allowed to buy the item(s),
	 * 					else false
	 */
	public boolean buyItem(int itemID, int fromSlot, int amount) {

		//System.out.println("n: "+ShopHandler.ShopItemsN[c.myShopId][fromSlot]+" sn: "+ShopHandler.ShopItemsSN[c.myShopId][fromSlot]+" ");
		if (c.myShopId == 0) {
		//	if (c.myShopId == 0 || !c.isInterfaceOpen(SHOP_INTERFACE_ID) && !c.isInterfaceOpen(SHOP_INTERFACE_ID2)) {
			return false;
		}
		if (Configuration.DISABLE_SHOP_BUY) {
			c.sendMessage("Buying from shops is disabled atm.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
//			if (c.debugMessage)
//				c.sendMessage("");
			return false;
		}
		if (c.myShopId == 26) {
			c.sendMessage("You can't buy from this store.");
			return false;

		}

		if (!Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS) && c.myShopId == 147) {
			c.sendMessage("You cannot do this right now.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS) && c.myShopId != 147) {
			c.sendMessage("You cannot do this right now.");
			return false;
		}
		if (!c.getMode().isItemPurchasable(c.myShopId, itemID)) {
			c.sendMessage("Your game mode does not allow you to buy this item.");
			return false;
		}
		//alright let's put items that need some sort of requirement other than coins/currency to purchase here.

		//if (c.myShopId == 178) {
			if (itemID == 2415 || itemID == 2416 || itemID == 2417) {
				if(c.maRound < 2){
					c.sendMessage("You must have completed @blu@Mage Arena I@bla@ to purchase this.");
					return false;
				}

			}
		if (itemID >= 3839  && itemID <= 3843) {
				if(!c.getQuesting().getQuestList().get(1).isQuestCompleted()){
				c.sendMessage("You must have completed @blu@Horror From the Deep@bla@ to purchase this.");
				return false;
			}

		}
		if (itemID >= 6106 && itemID <= 6111) {
			if(!c.deserttreasure){
				c.sendMessage("You must have completed @blu@Desert Treasure@bla@ to purchase this.");
				return false;
			}

		}
		//}
//		if (c.myShopId == 83) {
//			c.sendMessage("You cannot buy items from this shop.");
//			return false;
//		}
//		if (c.myShopId == FireOfExchangeBurnPrice.SHOP_ID)
//			return false;
//
//		if (Configuration.DISABLE_FOE && c.myShopId == FireOfExchange.FOE_SHOP_ID) {
//			c.sendMessage("Fire of Exchange has been temporarily disabled.");
//			return false;
//		}


			if (IntStream.of(storesthatcanhavesomedonatorpriceditems).anyMatch(id -> id == c.myShopId)) {

					if(ShopHandler.ShopItemsN[c.myShopId][fromSlot] > 1_800_000_000 && ShopHandler.ShopItemsN[c.myShopId][fromSlot] < 1_900_000_000) {

						handleOtherShop(itemID, amount);
				return false;
			}
		}
		/*
		 * ACHIEVMENT LOST N FOUND AREA
		 */
//		if (c.myShopId == 178) {
//			if (itemID == 10941 || itemID == 10933 && c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Woodcutting_Task_I.getId())) {
//				c.sendMessage("You have not yet unlocked this item.");
//				return false;
//			}
//		}

		if (itemID == 3841 || itemID == 3839 || itemID == 3843) {//can't buy multiple god books
			if (c.getItems().getItemCount(itemID, true) > 0){
				c.sendMessage("It seems like you already have one of these.");
			}
		}
		if (itemID == LootingBag.LOOTING_BAG) {
			if (c.getItems().getItemCount(LootingBag.LOOTING_BAG, true) > 0
					|| c.getItems().getItemCount(LootingBag.LOOTING_BAG_OPEN, true) > 0) {
				c.sendMessage("It seems like you already have one of these.");
				return false;
			}
		}
		if (itemID == 10941) {
			if (c.getItems().getItemCount(10941, true) > 0) {
				c.sendMessage("It seems like you already have one of these.");
				return false;
			}
		}
		/**
		 * Slayer shop
		 */
		if (c.myShopId == 44) {
			if (ItemDef.forId(itemID).getName().contains("head")) {
				c.sendMessage("This product cannot be purchased.");
				return false;
			}
		}
		/**
		 * Ava's
		 */
//		if (c.myShopId == 19) {
//			if (itemID == 10499) {
//				if (!c.getItems().playerHasItem(886, 75)) {
//					c.sendMessage("You must have 75 steel arrows to exchange for this attractor");
//					return false;
//				}
//				c.getItems().deleteItem(886, 775);
//				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.ATTRACTOR);
//			}
//		}
		/**
		 * RFD Shop
		 */
		if (c.myShopId == 14) {
			if (itemID == 7458 && c.rfdGloves < 1) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7459 && c.rfdGloves < 2) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7460 && c.rfdGloves < 3) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7461 && c.rfdGloves < 5) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7462) {
				if (c.rfdGloves < 6) {
					c.sendMessage("You are not eligible to buy these.");
					return false;
				}
			}
		}
		if (c.myShopId == 17) {
			skillBuy(itemID);
			return false;
		}
		if (c.myShopId == 179) {
			millBuy(itemID);
			return false;
		}
		if (!shopSellsItem(itemID)) {//hmmmm
			System.out.println("shop doesnt sell this!");
			return false;
		}
		if (amount > 0) {
			if (fromSlot >= ShopHandler.ShopItemsN[c.myShopId].length) {
				c.sendMessage("There was a problem buying that item, please report it to staff!");
				return false;
			}
//System.out.println("N: "+ ShopHandler.ShopItemsN[c.myShopId][fromSlot]+" and :"+ ShopHandler.ShopItems[c.myShopId][fromSlot]);
		//	if (c.myShopId != 115) {
				if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
					amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
				}
		//	}

			int TotPrice2 = 0;
			int Slot = 0;
			int Slot1 = 0;

			switch (c.myShopId) {
				case 9:
				case 77:
				case 121:
					handleOtherShop(itemID, amount);
					return false;
			}
			TotPrice2 = (int) Math.floor(getBuyFromShopPrice(c.myShopId, itemID));
			TotPrice2 = c.getMode().getModifiedShopPrice(c.myShopId, itemID, TotPrice2);
			//System.out.println("pr1: "+TotPrice2+" stock: "+ShopHandler.ShopItemsN[c.myShopId][fromSlot]);

			//general store calculation:

			//if (c.myShopId == 2) {

				TotPrice2 = Math.max( (TotPrice2 * (130 - 3 * ShopHandler.ShopItemsN[c.myShopId][fromSlot])) / 100, (30 * TotPrice2)/100);
				//System.out.println("item:");
				//ShopValue *=1.27;
		//	}
		//System.out.println("pr2: "+TotPrice2);
			Slot = c.getItems().getInventoryItemSlot(995);
			Slot1 = c.getItems().getInventoryItemSlot(6529);
//			if (TotPrice2 <= 1) {
//				TotPrice2 = (int) Math.floor(getItemShopValue(itemID));
//				TotPrice2 *= 1.66;
//			}
//			if (c.myShopId == 115) {
//				TotPrice2 = -1;
//			}
//			if (c.myShopId == 147) {
//				TotPrice2 = 0;
//			}
			//System.out.println("pr2after: "+TotPrice2);
//			if(c.myShopId==124 && c.amDonated>=150 && itemID == 299){
//				TotPrice2=0;
//			}

			if (ItemDef.forId(itemID).isStackable()) {
				if (c.myShopId != 29 || c.myShopId != 147 || c.myShopId != 241) {//add shops that ARENT coin currency here
				//	if (TotPrice2 != 0) {
//						if (!c.getItems().playerHasItem(995, TotPrice2 * amount) || TotPrice2 == 0) {
//							int coinAmount = c.getItems().getItemAmount(995);
////if(TotPrice2 == 0)
////	TotPrice2 = 1;
//							int amountThatCanBeBought = (int) Math.floor(coinAmount / TotPrice2);
//							if (TotPrice2 == 0) {
//								amountThatCanBeBought = 1000;
//							}
//							if (amountThatCanBeBought > 0) {
//								amount = amountThatCanBeBought;
//							}
//							c.sendMessage("You don't have enough coins.");
//						}

				//	}
if(TotPrice2 == 0)
	TotPrice2 = 1;
					if (c.getItems().playerHasItem(995, TotPrice2 * amount)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995, TotPrice2 * amount);
							c.getItems().addItem(itemID, amount);
							logShop("bought", itemID, amount);
							//if (c.myShopId != 115) {
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							ShopHandler.shopItemsRestock[c.myShopId][fromSlot] = 300000;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							//}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							c.getItems().sendInventoryInterface(3823);
							return false;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						c.getItems().sendInventoryInterface(3823);
						return false;
					}
				}
			} else if (c.myShopId == 29 || c.myShopId == 241) {
//				if (c.getRechargeItems().hasItem(11136)) {
//					TotPrice2 = (int) (TotPrice2 * 0.95);
//				}
//				if (c.getRechargeItems().hasItem(11138)) {
//					TotPrice2 = (int) (TotPrice2 * 0.9);
//				}
//				if (c.getRechargeItems().hasItem(11140)) {
//					TotPrice2 = (int) (TotPrice2 * 0.85);
//				}
//				if (c.getRechargeItems().hasItem(13103)) {
//					TotPrice2 = (int) (TotPrice2 * 0.75);
//				}
				if(Slot1 == -1){
					c.sendMessage("You don't have enough tokkul.");
					return false;
				}
				if (c.playerItemsN[Slot1] >= TotPrice2 * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.getItems().deleteItem(6529, TotPrice2 * amount);
						c.getItems().addItem(itemID, amount);
						logShop("bought", itemID, amount);
						ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
						ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
						if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {//prolly here is where the left over prices are being written lol.
							ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
						}
					} else {
						c.sendMessage("You don't have enough space in your inventory.");
						c.getItems().sendInventoryInterface(3823);
						return false;
					}
				} else {
					c.sendMessage("You don't have enough tokkul.");
					c.getItems().sendInventoryInterface(3823);
					return false;
				}
			} else {
				int boughtAmount = 0;


				// Iterate and buy each single item individually
				for (int i = amount; i > 0; i--) {
					if (c.myShopId == 115) {
						TotPrice2 = -1;
					}
					if (c.myShopId == 147) {
						TotPrice2 = -1;
					}
					if (Slot == -1 && c.myShopId != 29 && c.myShopId != 241 && c.myShopId != 115 && c.myShopId != 147) {
						c.sendMessage("You don't have enough coins.");
						return false;
					}
					if (Slot1 == -1 && (c.myShopId == 29 || c.myShopId == 241)) {
						c.sendMessage("You don't have enough tokkul.");
						return false;
					}

					// not tokkul
					if (c.myShopId != 29 && c.myShopId != 241) {
						if (c.getItems().playerHasItem(995, TotPrice2) || TotPrice2 == 0) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(995, c.getItems().getInventoryItemSlot(995), TotPrice2);
								c.getItems().addItem(itemID, 1);
								boughtAmount++;
								if (c.myShopId == 6) {
									if (c.getMode().isIronmanType()) {
										if (!c.getDiaryManager().getVarrockDiary().hasDoneMedium()) {
											c.sendMessage("You must have completed the varrock diary up to medium to purchase this.");
											return false;
										}
										if (c.getRechargeItems().useItem(2, 1)) {

										} else {
											c.sendMessage("You have already purchased 150 battlestaves today.");
											return false;
										}
									}
								}
							//	if (c.myShopId != 115) {
									ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.shopItemsRestock[c.myShopId][fromSlot] = 300000;
							//	}
								//custom delay here lol
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().sendInventoryInterface(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough coins.");
							c.getItems().sendInventoryInterface(3823);
							return false;
						}
					}

					// tokkul
					if (c.myShopId == 29 || c.myShopId == 241) {
//						if (c.getRechargeItems().hasItem(11136)) {
//							TotPrice2 = (int) (TotPrice2 * 0.95);
//						}
//						if (c.getRechargeItems().hasItem(11138)) {
//							TotPrice2 = (int) (TotPrice2 * 0.9);
//						}
//						if (c.getRechargeItems().hasItem(11140)) {
//							TotPrice2 = (int) (TotPrice2 * 0.85);
//						}
//						if (c.getRechargeItems().hasItem(13103)) {
//							TotPrice2 = (int) (TotPrice2 * 0.75);
//						}
						if (c.playerItemsN[Slot1] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(6529, c.getItems().getInventoryItemSlot(6529), TotPrice2);
								c.getItems().addItem(itemID, 1);
								//and delay for tokkuul shops here yep
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().sendInventoryInterface(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough tokkul.");
							c.getItems().sendInventoryInterface(3823);
							return false;
						}
					}
					if (c.myShopId == 176) {
						if (c.playerItemsN[Slot1] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(7776, c.getItems().getInventoryItemSlot(7776), TotPrice2);
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().sendInventoryInterface(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough tokkul.");
							c.getItems().sendInventoryInterface(3823);
							return false;
						}
					}
				}



				if (boughtAmount > 0) {
					logShop("bought", itemID, boughtAmount);
				}
			}
			c.getItems().sendInventoryInterface(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void logShop(String action, int itemId, int amount) {
		try {
			switch (action) {
				case "bought":
				//	Server.getLogging().write(new ShopBuyLog(c, c.myShopId, ShopHandler.ShopName[c.myShopId], new GameItem(itemId, amount)));
					break;
				case "sell":
				//	Server.getLogging().write(new ShopSellLog(c, c.myShopId, ShopHandler.ShopName[c.myShopId], new GameItem(itemId, amount)));
					break;
				case "received": // Ignore this one for now
					break;
				default:
					throw new IllegalArgumentException("Action not supported " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Special currency stores
	 * @param itemID
	 * 					itemID that is being bought
	 * @param amount
	 * 					amount that is being bought
	 */
	public void handleOtherShop(int itemID, int amount) {
		if (amount <= 0) {
			if (c.myShopId == 172 || c.myShopId == 173) {
				c.sendMessage("This item cannot be bought, its on showcase only.");
				return;
			}
			c.sendMessage("You need to buy at least one or more of this item.");
			return;
		}
		if (!c.getItems().isStackable(itemID)) {
			if (amount > c.getItems().freeSlots()) {
				amount = c.getItems().freeSlots();
			}
		}
		if (c.myShopId == 40) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need at least one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getArenaPoints() < itemValue) {
				c.sendMessage("You do not have enough arena points to buy this from the shop.");
				return;
			}
			c.setArenaPoints(c.getArenaPoints() - itemValue);
			c.getQuestTab().updateInformationTab();
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 82) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need at least one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getShayPoints() < itemValue) {
				c.sendMessage("You do not have enough assault points to buy this from the shop.");
				return;
			}
			c.setShayPoints(c.getShayPoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 118) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need at least one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getRaidPoints() < itemValue) {
				c.sendMessage("You do not have enough raid points to buy this from the shop.");
				return;
			}
			c.setRaidPoints(c.getRaidPoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 120) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need at least one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getPrestigePoints() < itemValue) {
				c.sendMessage("You do not have enough prestige points to buy this from the shop.");
				return;
			}
			c.prestigePoints =(c.getPrestigePoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 13) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(HftdQuest.CASKET_TO_BUY_BOOK, itemValue)) {
				c.sendMessage("You do not have enough rusty caskets to switch with Jossik.");
				return;
			}
			c.getItems().deleteItem(HftdQuest.CASKET_TO_BUY_BOOK, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 19) {//ava's
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(Items.STEEL_ARROW, itemValue)) {
				c.sendMessage("You do not have enough steel arrows.");
				return;
			}
			c.getItems().deleteItem(Items.STEEL_ARROW, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 117 && itemID == 12695) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(13307, itemValue)) {
				c.sendMessage("You do not have enough blood money to purchase this.");
				return;
			}
			c.getItems().deleteItem(13307, itemValue);
			c.getItems().addItem(itemID + 1, amount * 2);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 116/* || c.myShopId == 117*/) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(13307, itemValue)) {
				c.sendMessage("You do not have enough blood money to purchase this.");
				return;
			}
			c.getItems().deleteItem(13307, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}

		if (c.myShopId == 18) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(11849, itemValue)) {
				c.sendMessage("You do not have enough marks of grace to purchase this.");
				return;
			}
			c.getItems().deleteItem(11849, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().sendInventoryInterface(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 79) {

			if (c.getItems().playerHasItem(itemID) || c.getItems().isWearingItem(itemID) || c.getItems().bankContains(itemID)) {
				c.sendMessage("You still have this item, you have not lost it.");
				return;
			}
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			if (!c.getItems().playerHasItem(995, 500_000)) {
				c.sendMessage("You need at least 500,000GP to purchase this item.");
				return;
			}
			c.getItems().deleteItem2(995, 500_000);
			c.getItems().addItem(itemID, 1);
			c.getItems().sendInventoryInterface(3823);
			c.sendMessage("You have redeemed the " + ItemAssistant.getItemName(itemID) + ".");
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 44) {
			if (itemID == 13226) {
				if (c.getItems().getItemCount(13226, true) == 1) {
					c.sendMessage("You already have a herb sack, theres no need for another.");
					return;
				}
			}
			if (itemID == 12020) {
				if (c.getItems().getItemCount(12020, true) == 1) {
					c.sendMessage("You already have a gem bag. theres no need for another.");
					return;
				}
			}
			if (c.getSlayer().getPoints() >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayer().setPoints(c.getSlayer().getPoints() - (getSpecialItemValue(itemID) * amount));
					int mult = 1;
					if(itemID ==4160 || itemID ==  11875)
						mult = 250;
					c.getItems().addItem(itemID, amount*mult);
					c.getItems().sendInventoryInterface(3823);
					c.getQuestTab().updateInformationTab();
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}

		} else

			if (IntStream.of(storesthatcanhavesomedonatorpriceditems).anyMatch(id -> id == c.myShopId)) {
		int ShopValue = (int) Math.floor(getBuyFromShopPrice(c.myShopId, itemID));

			if (c.donatorPoints >= ShopValue * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.donatorPoints -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 10) {
			if (c.getSlayer().getPoints() >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayer().setPoints(c.getSlayer().getPoints() - (getSpecialItemValue(itemID) * amount));
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					c.getQuestTab().updateInformationTab();
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 78) {
			if (c.getAchievements().points >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getAchievements().points -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough achievement points to buy this item.");
			}
		} else if (c.myShopId == 75) {
			if (c.pcPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough PC Points to buy this item.");
			}
		} else if (c.myShopId == 77) {
			if (c.votePoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		} else if (c.myShopId == 121) {
			if (c.bossPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.bossPoints -= getSpecialItemValue(itemID) * amount;
					//c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough Boss Points to buy this item.");
			}
		} else if (c.myShopId == 80) {
			if (c.pkp >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pkp -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					c.getShops().openShop(80);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough PKP Points to buy this item.");
			}
		} else if (c.myShopId == 300) {
			if (c.prestigepoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.prestigepoints -= getSpecialItemValue(itemID) * amount;
					//	c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					//	c.getShops().openShop(80);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough prestigepoints Points to buy this item.");
			}
		} else if (c.myShopId == 171) {
			if (c.exchangePoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.exchangePoints -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough Exchange Points to buy this item.");
			}
		} else if (c.myShopId == 172 || c.myShopId == 173) {
			if (c.showcase >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.showcase -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("This item is only a showcase.");
			}
		} else if (c.myShopId == 131) {
			if (c.tournamentPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.tournamentPoints -= getSpecialItemValue(itemID) * amount;
					c.getQuestTab().updateInformationTab();
					c.getItems().addItem(itemID, amount);
					if (itemID == 8132 && c.getItems().getItemCount(8132, false) == 0) {
						c.getCollectionLog().handleDrop(c, 5, 8132, 1);
					}
					if (itemID == 10591 && c.getItems().getItemCount(10591, false) == 0) {
						c.getCollectionLog().handleDrop(c, 5, 10591, 1);
					}
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough Tournament Points to buy this item.");
			}
		} else if (c.myShopId == 119) {
			if (c.bloodPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.bloodPoints -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().sendInventoryInterface(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough blood money points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 17;
		setupSkillCapes(get99Count());
	}
	public void openMillCape() {
		int capes = 23;
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 179;
		c.getShops().openShop(179);
		//setupMillCapes(capes, 23);
	}
	public void setupSkillCapes(int capes2) {
		c.getPA().sendInterfaceHidden(1, 28050);
		c.getPA().sendInterfaceHidden(1, 28053);
		c.getItems().sendInventoryInterface(3823);
		c.isShopping = true;
		c.myShopId = 17;
		c.getPA().sendFrame248(SHOP_INTERFACE_ID2, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}

		if (c.getOutStream() != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeUnsignedWord(3900);
			c.getOutStream().writeUnsignedWord(TotalItems);
			for (int i = 0; i < 22; i++) {
				if (c.getLevelForXP(c.playerXP[i]) < 99)
					continue;
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void millBuy(int item) {
		int millcapeskill = (item - 33033);
		if (c.getItems().freeSlots() > 1) {
			if (c.getItems().playerHasItem(995, 10000000)) {
				if (c.playerXP[millcapeskill] >= 200000000) {
					c.getItems().deleteItem(995, c.getItems().getInventoryItemSlot(995), 10000000);

					c.getItems().addItem(item, 1);


				} else {
					c.sendMessage("You must have 200m XP in the skill of the cape you're trying to buy.");
				}
			} else {
				c.sendMessage("You need 10m to buy this item.");
			}
		} else {
			c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
		}


	}



// , str35, range37, prayer38, wc41,,
//	  smithing46,  thieving50, slayer51, rc53,

	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < 22; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}


	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getInventoryItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().sendInventoryInterface(3823);
	}

}
