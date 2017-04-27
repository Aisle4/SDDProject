package sdd.aisle4android.Model;

import java.util.HashMap;
import java.util.Map;
/* import statements for reading from file
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;*/

/**
 * Created by Robert Wild on 17/04/2017.
 */

public class FoodNameManager {

    // (name, category) pairs
    private Map<String, String> nameCategoryMap = new HashMap<String, String>();

    public FoodNameManager() {
        String data = getFoodCategoryData();

        //split the string by lines
        String[] nameCategoryArray = data.split("\n");
        String[] foodNameParse;
        for (int i = 0; i < nameCategoryArray.length; ++i ){
            //read in each line and put in map (format name,category)
            foodNameParse = nameCategoryArray[i].split(",");
            nameCategoryMap.put(foodNameParse[0].toLowerCase(), foodNameParse[1].toLowerCase());
        }
    }

    /**
     * @return array of all item names
     */
    public String[] getFoodNames() {
        return nameCategoryMap.keySet().toArray(new String[nameCategoryMap.size()]);
    }

    /**
     * @return array of all item categories
     */
    public String[] getCategories() {
        return nameCategoryMap.values().toArray(new String[nameCategoryMap.size()]);
    }

    /**
     * pass an item name and get it's corresponding category
     * @param itemName
     * @return item category
     */
    public String getCategory(String itemName) {
        return nameCategoryMap.get(itemName.toLowerCase());
    }

    private String getFoodCategoryData() {
        // TODO: read this from a csv file
        return "brownie mix,Baking Goods\n" +
                "flour,Baking Goods\n" +
                "sugar,Baking Goods\n" +
                "coffee,Beverage\n" +
                "juice,Beverage\n" +
                "soda,Beverage\n" +
                "tea,Beverage\n" +
                "spaghetti sauce,Bottled Food\n" +
                "bagels,Bread\n" +
                "dinner rolls,Bread\n" +
                "sandwich loaves,Bread\n" +
                "tortillas,Bread\n" +
                "Reese's Peanut Butter Cups,Candy\n" +
                "canned vegetables,Canned Food\n" +
                "Bergenost,Cheese\n" +
                "Brick cheese,Cheese\n" +
                "Cheddar Cheese,Cheese\n" +
                "Cheese curds,Cheese\n" +
                "Colby cheese,Cheese\n" +
                "Colby-Jack cheese,Cheese\n" +
                "Colorado Blackie,Cheese\n" +
                "Cream cheese,Cheese\n" +
                "Creole cream cheese,Cheese\n" +
                "Cup Cheese,Cheese\n" +
                "Farmer cheese,Cheese\n" +
                "Hoop cheese,Cheese\n" +
                "Liederkranz cheese,Cheese\n" +
                "Limburger cheese,Cheese\n" +
                "Monterey Jack cheese,Cheese\n" +
                "Muenster cheese,Cheese\n" +
                "Nacho cheese,Cheese\n" +
                "Pepper jack cheese,Cheese\n" +
                "Pinconning cheese,Cheese\n" +
                "Provel cheese,Cheese\n" +
                "Red Hawk,Cheese\n" +
                "String cheese,Cheese\n" +
                "Swiss cheese,Cheese\n" +
                "Teleme cheese,Cheese\n" +
                "all-purpose,Cleaners\n" +
                "dishwashing detergent,Cleaners\n" +
                "dishwashing liquid,Cleaners\n" +
                "handsoap,Cleaners\n" +
                "laundry detergent,Cleaners\n" +
                "soap,Cleaners\n" +
                "ketchup,Condiments\n" +
                "mayonnaise,Condiments\n" +
                "mustard,Condiments\n" +
                "Nutella,Condiments\n" +
                "Peanut Butter,Condiments\n" +
                "aluminum foil,containment\n" +
                "napkins,containment\n" +
                "paper bags,containment\n" +
                "paper towels,containment\n" +
                "parchment paper,containment\n" +
                "sandwich bags,containment\n" +
                "toilet paper,containment\n" +
                "wax paper,containment\n" +
                "Butter Cookies,Cookies\n" +
                "Chocolate Chip Cookies,Cookies\n" +
                "Double Chocolate Cookies,Cookies\n" +
                "Sugar Cookies,Cookies\n" +
                "Yogurt,Dairy\n" +
                "Milk, Dairy\n"+
                "Eggs, Dairy\n"+
                "Butter, Dairy\n"+
                "cereal,Dry Foods\n" +
                "pasta,Dry Foods\n" +
                "Brown Eggs,Eggs\n" +
                "Flamingo Eggs,Eggs\n" +
                "Grade A Eggs,Eggs\n" +
                "White Eggs,Eggs\n" +
                "Cod,Fish\n" +
                "Flounder,Fish\n" +
                "Salmon,Fish\n" +
                "Swordfish,Fish\n" +
                "Tuna,Fish\n" +
                "frozen dinners,Frozen Food\n" +
                "frozen vegetables,Frozen Food\n" +
                "frozen waffles,Frozen Food\n" +
                "ice cream,Frozen Food\n" +
                "bacon,Meat\n" +
                "beef,Meat\n" +
                "chicken,Meat\n" +
                "ground beef,Meat\n" +
                "lamb,Meat\n" +
                "pork,Meat\n" +
                "poultry,Meat\n" +
                "steak,Meat\n" +
                "turkey,Meat\n" +
                "Chocolate Milk,Milk\n" +
                "Strawberry Milk,Milk\n" +
                "Apple,Produce\n" +
                "Apricots,Produce\n" +
                "Artichokes,Produce\n" +
                "Arugula,Produce\n" +
                "Asparagus,Produce\n" +
                "Avocados,Produce\n" +
                "Bananas,Produce\n" +
                "Beets,Produce\n" +
                "Black Currants,Produce\n" +
                "Black Olives,Produce\n" +
                "Black Salsify,Produce\n" +
                "Blackberries,Produce\n" +
                "Blood Oranges,Produce\n" +
                "Blueberries,Produce\n" +
                "Broccoflower,Produce\n" +
                "Broccoli,Produce\n" +
                "Broccoli Rabe,Produce\n" +
                "Brown Pears,Produce\n" +
                "Brussels Sprouts,Produce\n" +
                "Butternut Squash,Produce\n" +
                "Cantaloupe,Produce\n" +
                "Cape Gooseberries,Produce\n" +
                "Carrot,Produce\n" +
                "Carrots,Produce\n" +
                "Cauliflower,Produce\n" +
                "Celery,Produce\n" +
                "Chayote Squash,Produce\n" +
                "Cherries,Produce\n" +
                "Chinese Cabbage,Produce\n" +
                "Concord Grapes,Produce\n" +
                "Cranberries,Produce\n" +
                "Cucumbers,Produce\n" +
                "Dates,Produce\n" +
                "Dried Plums,Produce\n" +
                "Eggplant,Produce\n" +
                "Elderberries,Produce\n" +
                "Endive,Produce\n" +
                "Garlic,Produce\n" +
                "Ginger,Produce\n" +
                "Golden Kiwifruit,Produce\n" +
                "Grape Juice (100%),Produce\n" +
                "Grapefruit,Produce\n" +
                "Green Apples,Produce\n" +
                "Green Beans,Produce\n" +
                "Green Cabbage,Produce\n" +
                "Green Grapes,Produce\n" +
                "Green Onions,Produce\n" +
                "Green Peas,Produce\n" +
                "Green Peppers,Produce\n" +
                "Honeydew,Produce\n" +
                "Jerusalem Artichokes,Produce\n" +
                "Jicama,Produce\n" +
                "Kiwifruit,Produce\n" +
                "Kohlrabi,Produce\n" +
                "Leafy Greens,Produce\n" +
                "Leeks,Produce\n" +
                "Lemons,Produce\n" +
                "Lettuce,Produce\n" +
                "Limes,Produce\n" +
                "Mango,Produce\n" +
                "Mangoes,Produce\n" +
                "Mushrooms,Produce\n" +
                "Nectarines,Produce\n" +
                "Okra,Produce\n" +
                "Onions,Produce\n" +
                "Oranges,Produce\n" +
                "Papayas,Produce\n" +
                "Parsnips,Produce\n" +
                "Peaches,Produce\n" +
                "Peas,Produce\n" +
                "Persimmons,Produce\n" +
                "Pineapples,Produce\n" +
                "Pink/Red Grapefruit,Produce\n" +
                "Plums,Produce\n" +
                "Pomegranates,Produce\n" +
                "Potato,Produce\n" +
                "Potatoes (purple fleshed),Produce\n" +
                "Potatoes (White Fleshed),Produce\n" +
                "Pumpkin,Produce\n" +
                "Purple Asparagus,Produce\n" +
                "Purple Belgian Endive,Produce\n" +
                "Purple Cabbage,Produce\n" +
                "Purple Carrots,Produce\n" +
                "Purple Figs,Produce\n" +
                "Purple Grapes,Produce\n" +
                "Purple Peppers,Produce\n" +
                "Radicchio,Produce\n" +
                "Radishes,Produce\n" +
                "Raspberries,Produce\n" +
                "Red Apples,Produce\n" +
                "Red Grapes,Produce\n" +
                "Red Onions,Produce\n" +
                "Red Pears,Produce\n" +
                "Red Peppers,Produce\n" +
                "Red Potatoes,Produce\n" +
                "Rhubarb,Produce\n" +
                "Rutabagas,Produce\n" +
                "Shallots,Produce\n" +
                "Snow Peas,Produce\n" +
                "Spinach,Produce\n" +
                "Strawberries,Produce\n" +
                "Sugar Snap Peas,Produce\n" +
                "Sweet Corn,Produce\n" +
                "Sweet Potatoes,Produce\n" +
                "Tangerines,Produce\n" +
                "Tomatoes,Produce\n" +
                "Turnips,Produce\n" +
                "Watercress,Produce\n" +
                "Watermelon,Produce\n" +
                "White Corn,Produce\n" +
                "White Nectarines,Produce\n" +
                "White Peaches,Produce\n" +
                "Yellow Apples,Produce\n" +
                "Yellow Beets,Produce\n" +
                "Yellow Figs,Produce\n" +
                "Yellow Pears,Produce\n" +
                "Yellow Peppers,Produce\n" +
                "Yellow Potatoes,Produce\n" +
                "Yellow Summer Squash,Produce\n" +
                "Yellow Tomatoes,Produce\n" +
                "Yellow Watermelon,Produce\n" +
                "Yellow Winter Squash,Produce\n" +
                "Zucchini,Produce\n" +
                "Bay Scallops,Seafood\n" +
                "Crab,Seafood\n" +
                "Lobster,Seafood\n" +
                "Scallops,Seafood\n" +
                "Sea Scallops,Seafood\n" +
                "Shrimp,Seafood\n" +
                "paprika,Spices\n" +
                "pepper,Spices\n" +
                "salt,Spices\n";
    }
}
