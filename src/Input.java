import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    List<Node> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    public String readInputFile() throws IOException {
        File file = new File("./src/input.txt");
        if (!file.exists()) throw new Error("Input file does not exist");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        Pattern startPattern = Pattern.compile("^Graph\s*([a-zA-Z0-9]+)\s*\\{");
        Pattern nodePattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9]*)=([0-9]+)");
        Pattern edgePattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9]*)-([a-zA-Z]+[a-zA-Z0-9]*):([0-9]+)");
        Matcher matcher = startPattern.matcher(br.readLine());
        if (!matcher.find()) throw new Error("Input file does not contain graph");
        String graphName = matcher.group(1);
        while((line = br.readLine()) != null) {
            line = line.replaceAll(" ", "");
            matcher = nodePattern.matcher(line);
            if(matcher.find()) createNode(matcher.group(1), matcher.group(2));
            matcher = edgePattern.matcher(line);
            if(matcher.find()) createEdge(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        br.close();
        checkOrphans(nodes);
        return graphName;
    }
//  Create The Spanning Tree
    private void createNode(String nodeName, String nodeID) {
        if(Node.getElementByName(nodeName, nodes)!= null) throw new Error("Duplicate node value: " + nodeName);
        nodes.add(new Node(nodeName,Integer.parseInt(nodeID)));
    }
    private void createEdge(String nodeSend, String nodeReceive, String cost) {
        String link = nodeSend + "-" + nodeReceive;
        checkEdges(link, nodeSend, nodeReceive);

        List<Node> parents = new ArrayList<>();
        parents.add(checkForParent(nodeSend, nodes));
        parents.add(checkForParent(nodeReceive, nodes));

        Edge edge = new Edge(parents, link ,Integer.parseInt(cost));
        edges.add(edge);
        for (Node node : parents) node.addEdge(edge);
    }

    public List<Node> getSpanningTree() {
        return nodes;
    }
//  ErrorHandling functionality
    private void checkEdges(String link, String nodeSend, String nodeReceive) {
        if(Edge.getElementByName(link, edges) != null) throw new Error("Duplicate edge value: " + link);
        if(Edge.getElementByName(nodeReceive + "-" + nodeSend, edges) != null) throw new Error("Duplicate edge value: " + nodeReceive + "-" + nodeSend);
        if(nodeSend.equals(nodeReceive)) throw new Error("Edge pointing back to sender found: " + link);
    }
    private void checkOrphans(List<Node> nodes) {
        for (Node node : nodes) {
            if(node.getEdges().isEmpty()) throw new Error("Orphan node found with name: " + node.getName());
        }
    }
    public Node checkForParent(String name, List<Node> nodes) {
        Node node = Node.getElementByName(name, nodes);
        if(node != null) return node;
        throw new Error("Parent node not found: " + name);
    }
}