import java.net.InetAddress;

public class AgentRecord {
    public String name;
    public InetAddress ip;
    public int port;

    public AgentRecord(String name, InetAddress ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return name + " " + ip.toString().replaceFirst("/", "") + " " + port;
    }
}
