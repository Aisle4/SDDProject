package sdd.aisle4android;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Wild on 17/04/2017.
 */

public class FoodNameManager {

    private Map<String, String> nameCategoryMap = new HashMap<String, String>();

    FoodNameManager() {
        // read from file
    }

    public String[] getFoodNames() {
//        return nameCategoryMap.keySet();
        return null;
    }
    public String[] getCategories() {
//        return nameCategoryMap.keySet().toArray();
        return null;
    }
    public String getCategory(String itemName) {
        return null;
    }
}
