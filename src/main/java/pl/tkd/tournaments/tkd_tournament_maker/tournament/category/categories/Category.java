package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;

import java.util.Set;

@Entity
@Getter
@Inheritance
@Setter
public abstract class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private Set<Competitor> competitors;


}
