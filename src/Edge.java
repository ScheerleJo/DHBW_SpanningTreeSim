import java.util.ArrayList;
import java.util.List;

public class Edge {
    private final List<Node> nodes;
    private final Integer cost;

    public Edge(List<Node> nodes, Integer cost) {
        this.cost = cost;
        this.nodes = nodes;
    }
    public void transferRequest(BPDU bpdu) {
        List<Node> possibleDestinations = new ArrayList<>(nodes);
        bpdu.addCost(cost);
        possibleDestinations.remove(Node.getElementByName(bpdu.getSource(), nodes));
        possibleDestinations.getFirst().receiveRequest(bpdu);
    }
}
