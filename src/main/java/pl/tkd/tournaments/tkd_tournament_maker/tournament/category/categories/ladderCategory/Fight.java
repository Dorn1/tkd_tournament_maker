package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Competitor competitor1;
    @ManyToOne
    private Competitor competitor2;
    @ManyToOne
    private Competitor winner;
    private Long nextFightObserver;
    private Long thirdPlaceFightObserver = null;
    @ElementCollection
    private Set<Long> fightsBefore = new HashSet<>();
    @ManyToOne
    private Category category;
    private Long mainFightReferee;
    @ElementCollection
    private Set<Long> fightReferees = new HashSet<>();

    @ElementCollection
    private Set<Long> tableReferees = new HashSet<>();




}
