package sdd.aisle4android;

import android.provider.ContactsContract;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GeneralUnitTests {

    @Test
    public void databaseTest() throws Exception{
//        private DatabaseManager manager = new DatabaseManager();
//        String AddTest = manager.addItem("UnitTest");
//        String PairTest = manager.addItemToItem(1, 1, 1, 5000);
//
//        System.out.println(AddTest);
//        System.out.println(PairTest);
//
//        assertTrue("INSERT INTO items (name) VALUES ('UnitTest')Values have been inserted successfully.".compareTo(AddTest) == 0);
//        assertTrue("INSERT INTO item_to_item (item1_id, item2_id, steps, travel_time) VALUES (1, 1, 1, 5000)Values have been inserted successfully.".compareTo(PairTest) == 0);
    }

    @Test
    public void itemOrdering_isCorrect() {
//        ShopList list = new ShopList("UTest List", null);
//        List<ItemToItemData> data = new ArrayList<>();
//
//        ShopItem apple = list.addItem(new ShopItem("Apple", null));
//        ShopItem orange = list.addItem(new ShopItem("Orange", null));
//
//        data.add(new ItemToItemData(null, "Orange", 5, 5));
//        data.add(new ItemToItemData("Apple", "Orange", 10, 10));
//
//        // Pre conditions
//        assertTrue(list.getItem(0) == apple && list.getItem(1) == orange);
//
//        // Order
//        ItemOrderer orderer = new ItemOrderer(null, null, data, null);
//        orderer.orderList(list, null);
//
//        // Post conditions
//        assertTrue(list.getItem(0) == orange && list.getItem(1) == apple);
    }

    @Test
    public void itemGraphMinDistCorrect() {
        List<ItemToItemData> testData = new ArrayList<>();
        String apple = "Apple";
        String mango = "Mango";
        String carrot = "Carrot";
        String pepper = "Pepper";
        String milk = "Milk";
        String eggs = "Eggs";
        String butter = "Butter";
        String pasta = "Pasta";
        String rice = "Rice";

        long shortTime = 10000;
        int aFewSteps = (int)(ItemGraph.STEPS_PER_MILLISECOND * shortTime);
        
        testData.add(new ItemToItemData(DataCollector.STORE_ENTRANCE_NAME, apple, shortTime, aFewSteps));
        testData.add(new ItemToItemData(apple, mango, shortTime, aFewSteps));
        testData.add(new ItemToItemData(mango, carrot, shortTime, aFewSteps));
        testData.add(new ItemToItemData(carrot, pepper, shortTime, aFewSteps));
        testData.add(new ItemToItemData(pepper, milk, shortTime*3, aFewSteps*3));
        testData.add(new ItemToItemData(milk, eggs, shortTime, aFewSteps));
        testData.add(new ItemToItemData(eggs, butter, shortTime, aFewSteps));
        testData.add(new ItemToItemData(butter, pasta, shortTime*3, aFewSteps*3));
        testData.add(new ItemToItemData(pasta, rice, shortTime, aFewSteps));

        FoodNameManager foodNameManager = new FoodNameManager();
        ItemGraph graph = new ItemGraph(testData, foodNameManager);
        assertTrue(graph.minDist(apple, DataCollector.STORE_ENTRANCE_NAME) == graph.minDist(DataCollector.STORE_ENTRANCE_NAME, apple));
        assertTrue(graph.minDist(DataCollector.STORE_ENTRANCE_NAME, apple) < graph.minDist(DataCollector.STORE_ENTRANCE_NAME, milk));
        assertTrue(graph.minDist(apple, pepper) < graph.minDist(milk, pasta));
        assertTrue(graph.minDist(carrot, apple) == 0);
    }
}