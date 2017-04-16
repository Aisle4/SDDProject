package sdd.aisle4android;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Robert Wild on 05/04/2017.
 */

class ItemOrderer implements Shopper.IEventStartShopListener, Shopper.IEventStopShopListener,
        Shopper.IEventLocationUpdatedListener {

    private ItemGraph graph;

    public ItemOrderer(Shopper shopper, DatabaseManager dbManager, List<ItemToItemData> data) {
        graph = new ItemGraph(data);

        shopper.eventStartShopping.attach(this);
        shopper.eventStopShopping.attach(this);
        shopper.eventLocationUpdated.attach(this);
    }

    public void onStartShopping(Shopper shopper) {
        orderList(shopper.getActiveList(), shopper.getNearestItem());
    }
    public void onStopShopping(Shopper shopper) {
    }
    public void onLocationUpdated(Shopper shopper, ShopItem nearest) {
        orderList(shopper.getActiveList(), nearest);
    }

    // TODO: make private - how to unit test? public, move event -> order code to OrdererController?
    void orderList(ShopList list, ShopItem nearestItem) {
        // TODO: order list via function rather than total access to list from getItems()?
        Collections.sort(list.getItems(), new ComparatorItemDist(nearestItem));
    }

    private class ComparatorItemDist implements Comparator<ShopItem> {
        private ShopItem referenceItem;

        ComparatorItemDist(ShopItem referenceItem) {
            this.referenceItem = referenceItem;
        }

        @Override
        public int compare(ShopItem item1, ShopItem item2) {
            return java.lang.Long.compare(
                    graph.minDist(item1.getName(), referenceItem.getName()),
                    graph.minDist(item2.getName(), referenceItem.getName()));
        }
    }
}


class ItemGraph {
    HashMap<String, Node> nodes;
    Node storeEntrance;

    ItemGraph(List<ItemToItemData> data) {
        nodes = new HashMap<>();
        storeEntrance = new Node(null);
        nodes.put(storeEntrance.itemName, storeEntrance);

        // Construct nodes and edges
        for (ItemToItemData dat : data) {
            // Nodes
            Node item1 = nodes.get(dat.item1Name);
            Node item2 = nodes.get(dat.item2Name);

            if (item1 == null) {
                item1 = new Node(dat.item1Name);
                nodes.put(dat.item1Name, item1);
            }
            if (item2 == null) {
                item2 = new Node(dat.item2Name);
                nodes.put(dat.item2Name, item2);
            }

            // Edge
            EdgeWeight ew = item1.edges.get(item2);
            if (ew == null) {
                ew = new EdgeWeight();
                item1.edges.put(item2, ew);
                item2.edges.put(item1, ew);
            }
            ew.timeData.add(dat.time);
        }

        // Calculate edge weights (item distances)
        for (Map.Entry<String, Node> nodeEntry : nodes.entrySet()) {
            Node node = nodeEntry.getValue();
            for (Map.Entry<Node, EdgeWeight> edgeEntry : node.edges.entrySet()) {
                EdgeWeight ew = edgeEntry.getValue();

                // Average item to item time
                ew.dist = 0;
                for (long time : ew.timeData) {
                    ew.dist += time;
                }
                ew.dist /= ew.timeData.size();
            }
        }
    }
    long minDist(String item1Name, String item2Name) {
        // TODO: dijkstra
        return 0;
    }

    class Node {
        String itemName;
        HashMap<Node, EdgeWeight> edges;

        Node(String itemName) {
            this.itemName = itemName;
            edges = new HashMap<>();
        }
    }
    class EdgeWeight {
        long dist = 0;
        List<Long> timeData = new ArrayList<>();
    }
}