import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PlayRequest extends Request{
    public String player_from;
    public String player_to;
    public int number = -1;
    public byte[] hash;

    public PlayRequest(String player_from, String player_to, int number) {
        super();
        this.player_from = player_from;
        this.player_to = player_to;
        this.number = number;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(Integer.toString(number).getBytes());
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        type = Type.PLAY;
    }
    public PlayRequest(String request) {
        super(request);
        type = Type.PLAY;
    }

    @Override
    public void parse(String request) {
        String args[] = request.split(" ");
        if(args[0].equals("PLAY")) {
            player_from = args[1];
            player_to = args[2];
            hash = Base64.getDecoder().decode(args[3]);
        }
    }
    @Override
    public String toString() {
        return "PLAY " + player_from + " " + player_to + " " + Base64.getEncoder().encodeToString(hash);
    }
}
