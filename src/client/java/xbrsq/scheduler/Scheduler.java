package xbrsq.scheduler;

import java.util.LinkedList;

public class Scheduler {

    private final LinkedList<ScheduledEvent> events = new LinkedList<>();

    private int time = 0;

    public void addEvent(EventBody body, int executionDelay){
        events.addFirst(new ScheduledEvent(body, time+ executionDelay));
    }

    public void tick(){
        time++;
        ScheduledEvent event;
        for(int i=0;i<events.size();i++){
            event = events.get(i);
            if(event.time()<=time){
                event.body().execute();
                events.remove(i--);
            }
        }
    }
}
