package xbrsq.scheduler.old;

public class RepeatingEvent extends ScheduledEvent{

    protected int repeatTime;
    protected int numRepeats;
    public RepeatingEvent(EventBody body, int time, int repeatTime, int numRepeats) {
        super(body, repeatTime+time);
        this.repeatTime = repeatTime;
    }
    public RepeatingEvent(EventBody body, int time, int repeatTime) {
        this(body, time, repeatTime, 10);
    }

    @Override
    public boolean doRemove(){
        time += this.repeatTime;
        return false;
    }
}
