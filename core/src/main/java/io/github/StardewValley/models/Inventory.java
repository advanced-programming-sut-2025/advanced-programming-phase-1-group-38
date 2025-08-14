package io.github.StardewValley.models;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final int slotCount;
    private final List<Stack> slots;

    public Inventory(int slotCount) {
        this.slotCount = slotCount;
        slots = new ArrayList<>(slotCount);
        for (int i = 0; i < slotCount; i++) {
            slots.add(new Stack());
        }
    }

    public int add(ItemType type, int amount) {
        for (Stack s : slots) {
            if (s.getType() == type && s.canAcceptMore()) {
                amount = s.add(amount);
                if (amount == 0) return 0;
            }
        }
        for (Stack s : slots) {
            if (s.isEmpty()) {
                amount = s.fill(type, amount);
                if (amount == 0) return 0;
            }
        }
        return amount;
    }

    public int size() {
        return slotCount;
    }

    public Stack peek(int i) {
        if (i < 0 || i >= slots.size()) return null;
        Stack s = slots.get(i);
        return s.isEmpty() ? null : s;
    }

    public ItemType peekType(int i) {
        Stack s = peek(i);
        return (s == null) ? null : s.getType();
    }

    public int peekQty(int i) {
        Stack s = peek(i);
        return (s == null) ? 0 : s.getQty();
    }

    public boolean contains(ItemType type, int atLeast) {
        int total = 0;
        for (Stack stack : slots) {
            if (stack.getType() == type) {
                total += stack.getQty();
                if (total >= atLeast) return true;
            }
        }
        return false;
    }

    public int getTotalQty(ItemType type) {
        int total = 0;
        for (Stack s : slots) {
            if (s.getType() == type) {
                total += s.getQty();
            }
        }
        return total;
    }

    // Inventory.java
    public int remove(ItemType type, int amount) {
        for (int i = 0; i < slots.size(); i++) {
            Stack s = slots.get(i);
            if (s.getType() != type) continue;

            int take = Math.min(s.qty, amount);
            s.qty   -= take;
            amount  -= take;

        /* ───────────────────────────────────────────────
           if that stack is now empty → compact the list
           ─────────────────────────────────────────────── */
            if (s.qty == 0) {
                // shift everything left  (i+1 .. end)
                for (int j = i + 1; j < slots.size(); j++) {
                    slots.set(j - 1, slots.get(j));
                }
                // last slot becomes a brand‑new empty Stack
                slots.set(slots.size() - 1, new Stack());
                // don’t increment i: the item that shifted into
                // this position might also be removable
                i--;
            }
            if (amount == 0) break;   // removed everything we needed
        }
        return amount;               // leftover (0 = success)
    }

    public ItemType get(int index) {                      // <-- to fix “Cannot resolve get”
        return peekType(index);
    }

    // Hard-sets a slot to an item/qty (overwrites whatever was there)
    public void set(int index, ItemType type, int qty) {  // <-- to fix “Cannot resolve set”
        if (index < 0 || index >= slots.size()) return;
        Stack s = slots.get(index);
        s.type = type;
        s.qty  = Math.max(0, qty);
    }

    public static class Stack {
        public ItemType type;
        public int qty;

        public int add(int add) {
            if (type == null) return add;
            int cap = (type.maxStack() == 0) ? Integer.MAX_VALUE : type.maxStack();
            int room = cap - qty;
            int accepted = Math.min(room, add);
            qty += accepted;
            return add - accepted;
        }

        public boolean canAcceptMore() {
            if (type == null) return false;
            int cap = (type.maxStack() == 0) ? Integer.MAX_VALUE : type.maxStack();
            return qty < cap;
        }

        public boolean isEmpty() {
            return type == null;
        }

        public int fill(ItemType t, int add) {
            this.type = t;
            return add(add);
        }

        public ItemType getType() {
            return type;
        }

        public int getQty() {
            return qty;
        }
    }
}
