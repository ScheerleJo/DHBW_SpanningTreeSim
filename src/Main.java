import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

//      Read the given Network from input.txt
        Input input = new Input();
        String graphName = input.readInputFile();

//      Creating the SpanningTree
        List<Node> nodes = input.createSpanningTree();

//      Running SpanningTree Sim
        for (int i = 1; i <= nodes.size(); i++) {
            // After running the algorithm as many times as there are nodes, it is very unlikely that the spanning tree will change after one iteration.
            for (Node node : nodes) node.sendBroadcast();
            for (Node node : nodes) node.processOffers();
        }

//      Print Output
        nodes.sort(Comparator.comparing(node -> node.getName()));
        System.out.println("Spanning-Tree of " + graphName + " {");
        for (Node node : nodes) {
            if(node.isRoot()) {
                System.out.println("\tRoot: " + node.getName());
                nodes.remove(node);
                break;
            }
        }
        for (Node node : nodes) node.printNextHop();
        System.out.println("}");
    }
}