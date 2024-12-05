import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Input {
    private final Map<String, Integer> nodeValues = new HashMap<>();
    private final Map<String, Integer> edgeValues = new HashMap<>();

    public String readInputFile() throws IOException {
        File file = new File("./src/input.txt");
        if (!file.exists()) throw new Error("Input file does not exist");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        Pattern startPattern = Pattern.compile("^Graph\s*([a-zA-Z0-9]+)\s*\\{");
        Pattern nodePattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9]*)=([0-9]+)");
        Pattern edgePattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9]*-[a-zA-Z]+[a-zA-Z0-9]*):([0-9]+)");
        Matcher matcher = startPattern.matcher(br.readLine());
        if (!matcher.find()) throw new Error("Input file does not contain graph");
        String graphName = matcher.group(1);
        while((line = br.readLine()) != null) {
            line = line.replaceAll(" ", "");
            matcher = nodePattern.matcher(line);
            if(matcher.find()) createNode(matcher.group(1), matcher.group(2));
            matcher = edgePattern.matcher(line);
            if(matcher.find()) createEdge(matcher.group(1), matcher.group(2));
        }
        br.close();
        checkMaps();
        return graphName;
    }
    private void createNode(String nodeName, String nodeID) {
        if(nodeValues.containsKey(nodeName)) throw new Error("Duplicate node value: " + nodeName);
        nodeValues.put(nodeName, Integer.parseInt(nodeID));
    }
    private void createEdge(String nodeNames, String cost) {
        if(edgeValues.containsKey(nodeNames)) throw new Error("Duplicate edge value: " + nodeNames);
        edgeValues.put(nodeNames, Integer.parseInt(cost));
    }

    public List<Node> createSpanningTree() {
        List<Node> nodes = new ArrayList<>();
        for (var entry : nodeValues.entrySet()) {
            checkForDuplicate(entry.getKey(), nodes);
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        for (var entry : edgeValues.entrySet()) {
            List<Node> parents = new ArrayList<>();
            for (String name : entry.getKey().split("-")) {
                Node node = Node.getElementByName(name, nodes);
                if (node != null ) parents.add(node);
            }
            for (Node node : parents) node.addEdge(new Edge(parents, entry.getValue()));
        }
        checkOrphans(nodes);
        return nodes;
    }

//  Error Checking and Handling
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
            if(node.getEdges().isEmpty()) throw new Error("Orphan node found with name: " + node.getName());
        }
    }
    private void checkForDuplicate(String nodeName, List<Node> nodes) {
        if(Node.getElementByName(nodeName, nodes) != null) throw new Error("Duplicate node found: " + nodeName);
    }
}