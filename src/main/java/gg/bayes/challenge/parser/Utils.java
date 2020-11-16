package gg.bayes.challenge.parser;

class Utils {

    static boolean isHero(String s) {
        return s.startsWith(EventConstants.HERO_PREFIX);
    }

    static String getHeroName(String s) {
        return s.replace(EventConstants.HERO_PREFIX,"");
    }

    static String getItemName(String s) {
        return s.replace(EventConstants.ITEM_PREFIX,"");
    }

    static String getSpellName(String s, String heroName) {
        if (s.startsWith(heroName))
            return s;
        else
            return null;
    }

    public static long  toMilliseconds(String s) {
        s = s.replace("[", "").replace("]", "").replace(".","");
        String[] x = s.split(":");
        long hoursInMilliseconds = Long.parseLong(x[0]) * 60L * 60L * 1000L;
        long minutesInMilliseconds = Long.parseLong(x[1]) * 60L * 1000L;
        long remainingMilliseconds = Long.parseLong(x[2]);
        return hoursInMilliseconds + minutesInMilliseconds + remainingMilliseconds;
    }

}
