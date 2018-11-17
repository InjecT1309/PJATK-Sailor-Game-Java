import java.io.BufferedReader;
import java.io.IOException;

public class PlayResponse {
    public String player_from;
    public String player_to;
    public int number;

    public void readResponse(BufferedReader in) throws IOException {
        String args[] = in.readLine().split(" ");
        player_from = args[0];
        player_to = args[1];
        number = Integer.parseInt(args[2]);
    }

    @Override
    public String toString() { return player_from + " " + player_to + " " + number; }
}
