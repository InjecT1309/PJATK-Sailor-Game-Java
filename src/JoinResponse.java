import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class JoinResponse {
    public ArrayList<AgentRecord> agents = new ArrayList<>();

    public JoinResponse() { }

    public void readResponse(BufferedReader in) throws IOException {
        String line;
        while((line = in.readLine()) != null && !line.isEmpty())
        {
            String args[] = line.split(" ");
            agents.add(new AgentRecord(args[0], InetAddress.getByName(args[1]), Integer.parseInt(args[2])));
        }
    }

    @Override
    public String toString() { return agents.toString(); }
}
