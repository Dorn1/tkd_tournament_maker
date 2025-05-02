package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

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
    public void setWinner(boolean wonFirst) throws ObjectNotFoundException {
        if (competitor1 != null && competitor2 != null){
        winner = wonFirst ? competitor1 : competitor2;
        }
        else if(competitor1 != null){
            winner = competitor1;
        }
        else if(competitor2 != null){
            winner = competitor2;
        }
        else{
            throw new ObjectNotFoundException("neither competitor1 nor competitor2 is set");
        }
    }

}
