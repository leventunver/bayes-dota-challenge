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
public class Kill {

    @Id
    @SequenceGenerator(name="hero_kill_id_seq", sequenceName="hero_kill_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="hero_kill_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Match match;

    @Column
    @NotNull
    private String heroName;

    @Column
    @NotNull
    private String target;

    @Column
    private Long timestamp;

}
