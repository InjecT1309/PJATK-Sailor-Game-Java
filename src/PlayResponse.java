import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

public class PlayResponse {
    public String player_from;
    public String player_to;
    public int number;

    public PlayResponse() { }
    public PlayResponse(String player_from, String player_to, int number) {
        this.player_from = player_from;
        this.player_to = player_to;
        this.number = number;
    }
    public void readResponse(BufferedReader in) throws IOException {
        String args[] = in.readLine().split(" ");
        player_from = args[0];
        player_to = args[1];
        number = Integer.parseInt(args[2]);
    }

    public boolean checkHash(byte hash[]) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(Integer.toString(number).getBytes());
        byte my_hash[] = digest.digest();
        return Arrays.equals(my_hash, hash);
    }

    @Override
    public String toString() { return player_from + " " + player_to + " " + number; }
}
