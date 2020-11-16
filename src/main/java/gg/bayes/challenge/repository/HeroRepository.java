package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroRepository extends JpaRepository <Hero, Long> {

    @Modifying
    @Query(value = "UPDATE hero SET kill_count = kill_count + 1 "
            + " WHERE match_id = ?1 AND hero_name =?2", nativeQuery = true)
    void incrementKillCount(Long matchId, String heroName);

    List<Hero> findAllByMatchId(Long matchId);
}
