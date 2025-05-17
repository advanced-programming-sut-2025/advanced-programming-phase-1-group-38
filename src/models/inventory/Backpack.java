package models.inventory;

import models.Item;
import models.Tool;
import models.enums.Types.BackpackType;
import models.enums.Types.ItemType;

import java.util.LinkedHashMap;
import java.util.Map;

public class Backpack extends Inventory {
    private BackpackType type;
    private Map<Tool, Integer> toolView;

    public Backpack(BackpackType type) {
        super(type.getCapacity(), type.isUnlimited());
        this.type = type;
        this.items = new LinkedHashMap<>();
        this.toolView = new LinkedHashMap<>();
    }
    public BackpackType getType() {
        return this.type;
    }

    public void upgradeBackpack(BackpackType newType) {
        this.type = newType;
        this.capacity = newType.getCapacity();
        this.isCapacityUnlimited = newType.isUnlimited();
    }

    @Override
    public boolean hasSpaceFor(Item item, int quantity) {
        if (isCapacityUnlimited) return true;
        return items.containsKey(item) || items.size() < capacity;
    }

    @Override
    public void addToInventory(Item item, int quantity) {
        if (!hasSpaceFor(item, quantity)) return;
        items.put(item, items.getOrDefault(item, 0) + quantity);

        if (item instanceof Tool tool) {
            toolView.put(tool, toolView.getOrDefault(tool, 0) + quantity);
        }
    }

    @Override
    public void CheatAddToInventory(Item item, int quantity) {
        if (!hasSpaceFor(item, quantity)) return;
        items.put(item, items.getOrDefault(item, 0) + quantity);

        if (item instanceof Tool tool) {
            toolView.put(tool, toolView.getOrDefault(tool, 0) + quantity);
        }
    }

    @Override
    public void removeFromInventory(Item item, int quantity) {
        int current = items.getOrDefault(item, 0);
        if (current <= quantity) {
            items.remove(item);
        } else {
            items.put(item, current - quantity);
        }

        if (item instanceof Tool tool) {
            int toolCount = toolView.getOrDefault(tool, 0);
            if (toolCount <= quantity) {
                toolView.remove(tool);
            } else {
                toolView.put(tool, toolCount - quantity);
            }
        }
    }

    @Override
    public boolean containsItem(Item item) {
        return items.containsKey(item);
    }

    public Map<Tool, Integer> getTools() {
        return new LinkedHashMap<>(toolView);
    }

    public int getItemCountByType(ItemType type) {
        return items.entrySet().stream()
            .filter(entry -> entry.getKey().getType() == type)
            .mapToInt(Map.Entry::getValue)
            .sum();
    }

    public Map<Item, Integer> getItems() {
        return new LinkedHashMap<>(items);
    }

}
