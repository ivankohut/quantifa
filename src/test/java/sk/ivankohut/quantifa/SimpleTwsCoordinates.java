package sk.ivankohut.quantifa;

public class SimpleTwsCoordinates implements TwsCoordinates {

    private final String hostName;
    private final int port;

    public SimpleTwsCoordinates(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public SimpleTwsCoordinates() {
        this("localhost", 7496);
    }

    @Override
    public String hostName() {
        return hostName;
    }

    @Override
    public int port() {
        return port;
    }
}
