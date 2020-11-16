package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Kill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KillRepository extends JpaRepository <Kill, Long> {
}
