package sdd.aisle4android;

import java.util.ArrayList;
import java.util.Collection;
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

    public ItemOrderer(Shopper shopper, DatabaseManager dbManager, List<ItemToItemData> data,
                       FoodNameManager foodNameMgr) {
        graph = new ItemGraph(data, foodNameMgr);

        shopper.eventStartShopping.attach(this);
        shopper.eventStopShopping.attach(this);
        shopper.eventLocationUpdated.attach(this);
    }

    public void onStartShopping(Shopper shopper) {
        // TODO: remove? order list when location updated which for now always happens on start shopping...
//        orderList(shopper.getActiveList(), shopper.getNearestItem());
    }
    public void onStopShopping(Shopper shopper) {
    }
    public void onLocationUpdated(Shopper shopper, ShopItem nearest) {
        orderList(shopper.getActiveList(), nearest);
    }

    // TODO: make private - how to unit test? public, move event handling / ordering commencing code to OrdererController?
    void orderList(ShopList list, ShopItem nearestItem) {
        // TODO: use custom sort function instead of comparator to avoid redundant dijkstra's calculations
        Collections.sort(list.getItems(), new ItemComparator(nearestItem));
        list.notifyReordered();
    }

    private class ItemComparator implements Comparator<ShopItem> {
        private ShopItem referenceItem;

        ItemComparator(ShopItem referenceItem) {
            this.referenceItem = referenceItem;
        }

        @Override
        public int compare(ShopItem item1, ShopItem item2) {

            // Collected items at bottom of list (always greater)
            if (item1.isCollected()) {
                return item2.isCollected() ? 0 : 1;
            }
            else if (item2.isCollected()) {
                return -1;
            }

            // Compare proximity to referenceItem
            return java.lang.Long.compare(
                    graph.minDist(item1, referenceItem),
                    graph.minDist(item2, referenceItem));
        }
    }
}


class ItemGraph {
    private FoodNameManager foodNameManager;
    private HashMap<String, Node> nodes;
    private Node storeEntrance;

    // Weights for computing edge distances based on time and step data
    static final float STEPS_PER_MILLISECOND = 0.002f; // 120 steps/min
    private static final float DISTANCE_PER_STEP = 0.7f; // 0.7m per step for someone 5"8'
    private static final float DISTANCE_PER_MILLISECOND = STEPS_PER_MILLISECOND * DISTANCE_PER_STEP;

    // Edge weight for item-category edges (paths that rely only on recorded data should be preferred)
    private static final float DISTANCE_CATEGORY_NODE = 10 * DISTANCE_PER_STEP;


    ItemGraph(List<ItemToItemData> data, FoodNameManager foodNameMgr) {
        this.foodNameManager = foodNameMgr;
        nodes = new HashMap<>();
        storeEntrance = new Node("");
        nodes.put(storeEntrance.itemName, storeEntrance);

        populateFromData(data);
        if (foodNameMgr != null)
            extendWithCategoryData(foodNameMgr);
    }

    /**
     * Store entrance should be denoted by null
     * @param item1
     * @param item2
     * @return
     */
    long minDist(ShopItem item1, ShopItem item2) {
        return minDist(item1 == null ? "" : item1.getName(),
                       item2 == null ? "" : item2.getName());
    }

    /**
     * Store entrance should be denoted with an empty item name ("").
     * @param item1Name
     * @param item2Name
     * @return
     */
    long minDist(String item1Name, String item2Name) {
        // Resolve item name and data
        Node item1Node = nodes.get(item1Name.toLowerCase());
        Node item2Node = nodes.get(item2Name.toLowerCase());

        // Use general food category as starting node in graph if no data for a specific item
        if (item1Node == null) {
            String category = foodNameManager.getCategory(item1Name.toLowerCase());
            item1Node = nodes.get(category.toLowerCase());
            if (item1Node == null) return Long.MAX_VALUE; // No category data or category not in graph
        }
        if (item2Node == null) {
            String category = foodNameManager.getCategory(item2Name.toLowerCase());
            item2Node = nodes.get(category.toLowerCase());
            if (item2Node == null) return Long.MAX_VALUE; // No category data or category not in graph
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
                    }
                }
            }
        }

        return item2Node.tmpDist;
    }

    /**
     * Add nodes to graph corresponding to items found in ItemToItemData instances
     * Requires: nodes has been instantiated
     * @param data
     */
    private void populateFromData(List<ItemToItemData> data) {
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
            ew.timeData.add(dat.timeMs);
            ew.stepsData.add(dat.steps);
        }

        // Calculate edge weights (item distances)
        for (Node node : nodes.values()) {
            for (EdgeWeight ew : node.edges.values()) {
                // Average item to item time
                ew.dist = 0;
                for (long ms : ew.timeData) {
                    ew.dist += ms * DISTANCE_PER_MILLISECOND;
                }
                for (Integer steps : ew.stepsData) {
                    ew.dist += steps * DISTANCE_PER_STEP;
                }
                ew.dist /= (ew.timeData.size() + ew.stepsData.size());
            }
        }
    }

    /**
     * Add nodes for general food categories with edges to specific food items already in the graph
     * @param foodNameMgr
     */
    private void extendWithCategoryData(FoodNameManager foodNameMgr) {
        List<Node> itemNodes = new ArrayList<>(nodes.values());

        for (Node node : itemNodes) {
            String category = foodNameMgr.getCategory(node.itemName);
            if (category != null) {
                Node categoryNode = nodes.get(category);

                // Create category node if not already created
                if (categoryNode == null) {
                    categoryNode = new Node(category);
                    nodes.put(category, categoryNode);
                }

                // Create Edge if not already created
                EdgeWeight ew = categoryNode.edges.get(node);
                if (ew == null) {
                    ew = new EdgeWeight();
                    ew.dist = (long)DISTANCE_CATEGORY_NODE;
                    categoryNode.edges.put(node, ew);
                    node.edges.put(categoryNode, ew);
                }
            }
        }
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
        List<Integer> stepsData = new ArrayList<>();
    }

    /** Sorts based on tmpDist */
    private class ComparatorTmpDist implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Long.compare(n1.tmpDist, n2.tmpDist);
        }
    }
}