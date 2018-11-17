public abstract class Request {
    public enum Type {
        ERROR,
        JOIN,
        ADD,
        PLAY,
        QUIT
    }
    Type type = Type.ERROR;

    public abstract void parse(String request);

    public Request() { }
    public Request(String request) {
        parse(request);
    }

    public static Type getType(String req) {
        if(req.contains("JOIN"))
            return Type.JOIN;
        else if(req.contains("ADD"))
            return Type.ADD;
        else if(req.contains("PLAY"))
            return Type.PLAY;
        else if(req.contains("QUIT"))
            return Type.QUIT;
        return Type.ERROR;
    }
}
