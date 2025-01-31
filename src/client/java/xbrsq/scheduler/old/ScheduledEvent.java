package xbrsq.scheduler.old;

public class ScheduledEvent {
    protected final EventBody body;
    protected int time;
    public ScheduledEvent(EventBody body, int time) {
        this.body = body;
        this.time = time;
    }

    public EventBody getBody(){
        return body;
    }

    public boolean doExecute(int currentTime){
        return time<=currentTime;
    }
    public boolean doRemove(){
        return true;
    }
}
