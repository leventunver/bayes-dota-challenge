package gg.bayes.challenge.parser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurchaseEvent {

    String heroName;
    String itemName;
    Long timestamp;

}
