package sdd.aisle4android;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Wild on 17/04/2017.
 */

public class FoodNameManager {

    // (name, category) pairs
    private Map<String, String> nameCategoryMap = new HashMap<String, String>();

    FoodNameManager() {
        // read from file -- set everything to lowercase

        nameCategoryMap.put("carrot", "produce");
        nameCategoryMap.put("apple", "produce");
        nameCategoryMap.put("mango", "produce");
        nameCategoryMap.put("potato", "produce");
    }

    String[] getFoodNames() {
        return nameCategoryMap.keySet().toArray(new String[nameCategoryMap.size()]);
    }
    String[] getCategories() {
        return nameCategoryMap.values().toArray(new String[nameCategoryMap.size()]);
    }
    String getCategory(String itemName) {
        return nameCategoryMap.get(itemName.toLowerCase());
    }
}
