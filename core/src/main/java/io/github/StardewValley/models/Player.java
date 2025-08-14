package io.github.StardewValley.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.StardewValley.models.Artisan.ArtisanProductType;
import io.github.StardewValley.models.enums.Types.MaterialType;
import io.github.StardewValley.models.farming.TreeSaplingType;

import java.util.*;

public class Player {
    private float x;
    private float y;
    private Animation<TextureRegion> currentAnimation;
    private String currentAnimationKey = "";
    private float animationTime = 0f;
    private String facingDirection = "down";
    private final Inventory inventory;
    private final InventoryRenderer inventoryRenderer;
    private final Inventory fridge;
    private float actionCooldown = 0f;
    private boolean isCurrentAnimationLooping = true;
    private final List<CookingRecipe> recipeList;
    GameEconomy gameEconomy;
    // === Energy ===
    private int maxEnergy = 270;      // قابل‌تنظیم
    private int energy = maxEnergy;

    public boolean hasEnergy(int cost) { return energy >= cost; }
    public void consumeEnergy(int cost) { energy = Math.max(0, energy - cost); }
    public void restoreEnergy(int amount) { energy = Math.min(maxEnergy, energy + amount); }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public float getEnergy01() { return maxEnergy == 0 ? 0f : (energy / (float)maxEnergy); }
    private final Skills skills = new Skills();
    public Skills getSkills() { return skills; }
    // افزایش سقف انرژی (مثلاً با لِوِل‌آپ Foraging)
    public void addMaxEnergy(int delta) {
        maxEnergy = Math.max(1, maxEnergy + delta);
        energy = Math.min(energy, maxEnergy);
    }

    public Player(float x, float y, int inventorySize) {
        this.x = x;
        this.y = y;
        this.setAnimation("idle_down", "character/idle/down", 2, 0.4f, true);
        this.inventory = new Inventory(inventorySize);
        this.gameEconomy = new GameEconomy(250);
        this.fridge = new Inventory(36);
        this.inventoryRenderer = new InventoryRenderer(inventory);

        this.recipeList = RecipeBook.getAllRecipes();

        // Add some initial items
        inventory.add(ToolType.SCYTHE, 1);
        inventory.add(ToolType.PICKAXE, 1);
        inventory.add(ToolType.AXE, 1);
        inventory.add(ToolType.WATERCAN, 1);
        inventory.add(CropType.CARROT, 100);
        inventory.add(SeedType.CORN_SEED, 20);
        inventory.add(SeedType.CARROT_SEED, 20);
        inventory.add(CropType.CORN, 10);
        inventory.add(MaterialType.Wood, 1000);
        inventory.add(ArtisanProductType.COFFEE, 20);
        inventory.add(TreeSaplingType.APPLE_SAPLING, 1);
        inventory.add(MaterialType.Stone, 1000);
        inventory.add(MaterialType.IronOre, 1000);
        inventory.add(MaterialType.IronBar, 1000);
        inventory.add(MaterialType.GoldBar, 1000);
        inventory.add(MaterialType.Coal, 1000);
        inventory.add(MaterialType.CopperOre, 1000);
        inventory.add(ItemCatalog.chickenCrate(), 1);
        inventory.add(ItemCatalog.cowCrate(), 1);
        inventory.add(CropType.WHEAT, 20);

    }

    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {
            TextureRegion frame = currentAnimation.getKeyFrame(animationTime, isCurrentAnimationLooping);
            batch.draw(frame, x, y);
        }
    }


    public void update(float delta) {
        animationTime += delta;
        if (actionCooldown > 0f) {
            actionCooldown -= delta;
            if (actionCooldown <= 0f) {
                String dir = getFacingDirection();
                setAnimation("idle_" + dir, "character/idle/" + dir, 2, 0.4f, true);
            }
        }
    }



    public void setActionCooldown(float seconds) {
        this.actionCooldown = seconds;
    }

    public boolean isActionLocked() {
        return actionCooldown > 0f;
    }

    public void moveBy(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }


    public float getX() { return x; }
    public float getY() { return y; }

    public Inventory getInventory() { return inventory; }
    public InventoryRenderer getInventoryRenderer() { return inventoryRenderer; }
    public Inventory getFridgeInventory() {
        return fridge;
    }

    public List<CookingRecipe> getAllCookingRecipes() {
        return recipeList;
    }

    public void dispose() {
        inventoryRenderer.dispose();
    }

    public void setAnimation(String key, String basePath, int frameCount, float frameDuration, boolean looping) {
        if (!key.equals(currentAnimationKey)) {
            currentAnimation = GameAssetManager.getGameAssetManager().getFrameAnimation(
                key, basePath, frameCount, frameDuration, looping
            );
            currentAnimationKey = key;
            animationTime = 0f;
            isCurrentAnimationLooping = looping;

            // Normalize direction
            if (key.startsWith("walk/")) {
                facingDirection = key.replace("walk/", "");  // e.g. "down"
            } else if (key.startsWith("idle_")) {
                facingDirection = key.replace("idle_", "");
            }
        }
    }


    public String getFacingDirection() {
        return facingDirection;
    }

    public TextureRegion getCurrentFrame() {
        if (currentAnimation != null) {
            return currentAnimation.getKeyFrame(animationTime, isCurrentAnimationLooping);
        }
        return null;
    }


    public float getWidth() {
        TextureRegion frame = getCurrentFrame();
        return frame != null ? frame.getRegionWidth() : 0;
    }

    public float getHeight() {
        TextureRegion frame = getCurrentFrame();
        return frame != null ? frame.getRegionHeight() : 0;
    }

    public String getCurrentAnimationKey() {
        return currentAnimationKey;
    }

    public GameEconomy getGameEconomy() {
        return gameEconomy;
    }

}
