package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

@Setter
@Getter
@Entity
public class TableData {
    @Id
    private Long id;

    @ManyToOne
    private Competitor competitor;

    private Double score;
}
