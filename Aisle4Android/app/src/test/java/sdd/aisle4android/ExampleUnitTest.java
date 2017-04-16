package sdd.aisle4android;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private DatabaseManager manager = new DatabaseManager();
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void databaseTest() throws Exception{
        String AddTest = manager.addItem("UnitTest");
        String PairTest = manager.addItemToItem(1, 1, 1, 5000);

        System.out.println(AddTest);
        System.out.println(PairTest);

        assertTrue("INSERT INTO items (name) VALUES ('UnitTest')Values have been inserted successfully.".compareTo(AddTest) == 0);
        assertTrue("INSERT INTO item_to_item (item1_id, item2_id, steps, travel_time) VALUES (1, 1, 1, 5000)Values have been inserted successfully.".compareTo(PairTest) == 0);
    }

    @Test
    public void itemOrdering_isCorrect() {
        ShopList list = new ShopList("UTest List");
        List<ItemToItemData> data = new ArrayList<>();

        ShopItem apple = list.addItem(new ShopItem("Apple"));
        ShopItem orange = list.addItem(new ShopItem("Orange"));

        data.add(new ItemToItemData(null, orange, 5, 5));
        data.add(new ItemToItemData(apple, orange, 10, 10));

        // Pre conditions
        assertTrue(list.getItem(0) == apple && list.getItem(1) == orange);

        // Order
        ItemOrderer orderer = new ItemOrderer(null, data);
        orderer.orderList(list);

        // Post conditions
        assertTrue(list.getItem(0) == orange && list.getItem(1) == apple);
    }
}