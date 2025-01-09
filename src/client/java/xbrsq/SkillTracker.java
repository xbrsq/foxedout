package xbrsq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SkillTracker {
    private static final Map<String, SkillEntry> skills = new HashMap<>();

    public static ChatContext chatMode = ChatContext.NORMAL;

    public static String focus = "";

    public static long currentTime = 0L;


    public static void updateSkill(String skill, Integer level){
        updateSkill(skill, level, 0, 1);
    }

    public static void updateSkill(String skill, int level, int xp, int xpMax){
        SkillEntry se = getByName(skill);
        if(se != null){
            se.level = level;
            se.xp = xp;
            se.maxxp = xpMax;
            return;
        }
        SkillEntry temp = new SkillEntry(skill, new int[]{level, xp, xpMax});
        temp.time = currentTime++;
        skills.put(skill, temp);
    }

    public static void updateSkillXP(String skill, int level, float percent){

        // in theory, I could be checking values of maxXP to find the most likely one that matches percent.
        // in practice, I'm lazy. Percent is out of 100 now.
        // also, I'm lazy, so it will wipe the highlight.

        SkillEntry temp = new SkillEntry(skill, new int[]{level, (int) (percent * 100), 100, 0});
        temp.time = currentTime++;
        skills.put(skill, temp);
    }

    public static void clearSkills(){
        skills.clear();
        currentTime = 0L;
    }


    public static boolean parseText(String text){

        String enterIgnoreModeRegex = "\\[]=====\\[].*";
        String exitIgnoreModeRegex = "\\[]=====\\[].*";

        String increaseRegex = "§l(\\w+) increased to §r§a§l(\\w+)§r§f";
        String statsRegex = "(\\w+): (\\w+) XP\\(([0-9,]+)/([0-9,]+)\\).*";

//        Acrobatics: 96 XP(911/2,940)

        Pattern pattern = Pattern.compile(enterIgnoreModeRegex);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            chatMode = ChatContext.SKILL_DETAILS;
            return false;
        }

        pattern = Pattern.compile(exitIgnoreModeRegex);
        matcher = pattern.matcher(text);

        if(matcher.find()){
            chatMode = ChatContext.NORMAL;
            return false;
        }

        pattern = Pattern.compile(increaseRegex);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String skillName = matcher.group(1);
            String levelString = matcher.group(2);

            int level = Integer.parseInt(levelString.replace(",",""));

            updateSkill(skillName, level);
            return true;

        }

        if(chatMode == ChatContext.SKILL_DETAILS) return false;
        pattern = Pattern.compile(statsRegex);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String skillName = matcher.group(1);

            int level = parseCommaedInt(matcher.group(2));
            String xpString = matcher.group(3);
            String xpMaxString = matcher.group(4);

            int xp = parseCommaedInt(xpString);
            int xpMax = parseCommaedInt(xpMaxString);


            updateSkill(skillName, level, xp, xpMax);
            return true;

        }
        return false;
    }

    public static int parseCommaedInt(String i){
        try {
            System.out.println(">"+i+"<");
            return Integer.parseInt(i.replace(",",""));
        }
        catch (Exception e){
            System.out.println("Error in comma-ed number parsing");
            return 0;
        }
    }

    public static void parseBar(String name, float percent){
        String bossBarRegex = "(\\w+) Lv\\.([0-9,]+)";
        Pattern pattern = Pattern.compile(bossBarRegex);
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            String skillName = matcher.group(1);

            int level = SkillTracker.parseCommaedInt(matcher.group(2));

            SkillTracker.updateSkillXP(skillName, level, percent);
        }
    }

    public static ArrayList<SkillEntry> getSkills(boolean forceAll){
        ArrayList<SkillEntry> rtrn = new ArrayList<>();
        // if focused, only return focus
        if(!forceAll && getByName(focus)!=null){
            rtrn.add(getByName(focus));
            return rtrn;
        }

        // otherwise, return all.
        for (Map.Entry<String, SkillEntry> entry : skills.entrySet()) {
            rtrn.add(entry.getValue());
        }
        return rtrn;
    }
    public static ArrayList<SkillEntry> getSkills() {
        return getSkills(false);
    }

    public static SkillEntry getByName(String name){
        for (Map.Entry<String, SkillEntry> entry : skills.entrySet()) {
            SkillEntry se = entry.getValue();
            if(se.name.equalsIgnoreCase(name)){
                return se;
            }
        }return null;
    }
}
