package gg.bayes.challenge.parser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DamageEvent {

    String heroName;
    String hitBy;
    String target;
    Integer damageAmount;
    Long timestamp;
    String spellName;

}
