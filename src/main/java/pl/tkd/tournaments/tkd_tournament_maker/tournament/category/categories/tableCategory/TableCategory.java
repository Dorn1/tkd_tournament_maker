package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TableCategory extends Category {
    @OneToMany
    private Set<TableData> scores;

    @OneToMany
    private Set<TableData> rematches = new HashSet<>();
}
