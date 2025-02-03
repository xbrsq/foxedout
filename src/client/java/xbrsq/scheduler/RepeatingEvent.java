package xbrsq.scheduler;

public class RepeatingEvent extends ScheduledEvent{

    protected int repeatTime;
    protected int numRepeats;
    public RepeatingEvent(EventBody body, int time) {
        this(body, time, 0, 0);
    }
    public RepeatingEvent(EventBody body, int time, int repeatTime) {
        this(body, time, repeatTime, Integer.MAX_VALUE);
    }
    public RepeatingEvent(EventBody body, int currentTime, int repeatTime, int numRepeats){
        super(body, currentTime);
        this.repeatTime = repeatTime;
        this.numRepeats = numRepeats;
    }

    @Override
    public boolean doRemove(int time){
        numRepeats--;
        if(numRepeats<=0)
            return true;
        this.time += this.repeatTime;
        return false;
    }
}
