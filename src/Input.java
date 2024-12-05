import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Input {
    private final Map<String, Integer> nodeValues = new HashMap<>();
    private final Map<String, Integer> edgeValues = new HashMap<>();

    public void readInputFile() throws IOException {
        File file = new File("./src/input.txt");
        if (!file.exists()) throw new Error("Input file does not exist");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        boolean nodeReader = false, edgeReader = false;
        while((line = br.readLine()) != null) {
            if(line.contains("// Node-IDs")) {
                edgeReader = false;
                nodeReader = true;
                continue;
            } else if(line.contains("// Links und zugeh. Kosten")) {
                edgeReader = true;
                nodeReader = false;
                continue;
            } else if(line.contains("{") || line.contains("}")){
                edgeReader = false;
                nodeReader = false;
                continue;
            }

            if(nodeReader) {
                String[] tokens = line.replaceAll(" ", "").split("=");
                if(nodeValues.containsKey(tokens[0])) throw new Error("Duplicate node value: " + tokens[0]);
                nodeValues.put(tokens[0], Integer.parseInt(tokens[1].replace(";", "")));
            }
            if (edgeReader) {
                String[] tokens = line.replaceAll(" ", "").split(":");
                if(edgeValues.containsKey(tokens[0])) throw new Error("Duplicate edge value: " + tokens[0]);
                edgeValues.put(tokens[0], Integer.parseInt(tokens[1].replace(";", "")));
            }
        }
        br.close();
        checkMaps();
    }


    public List<Node> createSpanningTree() {
        List<Node> nodes = new ArrayList<>();

        for (var entry : nodeValues.entrySet()) {
            checkForDuplicate(entry.getKey(), nodes);
            nodes.add(new Node(entry.getKey().toUpperCase(), entry.getValue()));
        }
        for (var entry : edgeValues.entrySet()) {
            List<Node> parents = new ArrayList<>();
            for (String name : entry.getKey().split("-")) {
                Node node = Node.getElementByName(name.toUpperCase(), nodes);
                if (node != null ) parents.add(node);
            }
            for (Node node : parents) node.addEdge(new Edge(parents, entry.getValue()));
        }
        checkOrphans(nodes);
        return nodes;
    }


    public void checkMaps() {
        Integer smallestID = Integer.MAX_VALUE;
        for (var entry : nodeValues.entrySet()) {
            if (entry.getValue() < smallestID) smallestID = entry.getValue();
        }
        if(nodeValues.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).get(smallestID) > 1) throw new Error("Smallest ID exists more than once");
        for (var entry : edgeValues.entrySet()) {
            String[] values = entry.getKey().split("-");
            for (String value: values) {
                if(edgeValues.containsKey(value + "-" + value)) throw new Error("Edge pointing back to sender found: " + entry.getKey());
                if(!nodeValues.containsKey(value)) throw new Error("Node not found: " + value);
            }
            if(edgeValues.containsKey(values[1] + "-" + values[0])) throw new Error("Duplicate edge value: " + entry.getKey());
        }
    }

    private void checkOrphans(List<Node> nodes) {
        for (Node node : nodes) {
            if(node.getEdges().isEmpty()) {
                throw new Error("Orphan node found with name: " + node.getName());
            }
        }
    }
    private void checkForDuplicate(String nodeName, List<Node> nodes) {
        if(Node.getElementByName(nodeName, nodes) != null) throw new Error("Duplicate node found: " + nodeName);
    }
}