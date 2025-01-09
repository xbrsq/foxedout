package xbrsq;

public class SkillEntry {
    public String name;
    public int level;
    public int xp;
    public int maxxp;
    public boolean highlighted;

    public SkillEntry(String name, int level, int xp, int maxxp){
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.maxxp = maxxp;
        this.highlighted = false;
    }

    public SkillEntry(String name, int[] data){
        this(name, data[0], data[1], data[2]);
    }
}
