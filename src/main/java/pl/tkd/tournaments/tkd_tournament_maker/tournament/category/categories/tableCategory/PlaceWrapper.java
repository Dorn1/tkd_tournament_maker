package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;

@Setter
@Getter
@Entity
public class PlaceWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Competitor competitor;

    @ManyToOne
    private Category category;

    private int place;
}
