import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class Agent {
    public AgentRecord me;
    public HashMap<String, AgentRecord> names_to_agents = new HashMap<>();
    public HashMap<AgentRecord, List<MatchOutcome>> agents_to_matches = new HashMap<>();
    private ServerSocket listen;
    private AgentListenThread listenThread;

    public Agent(String name, String ip, int port, String connect_ip, int connect_port) {
        try {
            try {
                me = new AgentRecord(name, InetAddress.getByName(ip), port);
                listen = new ServerSocket(port);
                listenThread = new AgentListenThread(this, listen);
                listenThread.start();
            } catch (BindException e) {
                System.out.println("Adress already taken");
                return;
            }
            join(connect_ip, connect_port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void join(String connect_ip, int connect_port) {
        try {
            if(me.ip != InetAddress.getByName(connect_ip) && me.port != connect_port)
                new AgentSendThread(this, new Socket(connect_ip, connect_port), new JoinRequest(me.name, me.ip, me.port)).start();
            else
                System.out.println(me + " is a host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void add(AddRequest request) {
        for(AgentRecord agent : names_to_agents.values()) {
            try {
                if(!agent.name.equals(request.name))
                    new AgentSendThread(this, new Socket(agent.ip, agent.port), request).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void play(PlayRequest request) {
        AgentRecord agent = names_to_agents.get(request.player_to);
        try {
            new AgentSendThread(this, new Socket(agent.ip, agent.port), request).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void quit() {
        for(AgentRecord agent : agents_to_matches.keySet()) {
            try {
                new AgentSendThread(this, new Socket(agent.ip, agent.port), new QuitRequest(me.name)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listenThread.stop();
        try {
            listen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(me + " is quitting. \nScores:");
        agents_to_matches.values().forEach(System.out::println);
    }

    @Override
    public String toString() {
        return me.toString();
    }
}