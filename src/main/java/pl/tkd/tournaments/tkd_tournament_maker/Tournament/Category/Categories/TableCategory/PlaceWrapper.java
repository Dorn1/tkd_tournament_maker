package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.Category;

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
