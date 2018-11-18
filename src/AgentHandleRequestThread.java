import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AgentHandleRequestThread extends Thread {
    private Agent _parent;
    private Socket _socket;
    private BufferedReader _in;
    private PrintWriter _out;

    public AgentHandleRequestThread(Agent parent, Socket socket) {
        _socket = socket;
        _parent = parent;
    }

    @Override
    public void run() {
        try {
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _out = new PrintWriter(_socket.getOutputStream(), true);
            String request = _in.readLine();
            System.out.println(_parent + " recieved " + request);

            switch(Request.getType(request)) {
                case JOIN:
                    handleJoin(new JoinRequest(request));
                    break;
                case ADD:
                    handleAdd(new AddRequest(request));
                    break;
                case PLAY:
                    handlePlay(new PlayRequest(request));
                    break;
                case QUIT:
                    handleQuit(new QuitRequest(request, true));
                    break;
                default:
                    System.out.println("Error in request: " + request);
            }
            _socket.close();
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void handleJoin(JoinRequest request) {
        if(_parent.names_to_agents.keySet().contains(request.name)) {
            _out.println(); //Username taken
            return;
        }

        _out.println(_parent.me);
        _parent.names_to_agents.values().forEach(_out::println);

        _parent.names_to_agents.put(request.name, new AgentRecord(request.name, request.ip, request.port));
        _parent.agents_to_matches.put(_parent.names_to_agents.get(request.name), new ArrayList<>());

        _parent.add(new AddRequest(request.name, request.ip, request.port));
        _parent.play(new PlayRequest(_parent.me.name, request.name, (int)(Math.random()*1000000)));
    }
    private void handleAdd(AddRequest request) {
        _parent.names_to_agents.put(request.name, new AgentRecord(request.name, request.ip, request.port));
        _parent.agents_to_matches.put(_parent.names_to_agents.get(request.name), new ArrayList<>());

        _parent.play(new PlayRequest(_parent.me.name, request.name, (int)(Math.random()*1000000)));
    }
    private void handlePlay(PlayRequest request) throws Exception {
        if(!_parent.names_to_agents.containsKey(request.player_from)) {
            System.out.println(_parent + " doesnt know who is " + request.player_from);
            return;
        }

        int my_number = (int)(Math.random() * 1000000);
        _out.println(new PlayResponse(_parent.me.name, request.player_from, my_number));

        PlayResponse response = new PlayResponse();
        response.readResponse(_in);

        if(response.checkHash(request.hash)) {
            MatchOutcome outcome = new MatchOutcome(_parent.me, _parent.names_to_agents.get(request.player_from),
                    my_number, response.number, ((my_number + response.number) % 2) > 0); //start counting from the sender
            _parent.agents_to_matches.get(_parent.names_to_agents.get(request.player_from)).add(outcome);
            System.out.println(outcome);
        } else
            System.out.println("CHEATER");
    }
    private void handleQuit(QuitRequest request) {
        if(!_parent.names_to_agents.containsKey(request.name)) {
            System.out.println(_parent + " doesnt know who is " + request.name);
            return;
        }

        _parent.agents_to_matches.remove(_parent.names_to_agents.get(request.name));
        _parent.names_to_agents.remove(request.name);
    }
}