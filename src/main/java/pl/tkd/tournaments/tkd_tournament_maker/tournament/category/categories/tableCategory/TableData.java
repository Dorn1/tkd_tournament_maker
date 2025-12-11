package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;

import java.util.Set;

@Data
@Entity
public class TableData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Competitor competitor;

    private Long score;

    private Boolean checked;

}
