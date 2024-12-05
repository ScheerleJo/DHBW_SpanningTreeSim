import java.util.ArrayList;
import java.util.List;

public class Edge {
    private final List<Node> nodes;
    private final Integer cost;
    private final String link;

    public Edge(List<Node> nodes, String link, Integer cost) {
        this.link = link;
        this.cost = cost;
        this.nodes = nodes;
    }
    public void transferRequest(BPDU bpdu) {
        List<Node> possibleDestinations = new ArrayList<>(nodes);
        bpdu.addCost(cost);
        possibleDestinations.remove(Node.getElementByName(bpdu.getSource(), nodes));
        possibleDestinations.getFirst().receiveRequest(bpdu);
    }
    public String getLink() {
        return link;
    }
    public static Edge getElementByName(String name, List<Edge> list){
        for(Edge item : list) {
            try {
                if(item.getLink().equals(name)) return item;
            } catch (NullPointerException e) {
                throw new NullPointerException("Element with Name " + name + " not found in list");
            }
        }
        return null;
    }
}
