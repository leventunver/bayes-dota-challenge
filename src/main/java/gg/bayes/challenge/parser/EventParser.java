package gg.bayes.challenge.parser;

public class EventParser {

    public EventType getType(String line) {
        if (line.contains(EventConstants.CAST_SIGNIFIER))
            return EventType.CAST;
        else if (line.contains(EventConstants.DAMAGE_SIGNIFIER))
            return EventType.DAMAGE;
        else if (line.contains(EventConstants.KILL_SIGNIFIER))
            return EventType.KILL;
        else if (line.contains(EventConstants.PURCHASE_SIGNIFIER))
            return EventType.PURCHASE;
        else
            return EventType.OTHER;
    }

    public String getHeroName(String line) {
        String[] s = line.split(" ");
        if (Utils.isHero(s[1]))
            return Utils.getHeroName(s[1]);
        else
            return null;
    }

    public PurchaseEvent getPurchaseEvent(String line) {
        String[] s = line.replace(EventConstants.PURCHASE_SIGNIFIER, " ").split(" ");
        if (!Utils.isHero(s[1]))
            return null;

        Long timestamp = Utils.toMilliseconds(s[0]);
        String heroName = Utils.getHeroName(s[1]);
        String itemName = Utils.getItemName(s[2]);

        PurchaseEvent purchaseEvent = PurchaseEvent.builder()
                .heroName(heroName)
                .itemName(itemName)
                .timestamp(timestamp)
                .build();
        return purchaseEvent;
    }

    public KillEvent getKillEvent(String line) {
        String[] s = line.replace(EventConstants.KILL_SIGNIFIER, " ").split(" ");
        if (!Utils.isHero(s[1]) || !Utils.isHero(s[2]))
            return null;

        Long timestamp = Utils.toMilliseconds(s[0]);
        String target = Utils.getHeroName(s[1]);
        String heroName = Utils.getHeroName(s[2]);

        KillEvent killEvent = KillEvent.builder()
                .heroName(heroName)
                .target(target)
                .timestamp(timestamp)
                .build();
        return killEvent;
    }

    public CastEvent getCastEvent(String line) {
        String[] s = line.replace(EventConstants.CAST_SIGNIFIER, " ").replace(EventConstants.ON, " ").split(" ");
        if (!Utils.isHero(s[1]))
            return null;

        Long timestamp = Utils.toMilliseconds(s[0]);
        String heroName = Utils.getHeroName(s[1]);
        String spellName = s[2];
        Integer level = Integer.parseInt(s[4].replace(EventConstants.SPELL_PREFIX, "")
                .replace(EventConstants.SPELL_SUFFIX, ""));
        String target = Utils.getHeroName(s[5]);

        CastEvent castEvent = CastEvent.builder()
                .timestamp(timestamp)
                .heroName(heroName)
                .level(level)
                .spellName(spellName)
                .target(target)
                .build();
        return castEvent;
    }

    public DamageEvent getDamageEvent(String line) {
        String[] s = line.replace(EventConstants.DAMAGE_SIGNIFIER, " ")
                .replace(EventConstants.WITH, " ")
                .replace(EventConstants.FOR, " ").split(" ");
        Long timestamp = Utils.toMilliseconds(s[0]);
        if (!Utils.isHero(s[1]) || !Utils.isHero(s[2]))
            return null;

        String heroName = Utils.getHeroName(s[1]);
        String target = Utils.getHeroName(s[2]);
        String hitsBy = s[3];
        String spellName = Utils.getSpellName(s[3], heroName);
        Integer damageAmount = Integer.parseInt(s[4]);

        DamageEvent damageEvent = DamageEvent.builder()
                .heroName(heroName)
                .target(target)
                .hitBy(hitsBy)
                .damageAmount(damageAmount)
                .timestamp(timestamp)
                .spellName(spellName)
                .build();

        return damageEvent;
    }

}
