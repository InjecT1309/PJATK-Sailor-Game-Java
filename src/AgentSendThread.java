import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AgentSendThread extends Thread {
    private Agent _parent;
    private Socket _socket;
    private Request _request;
    private PrintWriter _out;
    private BufferedReader _in;

    public AgentSendThread(Agent parent, Socket socket, Request request) {
        _parent = parent;
        _socket = socket;
        _request = request;
    }

    @Override
    public void run() {
        try {
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            switch (_request.type) {
                case JOIN:
                    sendJoin();
                    break;
                case ADD:
                    sendAdd();
                    break;
                case PLAY:
                    sendPlay();
                    break;
                case QUIT:
                    sendQuit();
                    break;
                default:
                    System.out.println("Error in request: " + _request);
            }

            _socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendJoin() throws IOException {
        System.out.println(_parent + " is sending: " + _request);
        _out.println(_request);

        JoinResponse response = new JoinResponse();
        response.readResponse(_in);

        System.out.println(_parent + " recieved: " + response);
        for(AgentRecord agent : response.agents) {
            _parent.names_to_agents.put(agent.name, new AgentRecord(agent.name, agent.ip, agent.port));
            _parent.agents_to_matches.put(_parent.names_to_agents.get(agent.name), new ArrayList<>());
        }
    }
    private void sendAdd() {
        System.out.println(_parent + " is sending: " + _request);
        _out.println(_request);
    }
    private void sendPlay() throws IOException {
        PlayRequest play_request = (PlayRequest)_request;
        int my_number = play_request.number;
        System.out.println(_parent + " is sending: " + _request);
        _out.println(_request);

        PlayResponse response = new PlayResponse();
        response.readResponse(_in);

        System.out.println(_parent + " recieved: " + response);
        if(_parent.names_to_agents.containsKey(response.player_from))
            _parent.agents_to_matches.get(_parent.names_to_agents.get(response.player_from)).add(
                    new MatchOutcome(_parent.names_to_agents.get(response.player_from), ((response.number + my_number + 1) % 2) > 0));
    }
    private void sendQuit() {
        System.out.println(_parent + " is sending: " + _request);
        _out.println(_request);
    }
}
