package views;

import controllers.GamePlayController;
import models.App;
import models.Result;
import models.enums.Commands.GamePlayCommands;
import models.enums.Menu;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GamePlay implements AppMenu {
    private final GamePlayController controller;

    public GamePlay() {
        this.controller = new GamePlayController(App.getCurrentGame());
    }

    @Override
    public AppMenu checkCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) return this;
        String input = scanner.nextLine().trim();
        Matcher m;

        if ((m = GamePlayCommands.WALK.getMatcher(input)) != null) {
            String pos = m.group("position");
            System.out.println(controller.respondForWalkRequest(pos));
            return this;
        } else if ((m = GamePlayCommands.WALK_CONFIRM.getMatcher(input)) != null) {
            String ans = m.group("answer");
            System.out.println(controller.confirmWalk(ans));
            return this;
        } else if ((m = GamePlayCommands.PRINT_MAP.getMatcher(input)) != null) {
            String pos = m.group("position");
            String size = m.group("size");
            Result res = controller.printMap(pos, size);
            System.out.println(res.message());
            return this;
        } else if ((m = GamePlayCommands.CHEAT_ADVANCE_TIME.getMatcher(input)) != null) {
            String hoursArg = m.group("hours");
            Result res = controller.cheatAdvanceHours(hoursArg);
            System.out.println(res.message());
            return this;
        } else if ((m = GamePlayCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
            String daysArg = m.group("days");
            Result res = controller.cheatAdvanceDays(daysArg);
            System.out.println(res.message());
            return this;
        } else if ((m = GamePlayCommands.NEXT_TURN.getMatcher(input)) != null)
            System.out.println(controller.nextTurn().message());

        else if ((m = GamePlayCommands.SHOW_TIME.getMatcher(input)) != null) {
            System.out.println(controller.showTime().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_DATE.getMatcher(input)) != null) {
            System.out.println(controller.showDate().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_DATETIME.getMatcher(input)) != null) {
            System.out.println(controller.showDateTime().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_DAY_OF_WEEK.getMatcher(input)) != null) {
            System.out.println(controller.showDayOfWeek().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_SEASON.getMatcher(input)) != null) {
            System.out.println(controller.showSeason().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_WEATHER.getMatcher(input)) != null) {
            System.out.println(controller.showWeather().message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_WEATHER_FORECAST.getMatcher(input)) != null) {
            System.out.println(controller.showWeatherForecast().message());
            return this;
        } else if ((m = GamePlayCommands.TOOLS_EQUIP.getMatcher(input)) != null) {
            System.out.println(controller.equipTool(m.group("tool_name")).message());
            return this;
        } else if ((m = GamePlayCommands.TOOLS_SHOW_CURRENT.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentTool().message());
            return this;
        } else if ((m = GamePlayCommands.TOOLS_SHOW_AVAILABLE.getMatcher(input)) != null) {
            System.out.println(controller.showAvailableTool().message());
            return this;
        } else if ((m = GamePlayCommands.TOOLS_UPGRADE.getMatcher(input)) != null) {
            System.out.println(controller.toolUpgrade(m.group("tools_name")).message());
            return this;
        } else if ((m = GamePlayCommands.TOOLS_USE.getMatcher(input)) != null) {
            System.out.println(controller.useTool(m.group("direction")).message());
            return this;
        } else if ((m = GamePlayCommands.PLANT.getMatcher(input)) != null) {
            System.out.println(controller.plant(m.group("seed"), m.group("direction")).message());
            return this;
        }

//        else if ((m = GamePlayCommands.FERTILIZE.getMatcher(input)) != null) {
//            System.out.println(controller.fertilize(m.group("fertilizer"), m.group("direction")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.SHOW_PLANT.getMatcher(input)) != null) {
            String plantName = m.group("x") + "," + m.group("y");
            System.out.println(controller.showPlant(plantName).message());
            return this;
        } else if ((m = GamePlayCommands.HOWMUCH_WATER.getMatcher(input)) != null) {
            System.out.println(controller.howMuchWater().message());
            return this;
        } else if ((m = GamePlayCommands.INVENTORY_SHOW.getMatcher(input)) != null) {
            System.out.println(controller.inventoryShow().message());
            return this;
        } else if ((m = GamePlayCommands.THROW_ITEM_TO_TRASH.getMatcher(input)) != null) {
            System.out.println(controller.throwItemToTrash(m.group("itemName"), m.group("number")).message());
            return this;
        } else if ((m = GamePlayCommands.CRAFT_INFO.getMatcher(input)) != null) {
            System.out.println(controller.showCraftInfo(m.group("craftName")).message());
            return this;
        } else if ((m = GamePlayCommands.CRAFTING_SHOW_RECIPES.getMatcher(input)) != null) {
            System.out.println(controller.craftingShowRecipes().message());
            return this;
        }

//        else if ((m = GamePlayCommands.CRAFTING_CRAFT.getMatcher(input)) != null) {
//            System.out.println(controller.craft(m.group("itemName")).message());
//            return this;
//        }

//        else if ((m = GamePlayCommands.COOKING_REFRIGERATOR.getMatcher(input)) != null) {
//            System.out.println(controller.cookingRefrigerator(m.group("putOrPick"), m.group("item")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.COOKING_SHOW_RECIPES.getMatcher(input)) != null) {
            System.out.println(controller.cookingShowRecipes().message());
            return this;
        }

//        else if ((m = GamePlayCommands.COOKING_PREPARE.getMatcher(input)) != null) {
//            System.out.println(controller.cookingPrepare(m.group("recipeName")).message());
//            return this;
//        }
//
//        else if ((m = GamePlayCommands.EAT.getMatcher(input)) != null) {
//            System.out.println(controller.eat(m.group("foodName")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddItem(m.group("itemName"), m.group("count")).message());
            return this;
        } else if ((m = GamePlayCommands.CHEAT_ADD_DOLLARS.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddDollars(m.group("count")).message());
            return this;
        }

//        else if ((m = GamePlayCommands.FRIENDSHIPS.getMatcher(input)) != null) {
//            System.out.println(controller.showFriendships().message());
//            return this;
//        }

        else if ((m = GamePlayCommands.TALK.getMatcher(input)) != null) {
            System.out.println(controller.talk(m.group("username"), m.group("message")).message());
            return this;
        }

//        else if ((m = GamePlayCommands.TALK_HISTORY.getMatcher(input)) != null) {
//            System.out.println(controller.(m.group("username")).message());
//            return this;
//        }
//
//        else if ((m = GamePlayCommands.GIFT.getMatcher(input)) != null) {
//            System.out.println(controller.giveGift(m.group("username"), m.group("item"), m.group("amount")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.GIFT_LIST.getMatcher(input)) != null) {
            System.out.println(controller.giftList().message());
            return this;
        }

//        else if ((m = GamePlayCommands.GIFT_RATE.getMatcher(input)) != null) {
//            System.out.println(controller.giftRate(m.group("giftNumber"), m.group("rate")).message());
//            return this;
//        }

//        else if ((m = GamePlayCommands.GIFT_HISTORY.getMatcher(input)) != null) {
//            System.out.println(controller.showGiftHistory(m.group("username")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.HUG.getMatcher(input)) != null) {
            System.out.println(controller.hug(m.group("username")).message());
            return this;
        } else if ((m = GamePlayCommands.FLOWER.getMatcher(input)) != null) {
            System.out.println(controller.flower(m.group("username")).message());
            return this;
        }

//        else if ((m = GamePlayCommands.ASK_MARRIAGE.getMatcher(input)) != null) {
//            System.out.println(controller.askMarriage(m.group("username"), m.group("ring")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.RESPONSE_MARRIAGE.getMatcher(input)) != null) {
            return this;
        } else if ((m = GamePlayCommands.ANIMALS.getMatcher(input)) != null) {
            System.out.println(controller.showMyAnimalsInfo().message());
            return this;
        } else if ((m = GamePlayCommands.PET.getMatcher(input)) != null) {
            System.out.println(controller.pet(m.group("name")).message());
            return this;
        } else if ((m = GamePlayCommands.SHEPHERD_ANIMALS.getMatcher(input)) != null) {
            String animal = m.group("animalName");
            String x = m.group("x");
            String y = m.group("y");
            String position = x + "," + y;

            System.out.println(controller.shepherdAnimal(animal, position).message());
            return this;
        } else if ((m = GamePlayCommands.FEED_HAY.getMatcher(input)) != null) {
            System.out.println(controller.feedHayToAnimal(m.group("animalName")).message());
            return this;
        } else if ((m = GamePlayCommands.PRODUCES.getMatcher(input)) != null) {
            System.out.println(controller.showProducedProducts().message());
            return this;
        } else if ((m = GamePlayCommands.COLLECT_PRODUCE.getMatcher(input)) != null) {
            System.out.println(controller.collectProducts(m.group("name")).message());
            return this;
        } else if ((m = GamePlayCommands.SELL_ANIMAL.getMatcher(input)) != null) {
            System.out.println(controller.sellAnimal(m.group("name")).message());
            return this;
        } else if ((m = GamePlayCommands.SHOW_ALL_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(controller.showAllProducts());
            return this;
        } else if ((m = GamePlayCommands.SHOW_ALL_AVAILABLE_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(controller.showAvailableProducts());
            return this;
        }

//        else if ((m = GamePlayCommands.PURCHASE.getMatcher(input)) != null) {
//            System.out.println(controller.purchase(m.group("productName"), m.group("count")).message());
//            return this;
//        }

//        else if ((m = GamePlayCommands.SELL.getMatcher(input)) != null) {
//            System.out.println(controller.sell(m.group("productName"), m.group("count")).message());
//            return this;
//        }

        else if ((m = GamePlayCommands.ARTISAN_USE.getMatcher(input)) != null) {
            String machine = m.group("name");
            String item = m.group("item");
            Result res = controller.tryStartArtisan(machine, item);
            System.out.println(res.message());
            return this;
        } else if ((m = GamePlayCommands.ARTISAN_GET.getMatcher(input)) != null) {
            String machine = m.group("name");
            Result res = controller.tryCollectArtisan(machine);
            System.out.println(res.message());
            return this;
        }
        return this;
    }
}
