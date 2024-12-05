public class BPDU {
    private final String source;
    private final Integer rootID;
    private Integer totalCost;

    public BPDU (String source, Integer rootID, Integer totalCost) {
        this.source = source;
        this.rootID = rootID;
        this.totalCost = totalCost;
    }

    public void addCost(Integer costAdditive) {
        this.totalCost += costAdditive;
    }

    public String getSource() {
        return source;
    }
    public Integer getRootID() {
        return rootID;
    }
    public Integer getTotalCost() {
        return totalCost;
    }
}