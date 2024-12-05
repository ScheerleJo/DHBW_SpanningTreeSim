import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Input input = new Input();
        input.readInputFile();

//      Creating the SpanningTree
        List<Node> nodes = input.createSpanningTree();

//      Running SpanningTree Sim
        for (int i = 1; i <= nodes.size(); i++) {
            // After running the algorithm as many times as there are nodes, it is very unlikely that the spanning tree will change after one iteration.
            for (Node node : nodes) node.sendBroadcast();
            for (Node node : nodes) node.processOffers();
        }

//      Print Output
        for (Node node : nodes) node.printNextHop();
    }
}