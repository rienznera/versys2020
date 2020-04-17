package server;

public class ConnectionWaiter extends Thread {
    private Server server;
    private int timeout;

    public ConnectionWaiter(Server server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }


    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void run(){
        try {
            Thread.sleep(this.timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.timeout();
    }

 }
