package sdd.aisle4android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert Wild on 05/04/2017.
 */

class ItemOrderer implements Shopper.IEarStartShopping, Shopper.IEarStopShopping,
        Shopper.IEarLocationUpdated {

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

    // TODO: make private - how to unit test? public, move event handling / ordering commencing code to OrdererController?
    void orderList(ShopList list, ShopItem nearestItem) {
        // TODO: use custom sort function instead of comparator to avoid redundant dijkstra's calculations
        Collections.sort(list.getItems(), new ComparatorItemDist(nearestItem));
        list.notifyReordered();
    }

    private class ComparatorItemDist implements Comparator<ShopItem> {
        private ShopItem referenceItem;

        ComparatorItemDist(ShopItem referenceItem) {
            this.referenceItem = referenceItem;
        }

        @Override
        public int compare(ShopItem item1, ShopItem item2) {
            return java.lang.Long.compare(
                    graph.minDist(item1, referenceItem),
                    graph.minDist(item2, referenceItem));
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
            String name1 = dat.item1Name == null ? null : dat.item1Name.toLowerCase();
            String name2 = dat.item2Name == null ? null : dat.item2Name.toLowerCase();
            Node item1 = nodes.get(name1);
            Node item2 = nodes.get(name2);

            if (item1 == null) {
                item1 = new Node(name1);
                nodes.put(name1, item1);
            }
            if (item2 == null) {
                item2 = new Node(name2);
                nodes.put(name2, item2);
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
        for (Node node : nodes.values()) {
            for (EdgeWeight ew : node.edges.values()) {
                // Average item to item time
                ew.dist = 0;
                for (long time : ew.timeData) {
                    ew.dist += time;
                }
                ew.dist /= ew.timeData.size();
            }
        }
    }
    long minDist(ShopItem item1, ShopItem item2) {
        // Resolve item name and data
        Node item1Node = nodes.get(item1 == null ? null : item1.getName().toLowerCase());
        Node item2Node = nodes.get(item2 == null ? null : item2.getName().toLowerCase());
        if (item1Node == null || item2Node == null) {
            // No relevent data
            return Long.MAX_VALUE;
        }

        // Dijkstra Initialization
//        SortedMultiset<Node> q = new TreeMultiset<>(new ComparatorTmpDist());
        List<Node> q = new ArrayList<>();
        for (Node node : nodes.values()) {
            node.tmpDist = Long.MAX_VALUE;
            node.tmpVisited = false;
            q.add(node);
        }
        item1Node.tmpDist = 0;
        Collections.sort(q, new ComparatorTmpDist());
//        boolean ret = q.remove(item1Node);
//        q.add(item1Node);

        // Main
        while (q.size() > 0) {
            Node node = q.get(0);
            if (node == item2Node) break; // Done
            node.tmpVisited = true;
            Iterator<Node> itr = q.iterator();
            itr.next();
            itr.remove();

            for (Map.Entry<Node, EdgeWeight> entry : node.edges.entrySet()) {
                Node neighbor = entry.getKey();
                long edgeDist = entry.getValue().dist;
                if (!neighbor.tmpVisited) {
                    Long altDist = node.tmpDist + edgeDist;
                    if (altDist < neighbor.tmpDist) {
                        neighbor.tmpDist = altDist;
                        Collections.sort(q, new ComparatorTmpDist());
//                        q.remove(neighbor);
//                        q.add(neighbor);
                    }
                }
            }
        }

        return item2Node.tmpDist;
    }

    private class Node {
        String itemName;
        HashMap<Node, EdgeWeight> edges;
        long tmpDist;
//        Node tmpPrev;
        boolean tmpVisited = false;

        Node(String itemName) {
            this.itemName = itemName;
            edges = new HashMap<>();
        }
    }
    private class EdgeWeight {
        long dist = 0;
        List<Long> timeData = new ArrayList<>();
    }
    /** Sorts based on tmpDist */
    private class ComparatorTmpDist implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Long.compare(n1.tmpDist, n2.tmpDist);
        }
    }
}