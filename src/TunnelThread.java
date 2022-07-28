import java.util.concurrent.BlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

public class TunnelThread implements Runnable{
    private final BlockingDeque<Train> train;
    private final String tunnelName;
    ReentrantLock lockTunnel;

    public TunnelThread(BlockingDeque<Train> train, String tunnelName, ReentrantLock lockTunnel) {
        this.train = train;
        this.tunnelName = tunnelName;
        this.lockTunnel = lockTunnel;
    }

    public  void run() {
        lockTunnel.lock();
        try {
            MainThread.printTrainCrossingTunnelStatus(train,tunnelName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lockTunnel.unlock();
        }
    }
}
