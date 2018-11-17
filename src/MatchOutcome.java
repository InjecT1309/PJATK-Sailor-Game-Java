public class MatchOutcome {
    public AgentRecord player;
    boolean won;

    public MatchOutcome(AgentRecord player, boolean won) {
        this.player = player;
        this.won = won;
    }

    @Override
    public String toString() {
        return (won ? "Won with " : "Lost to ") + player;
    }
}
