public class MatchOutcome {
    public AgentRecord me;
    public AgentRecord player;
    public int my_num;
    public int his_num;
    public boolean won;

    public MatchOutcome(AgentRecord me, AgentRecord player, int my_num, int his_num, boolean won) {
        this.me = me;
        this.player = player;
        this.my_num = my_num;
        this.his_num = his_num;
        this.won = won;
    }

    @Override
    public String toString() {
        return me + " - " + my_num + " | " + player + " - " + his_num + " | " + (won ? "won" : "lost");
    }
}
