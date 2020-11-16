package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Damage;
import gg.bayes.challenge.entity.DamageCount;
import gg.bayes.challenge.entity.SpellCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DamageRepository extends JpaRepository <Damage, Long> {
    @Query(value = "SELECT target, COUNT(target) as damageCount, SUM(damage_amount) as damageSum FROM damage " +
            "WHERE match_id = ?1 AND hero_name = ?2 " +
            "GROUP BY target", nativeQuery = true)
    List<DamageCount> findAllDamageCountAndSumByMatchIdAndHeroName(Long matchId, String heroName);
}
