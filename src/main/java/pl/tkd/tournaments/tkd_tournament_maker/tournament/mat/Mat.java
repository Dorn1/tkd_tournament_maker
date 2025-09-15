package pl.tkd.tournaments.tkd_tournament_maker.tournament.mat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
    @ManyToOne
    private Tournament tournament;
    @OneToMany
    private List<Category> categoryQueque;
    @ManyToOne
    private Referee matLeader;
}
