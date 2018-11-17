import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AgentHandleRequestThread extends Thread {
    private Agent _parent;
    private Socket _socket;

    public AgentHandleRequestThread(Agent parent, Socket socket) {
        _socket = socket;
        _parent = parent;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            String request = in.readLine();
            System.out.println(_parent + " recieved " + request);

            switch(Request.getType(request)) {
                case JOIN:
                    handleJoin(new JoinRequest(request), out);
                    break;
                case ADD:
                    handleAdd(new AddRequest(request));
                    break;
                case PLAY:
                    handlePlay(new PlayRequest(request), out);
                    break;
                case QUIT:
                    handleQuit(new QuitRequest(request, true));
                    break;
                default:
                    System.out.println("Error in request: " + request);
            }
            _socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void handleJoin(JoinRequest request, PrintWriter out) {
        out.println(_parent.me);
        _parent.names_to_agents.values().forEach(out::println);

        _parent.names_to_agents.put(request.name, new AgentRecord(request.name, request.ip, request.port));
        _parent.agents_to_matches.put(_parent.names_to_agents.get(request.name), new ArrayList<>());

        _parent.add(new AddRequest(request.name, request.ip, request.port));
        _parent.play(new PlayRequest(_parent.me.name, request.name, (int)(Math.random()*1000)));
    }
    private void handleAdd(AddRequest request) {
        _parent.names_to_agents.put(request.name, new AgentRecord(request.name, request.ip, request.port));
        _parent.agents_to_matches.put(_parent.names_to_agents.get(request.name), new ArrayList<>());

        _parent.play(new PlayRequest(_parent.me.name, request.name, (int)(Math.random()*1000)));
    }
    private void handlePlay(PlayRequest request, PrintWriter out) {
        int my_number = (int)(Math.random() * 1000);
        out.println(_parent.me.name + " " + request.player_from + " " + my_number);
        if(_parent.names_to_agents.containsKey(request.player_from))
            _parent.agents_to_matches.get(_parent.names_to_agents.get(request.player_from)).add(
                    new MatchOutcome(_parent.names_to_agents.get(request.player_from), ((request.number + my_number) % 2) > 0));
    }
    private void handleQuit(QuitRequest request) {
        _parent.agents_to_matches.remove(_parent.names_to_agents.get(request.name));
        _parent.names_to_agents.remove(request.name);
    }
}