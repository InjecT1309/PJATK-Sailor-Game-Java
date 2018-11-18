import java.util.HashMap;
import java.util.Scanner;

public class AgentManager {
    private HashMap<String, Agent> agents = new HashMap<>();

    public static void main(String args[]) {
        new AgentManager().start();
    }

    public void start() {
        System.out.println("Sailors Game - SKJ Project");
        printListOfCommands();
        while(true)
            inputLoop();
    }
    public void inputLoop() {
        try {
            switch (new Scanner(System.in).next().charAt(0)) {
                case 'l':
                    printAgents();
                    break;
                case 'n':
                    createAgent();
                    break;
                case 'p':
                    agentsPlay();
                    break;
                case 'r':
                    removeAgent();
                    break;
                case 'h':
                    printListOfCommands();
                    break;
                case 'q':
                    quit();
                    break;
                default:
                    System.out.println("Unknow command. Type h for list of commands");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Enter correct input!");
        }
    }
    public void printAgents() {
        agents.values().forEach(System.out::println);
    }
    public void createAgent() {
        System.out.println("Enter agent name, ip, port and point to connect to [name ip port connect_ip connect_port]");

        try {
            String[] args = new Scanner(System.in).nextLine().split(" ");

            String name = args[0];
            String ip = args[1];
            int port = Integer.parseInt(args[2]);
            String connect_ip = args[3];
            int connect_port = Integer.parseInt(args[4]);

            agents.put(name, new Agent(name, ip, port, connect_ip, connect_port));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Enter correct input!");
        }
    }
    public void agentsPlay(){
        System.out.println("Enter a local agent name, name of agent to play with and your number");

        try {
            String agent_names[] = new Scanner(System.in).nextLine().split(" ");
            String agent1 = agent_names[0];
            String agent2 = agent_names[1];
            try {
                agents.get(agent1).play(new PlayRequest(agent1, agent2, (int)(Math.random() * 1000)));
            } catch (NullPointerException e) {
                System.out.println("No such agent");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Enter correct input!");
        }
    }
    public void removeAgent() {
        System.out.println("Enter agent name");

        try {
            final String agent_name = new Scanner(System.in).nextLine();
            agents.get(agent_name).quit();
            agents.remove(agent_name);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Enter correct input!");
        }
    }
    public void printListOfCommands() {
        System.out.println("l - get list of agents");
        System.out.println("n - add new agent");
        System.out.println("p - play as an agent with another agent");
        System.out.println("r - remove agent");
        System.out.println("h - get list of commands");
        System.out.println("q - quit");
    }
    public void quit() {
        agents.values().forEach(Agent::quit);
        System.exit(0);
    }
}