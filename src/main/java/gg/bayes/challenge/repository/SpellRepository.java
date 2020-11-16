package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Spell;
import gg.bayes.challenge.entity.SpellCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpellRepository extends JpaRepository <Spell, Long> {
    @Query(value = "SELECT spell_name as spellName, COUNT(spell_name) as spellCount FROM spell " +
            "WHERE match_id = ?1 AND hero_name = ?2 " +
            "GROUP BY spell_name", nativeQuery = true)
    List<SpellCount> findAllSpellCountByMatchIdandHeroName(Long matchId, String heroName);
}
