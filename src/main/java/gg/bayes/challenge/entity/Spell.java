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
public class Spell {

    @Id
    @SequenceGenerator(name="hero_spell_id_seq", sequenceName="hero_spell_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="hero_spell_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Match match;

    @Column
    @NotNull
    private String heroName;

    @Column
    private String target;

    @Column
    @NotNull
    private String spellName;

    @Column
    private Integer level;

    @Column
    private Long timestamp;

}
