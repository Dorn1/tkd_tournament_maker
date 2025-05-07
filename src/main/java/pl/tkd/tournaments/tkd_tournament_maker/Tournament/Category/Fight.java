package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.HashSet;
import java.util.Set;

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
    @OneToOne
    private Fight nextFightObserver;
    @OneToOne
    private Fight thirdPlaceFightObserver = null;
    @OneToMany
    private Set<Fight> fightsBefore = new HashSet<>();


    public Competitor getLoser(){
        if (winner.equals(competitor1)){
            return competitor2;
        }
        return competitor1;
    }

    public void updateObservers() throws IllegalAccessException {
        if (nextFightObserver != null){
            nextFightObserver.addCompetitor(winner);
        }
        if (thirdPlaceFightObserver != null) {
            if (thirdPlaceFightObserver.competitor1 ==null) {
                thirdPlaceFightObserver.setCompetitor1(competitor1);
            } else if (thirdPlaceFightObserver.competitor2 == null) {
                thirdPlaceFightObserver.setCompetitor2(competitor2);
            }
        }
    }

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

    public void addCompetitor(Competitor competitor) throws IllegalAccessException {
        if (competitor1 == null){
            competitor1 = competitor;
        }
        else if (competitor2 == null && competitor1 != null){
            competitor2 = competitor;
        }
        else {
            throw new IllegalAccessException("added too many competitors to fight");
        }
    }

}
