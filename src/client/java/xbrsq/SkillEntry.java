package xbrsq;

public class SkillEntry {
    public String name;
    public int level;
    public int xp;
    public int maxxp;
    public long time;
    public boolean highlighted;
    public boolean pinned;

    public SkillEntry(String name, int level, int xp, int maxxp){
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.maxxp = maxxp;
        this.highlighted = false;
        this.pinned = false;
    }

    public SkillEntry(String name, int[] data){
        this(name, data[0], data[1], data[2]);
    }

    public int getPercent(){
        return this.xp*100 / this.maxxp;
    }
}
