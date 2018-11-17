import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AgentListenThread extends Thread {
    private Agent _parent;
    private ServerSocket _listen;

    public AgentListenThread(Agent parent, ServerSocket listen) {
        _parent = parent;
        _listen = listen;
    }

    @Override
    public void run() {
        System.out.println(_parent + " is listening...");

        Socket socket;
        while (true) {
            try {
                socket = _listen.accept();
                new AgentHandleRequestThread(_parent, socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
