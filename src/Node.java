import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String name;
    private final Integer id;
    private final List<Edge> edges = new ArrayList<>();
    private Boolean root = true;

    private final List<BPDU> offers = new ArrayList<>();
    private BPDU accepted;

    public Node(String name, Integer id) {
        this.name = name;
        this.id = id;
        accepted = new BPDU(name, id, 0);
    }
    public void addEdge(Edge e) {
        edges.add(e);
    }
    public List<Edge> getEdges() { return edges; }
    public String getName() { return name; }
    public Boolean isRoot() { return root; }

    public static Node getElementByName(String name, List<Node> list){
        for(Node item : list) {
            try {
                if(item.getName().equals(name)) return item;
            } catch (NullPointerException e) {
                throw new NullPointerException("Element with Name " + name + " not found in list");
            }
        }
        return null;
    }

    public void sendBroadcast(){
        for (Edge e : edges) e.transferRequest(new BPDU(name, accepted.getRootID(), accepted.getTotalCost()));
    }

    public void receiveRequest(BPDU req){
        offers.add(req);
    }

    public void processOffers() {
        for (BPDU offer : offers) {
            if(offer.getRootID() < accepted.getRootID()){
                accepted = offer;
                root = false;
            } else if (offer.getRootID().equals(accepted.getRootID()) && (accepted.getTotalCost() > offer.getTotalCost())){
                if (root) accepted = new BPDU(name, id, 0);
                else accepted = offer;
            }
        }
        offers.clear();
    }
    public void printNextHop() {
        System.out.println("\t" + name + "-" + accepted.getSource());
    }
}