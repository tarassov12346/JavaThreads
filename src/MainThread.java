import java.time.LocalTime;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MainThread {

    public static void main(String[] args) throws InterruptedException {
        BlockingDeque<Train> trainQueueTunnelA = generateTrainQueueTunnelA();
        BlockingDeque<Train> trainQueueTunnelB = generateTrainQueueTunnelB();
        BlockingDeque<Train> trainCrossingTheTunnelA = new LinkedBlockingDeque<>(1);
        BlockingDeque<Train> trainCrossingTheTunnelB = new LinkedBlockingDeque<>(1);
        BlockingDeque<Train> trainWithPreferenceToCrossTunnelA = new LinkedBlockingDeque<>();
        BlockingDeque<Train> trainWithPreferenceToCrossTunnelB = new LinkedBlockingDeque<>();
        ReentrantLock lockTunnelA = new ReentrantLock();
        ReentrantLock lockTunnelB = new ReentrantLock();

        new Thread(() -> {
            do {
                try {
                    if (!trainCrossingTheTunnelA.offer(trainQueueTunnelA.element(), trainQueueTunnelA.element().getTrainAcceptableWaitingTime(), TimeUnit.SECONDS)) {
                        System.out.printf("\nTunnel-A: %s waiting time elapsed, redirected to Tunnel-B!!!!!!!!!!!!!!", trainQueueTunnelA.element().getTrainName());
                        trainQueueTunnelA.element().setTrainName(trainQueueTunnelA.element().getTrainName() + "(redirected from " + "Tunnel-A" + " to Tunnel-B)");
                        trainWithPreferenceToCrossTunnelB.put(trainQueueTunnelA.take());
                        new Thread(new TunnelThread(trainWithPreferenceToCrossTunnelB, "Tunnel-B", lockTunnelB)).start();
                        continue;
                    }
                    new Thread(new TunnelThread(trainCrossingTheTunnelA, "Tunnel-A", lockTunnelA)).start();
                    trainQueueTunnelA.removeFirst();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (trainQueueTunnelA.size() != 0);
        }).start();

        new Thread(() -> {
            do {
                try {
                    if (!trainCrossingTheTunnelB.offer(trainQueueTunnelB.element(), trainQueueTunnelB.element().getTrainAcceptableWaitingTime(), TimeUnit.SECONDS)) {
                        System.out.printf("\nTunnel-B: %s waiting time elapsed, redirected to Tunnel-A!!!!!!!!!!!!", trainQueueTunnelB.element().getTrainName());
                        trainQueueTunnelB.element().setTrainName(trainQueueTunnelB.element().getTrainName() + "(redirected from " + "Tunnel-B" + " to Tunnel-A)");
                        trainWithPreferenceToCrossTunnelA.put(trainQueueTunnelB.take());
                        new Thread(new TunnelThread(trainWithPreferenceToCrossTunnelA, "Tunnel-A", lockTunnelA)).start();
                        continue;
                    }
                    new Thread(new TunnelThread(trainCrossingTheTunnelB, "Tunnel-B", lockTunnelB)).start();
                    trainQueueTunnelB.removeFirst();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (trainQueueTunnelB.size() != 0);
        }).start();

    }

    private static BlockingDeque<Train> generateTrainQueueTunnelA() throws InterruptedException {
        BlockingDeque<Train> trainQueueForTheTunnel = new LinkedBlockingDeque<>();
        trainQueueForTheTunnel.put(new Train(20, 3, "Train-1", "Tunnel-A"));
        trainQueueForTheTunnel.put(new Train(6, 21, "Train-2", "Tunnel-A"));
        trainQueueForTheTunnel.put(new Train(18, 5, "Train-3", "Tunnel-A"));
        trainQueueForTheTunnel.put(new Train(5, 7, "Train-4", "Tunnel-A"));
        trainQueueForTheTunnel.put(new Train(4, 6, "Train-5", "Tunnel-A"));
        return trainQueueForTheTunnel;
    }

    private static BlockingDeque<Train> generateTrainQueueTunnelB() throws InterruptedException {
        BlockingDeque<Train> trainQueueForTheTunnel = new LinkedBlockingDeque<>();
        trainQueueForTheTunnel.put(new Train(15, 3, "Train-6", "Tunnel-B"));
        trainQueueForTheTunnel.put(new Train(6, 16, "Train-7", "Tunnel-B"));
        trainQueueForTheTunnel.put(new Train(8, 3, "Train-8", "Tunnel-B"));
        trainQueueForTheTunnel.put(new Train(35, 9, "Train-9", "Tunnel-B"));
        return trainQueueForTheTunnel;
    }

    public static void printTrainCrossingTunnelStatus(BlockingDeque<Train> train, String tunnelName) throws InterruptedException {
        System.out.printf("\n%s: %s Thread started Time:%s", tunnelName, train.element().getTrainName(), LocalTime.now());
        System.out.printf("\n%s: %s Travel Time:%ss", tunnelName, train.element().getTrainName(), train.element().getTrainTunnelCrossingTime());
        TimeUnit.MILLISECONDS.sleep(train.element().getTrainTunnelCrossingTime() * 1000);
        System.out.printf("\n%s: PASSED:%s Time:%s", tunnelName, train.takeFirst().getTrainName().toUpperCase(), LocalTime.now());
        System.out.print("\n****************************************************************************");
    }
}
