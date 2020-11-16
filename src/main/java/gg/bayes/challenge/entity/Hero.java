package gg.bayes.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hero {

    @Id
    @SequenceGenerator(name="hero_id_seq", sequenceName="hero_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="hero_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Match match;

    @Column
    @NotNull
    private String heroName;

    @Column
    private Integer killCount = 0;
}
