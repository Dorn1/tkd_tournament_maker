package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.Category;

@Setter
@Getter
@Entity
public class PlaceWrapper {
    @Id
    private Long id;

    @ManyToOne
    private Competitor competitor;

    @ManyToOne
    private Category category;

    private int place;
}
