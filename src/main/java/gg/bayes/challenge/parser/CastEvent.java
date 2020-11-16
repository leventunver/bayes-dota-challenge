package gg.bayes.challenge.parser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CastEvent {

    String heroName;
    String spellName;
    Integer level;
    String target;
    Long timestamp;

}
