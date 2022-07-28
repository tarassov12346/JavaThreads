public class Train {
    private final int trainTunnelCrossingTime;
    private final int trainAcceptableWaitingTime;
    private String trainName;
    private final String tunnelName;

    public Train(int trainTravelTime, int trainAcceptableWaitingTime, String trainName, String tunnelName) {
        this.trainTunnelCrossingTime = trainTravelTime;
        this.trainAcceptableWaitingTime = trainAcceptableWaitingTime;
        this.trainName = trainName;
        this.tunnelName = tunnelName;
    }

    public int getTrainTunnelCrossingTime() {
        return trainTunnelCrossingTime;
    }

    public int getTrainAcceptableWaitingTime() {
        return trainAcceptableWaitingTime;
    }



    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainTunnelCrossingTime=" + trainTunnelCrossingTime +
                ", trainAcceptableWaitingTime=" + trainAcceptableWaitingTime +
                ", trainName='" + trainName + '\'' +
                ", tunnelName='" + tunnelName + '\'' +
                '}';
    }
}
