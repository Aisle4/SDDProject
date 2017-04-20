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