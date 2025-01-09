package xbrsq;

import java.util.ArrayList;

public class SkillSorter {
    public static final int S_NAME = 1;
    public static final int S_LEVEL = 2;
    public static final int S_PERCENT = 3;
    public static final int S_TIME = 4;
    public static final int S_NONE = 5;

    public static int default_sorting = S_NAME;

    public static ArrayList<SkillEntry> sort(ArrayList<SkillEntry> skills){return sort(skills, default_sorting);}

    public static ArrayList<SkillEntry> sort(ArrayList<SkillEntry> skills, int sortBy){
        if(skills.isEmpty()){
            return new ArrayList<>();
        }

        if(sortBy == S_NONE){
            return new ArrayList<>(skills);
        }


        ArrayList<SkillEntry> rtrn = new ArrayList<>();



//        boolean[] completed = new boolean[skills.size()];

//        int currentLowest = 0;

        rtrn.add(skills.removeFirst()); // needs to start with 1 element in rtrn to compare with

        int pos;
        for (SkillEntry skill : skills) {
            pos = rtrn.size();
            for (int j = 0; j < rtrn.size(); j++) {
//                if(!completed[j]) {
//                    if (compare(skills.get(j), skills.get(currentLowest), sortBy) < 0) {
//                        currentLowest = j;
//                    }
//                }


                // i is what to insert
                // j is what to compare to
                if (compare(skill, rtrn.get(j), sortBy)*(sortBy>=0?1:-1) <= 0) {
                    pos = j;
                    break;
                }
            }
            rtrn.add(pos, skill);

//            completed[currentLowest] = true;
//            rtrn.add(skills.get(currentLowest));
        }

        return rtrn;
    }

    private static int compare(SkillEntry a, SkillEntry b, int sortBy){
        // returns <0 when a is less, and >0 when a is greater, and 0 for equal
        return switch (sortBy*sortBy) {
            case S_NAME*S_NAME -> a.name.toLowerCase().compareTo(b.name.toLowerCase());
            case S_LEVEL*S_LEVEL -> Integer.compare(a.level, b.level);
            case S_PERCENT*S_PERCENT -> Integer.compare(a.getPercent(), b.getPercent());
            case S_TIME*S_TIME -> Long.compare(a.time, b.time);
            default -> 0;
        };
    };



}
