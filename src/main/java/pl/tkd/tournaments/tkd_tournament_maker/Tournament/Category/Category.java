package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;

@Entity
@Getter
@Setter
public abstract class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private Set<Competitor> competitors;

    public void filter_competitors(ICategoryFilter filter,
                                   Set<Competitor> allCompetitors){
        competitors = filter.filter(allCompetitors);
    }
}
