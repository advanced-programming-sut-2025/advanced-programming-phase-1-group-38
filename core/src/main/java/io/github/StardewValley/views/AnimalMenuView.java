package io.github.StardewValley.views;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.Animal;
import io.github.StardewValley.models.ItemCatalog;
import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.Player;

/**
 * A modal dialog for interacting with an Animal:
 * - Feed (corn for cow, wheat for chicken)
 * - Pet
 * - Collect product
 * - Sell
 *
 * Usage:
 *  AnimalMenuView.show(stage, skin, gameController, player, animal, new AnimalMenuView.Listener() {
 *      @Override public void onSold(Animal a, int price) {
 *          // credit money here
 *      }
 *  });
 */
public final class AnimalMenuView {

    public interface Listener {
        default void onFed(Animal a) {}
        default void onPetted(Animal a) {}
        default void onCollected(Animal a, ItemType product, int amount) {}
        default void onSold(Animal a, int price) {}
        default void onClosed() {}
    }

    private AnimalMenuView() {}

    public static void show(Stage stage, Skin skin,
                            GameController gc,
                            Player player,
                            Animal animal,
                            Listener listener) {

        if (stage == null || skin == null || gc == null || player == null || animal == null) return;

        // make the callback final/effectively final for inner classes
        final Listener cb = (listener != null) ? listener : new Listener() {};

        final Dialog dialog = new Dialog(animal.getSpecies().display + " — " + animal.getName(), skin);

        // UI elements
        final Label nameLbl       = new Label("Name: " + animal.getName(), skin);
        final Label speciesLbl    = new Label("Species: " + animal.getSpecies().display, skin);
        final Label feedLbl       = new Label("Feed: " + animal.getSpecies().feedItemId.toUpperCase(), skin);
        final Label productLbl    = new Label("", skin);
        final Label friendshipLbl = new Label("", skin);
        final Label msgLbl        = new Label("", skin);

        final TextButton feedBtn    = new TextButton("Feed", skin);
        final TextButton petBtn     = new TextButton("Pet", skin);
        final TextButton collectBtn = new TextButton("Collect Product", skin);
        final TextButton sellBtn    = new TextButton("Sell", skin);
        final TextButton closeBtn   = new TextButton("Close", skin);

        // Layout
        Table t = dialog.getContentTable();
        t.defaults().pad(4);
        t.add(nameLbl).left().row();
        t.add(speciesLbl).left().row();
        t.add(feedLbl).left().row();
        t.add(productLbl).left().row();
        t.add(friendshipLbl).left().row();
        t.add(new Separator(skin)).growX().padTop(6).row();

        Table btns = new Table();
        btns.defaults().pad(4);
        btns.add(feedBtn);
        btns.add(petBtn);
        btns.add(collectBtn);
        btns.add(sellBtn);
        t.add(btns).row();

        t.add(msgLbl).left().padTop(6).row();

        dialog.button(closeBtn);

        // Helpers
        Runnable refresh = () -> {
            boolean ready = animal.canCollectProduct();
            productLbl.setText("Product: " + (ready ? "Ready (" + animal.productItemId() + ")" : "Not ready"));
            friendshipLbl.setText("Friendship: (hidden)");
            msgLbl.setText("");

            // Enable/disable buttons based on state/inventory
            ItemType feed = ItemCatalog.get(animal.getSpecies().feedItemId);
            boolean canFeed = (feed != null) && player.getInventory().contains(feed, 1);
            feedBtn.setDisabled(!canFeed);
            collectBtn.setDisabled(!ready);
        };

        Runnable sayNoFeed = () -> {
            msgLbl.setText("You don't have required feed (" + animal.getSpecies().feedItemId + ").");
        };

        // Actions
        feedBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                ItemType feed = ItemCatalog.get(animal.getSpecies().feedItemId);
                if (feed == null || !player.getInventory().contains(feed, 1)) {
                    sayNoFeed.run();
                    return;
                }
                // Spend feed & feed animal
                player.getInventory().remove(feed, 1);
                if (animal.feed(feed)) {
                    msgLbl.setText("Fed successfully.");
                    cb.onFed(animal);
                } else {
                    msgLbl.setText("This animal doesn't eat that.");
                    player.getInventory().add(feed, 1); // optional refund
                }
                refresh.run();
            }
        });

        petBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                animal.pet();
                msgLbl.setText("You petted " + animal.getName() + ".");
                cb.onPetted(animal);
                refresh.run();
            }
        });

        collectBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (!animal.canCollectProduct()) {
                    msgLbl.setText("No product yet.");
                    return;
                }
                ItemType product = ItemCatalog.get(animal.productItemId());
                if (product == null) {
                    msgLbl.setText("Product item not registered: " + animal.productItemId());
                    return;
                }
                player.getInventory().add(product, 1);
                animal.onProductCollected();
                msgLbl.setText("Collected 1× " + product.id().toUpperCase() + ".");
                cb.onCollected(animal, product, 1);
                refresh.run();
            }
        });

        sellBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                int price = animal.sellPrice();

                // Remove from world/controller
                gc.animals().remove(animal);

                // Let the caller handle money credit
                cb.onSold(animal, price);

                msgLbl.setText("Sold for " + price + ".");
                dialog.hide();
            }
        });

        closeBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                cb.onClosed();
                dialog.hide();
            }
        });

        refresh.run();
        dialog.show(stage);
    }

    /** Tiny horizontal line separator */
    private static class Separator extends Table {
        Separator(Skin skin) {
            super(skin);
            add(new Label("", skin)).height(1).growX();
        }
    }
}
