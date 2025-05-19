package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.Category;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TableCategory extends Category {
    @ManyToOne
    private Competitor competitor;

    @OneToMany
    private Set<TableData> scores;

    @OneToMany
    private Set<Rematch> rematches = new HashSet<>();

    private Long WantedPlaces;

    @OneToMany
    private Set<PlaceWrapper> places;
}
