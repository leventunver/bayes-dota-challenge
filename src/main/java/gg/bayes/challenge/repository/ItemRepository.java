package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository <Item, Long> {

    List<Item> findAllByMatchIdAndHeroName(Long matchId, String heroName);
}
