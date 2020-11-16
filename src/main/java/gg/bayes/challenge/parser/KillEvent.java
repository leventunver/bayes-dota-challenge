package gg.bayes.challenge.parser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KillEvent {

    String heroName;
    String target;
    Long timestamp;

}
