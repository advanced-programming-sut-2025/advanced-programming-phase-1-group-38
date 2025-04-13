package models.shops;

import models.Item;
import models.NPC;
import models.Shop;
import models.enums.Types.ShopType;

import java.util.HashMap;

public class BlacksmithShop extends Shop {

    public BlacksmithShop(NPC owner) {
        super(ShopType.BLACKSMITH);
        this.name = "Blacksmith";
        this.owner = owner;
        this.balance = 10000; // Optional starting balance

        this.shopInventory = new HashMap<>();
    }

    public void showAvailableProducts() {
    }
}
