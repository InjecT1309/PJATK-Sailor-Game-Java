public class QuitRequest extends Request {
    public String name;

    public QuitRequest(String name) {
        super();
        this.name = name;
        type = Type.QUIT;
    }
    public QuitRequest(String request, boolean is_request) {
        super(request);
        type = Type.QUIT;
    }

    @Override
    public void parse(String request) {
        String args[] = request.split(" ");
        if(args.length != 2) {
            System.out.println("Request has an unexpected number of arguments: \n" + request);
            return;
        }
        if(args[0].equals("QUIT")) {
            this.name = args[1];
        }
    }

    @Override
    public String toString() {
        return "QUIT " + name;
    }
}
