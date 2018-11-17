import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddRequest extends Request {
    public String name;
    public InetAddress ip;
    public int port;

    public AddRequest(String name, InetAddress ip, int port) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
        type = Request.Type.ADD;
    }
    public AddRequest(String request) {
        super(request);
        type = Request.Type.ADD;
    }

    @Override
    public void parse(String request) {
        String args[] = request.split(" ");
        if(args.length != 4) {
            System.out.println("Request has an unexpected number of arguments: \n" + request);
            return;
        }
        if(args[0].equals("ADD")) {
            try {
                this.name = args[1];
                this.ip = InetAddress.getByName(args[2]);
                this.port = Integer.parseInt(args[3]);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String toString() {
        return "ADD " + name + " " + ip.toString().replaceFirst("/", "") + " " + port;
    }
}
