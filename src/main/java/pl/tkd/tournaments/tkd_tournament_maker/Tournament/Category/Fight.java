package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
@Entity
@Getter
@Setter
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Competitor competitor1;
    @ManyToOne
    private Competitor competitor2;
    @ManyToOne
    private Competitor winner;
    public void setWinner(boolean wonFirst){
        winner = wonFirst ? competitor1 : competitor2;
    }

}
