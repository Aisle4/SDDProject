package sdd.aisle4android;

import org.junit.Test;

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
}