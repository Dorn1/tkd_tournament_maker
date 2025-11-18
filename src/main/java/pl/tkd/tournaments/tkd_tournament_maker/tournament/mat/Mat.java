package pl.tkd.tournaments.tkd_tournament_maker.tournament.mat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.List;

@Getter
@Setter
@Entity
public class Mat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;
    @ManyToOne
    private Tournament tournament;
    @ElementCollection
    private List<Long> categoryQueque;
    @ManyToOne
    private Referee matLeader;
    @ManyToMany
    private List<Referee> referees;
}
