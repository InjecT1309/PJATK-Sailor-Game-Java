public class PlayRequest extends Request{
    public String player_from;
    public String player_to;
    public int number;

    public PlayRequest(String player_from, String player_to, int number) {
        super();
        this.player_from = player_from;
        this.player_to = player_to;
        this.number = number;
        type = Type.PLAY;
    }
    public PlayRequest(String request) {
        super(request);
        type = Type.PLAY;
    }

    @Override
    public void parse(String request) {
        String args[] = request.split(" ");
        if(args.length != 4) {
            System.out.println("Request has an unexpected number of arguments: \n" + request);
            return;
        }
        if(args[0].equals("PLAY")) {
            this.player_from = args[1];
            this.player_to = args[2];
            this.number = Integer.parseInt(args[3]);
        }
    }
    @Override
    public String toString() {
        return "PLAY " + player_from + " " + player_to + " " + number;
    }
}
