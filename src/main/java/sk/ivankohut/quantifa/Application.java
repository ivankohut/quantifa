package sk.ivankohut.quantifa;

public class Application {

    private final String name;

    public Application(String name) {
        this.name = name;
    }

    public String greeting() {
        return "Hello from " + name;
    }

    public static void main(String[] args) {
        System.out.println(new Application("quantifa").greeting());
    }
}
