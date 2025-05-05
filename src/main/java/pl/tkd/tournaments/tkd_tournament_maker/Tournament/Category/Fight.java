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
    @OneToMany
    private Set<Fight> observers = new HashSet<>();
    @OneToOne
    private Fight thirdPlaceObserver = null;

    public void addObserver(Fight observer) {
        observers.add(observer);
    }

    public void removeObserver(Fight observer) {
        observers.remove(observer);
    }

    public void updateObservers() throws IllegalAccessException {
        for (Fight observer : observers) {
            if (observer.competitor1 !=null && observer.competitor2 != null) {
                throw new IllegalAccessException("next Fight already has 2 competitors set");
            }
            if (observer.competitor1 != null) {
                observer.setCompetitor2(competitor2);
            }
            else {
                observer.setCompetitor1(competitor1);
            }
        }
        if (thirdPlaceObserver != null) {
            if (thirdPlaceObserver.competitor1 ==null) {
                thirdPlaceObserver.setCompetitor1(competitor1);
            } else if (thirdPlaceObserver.competitor2 == null) {
                thirdPlaceObserver.setCompetitor2(competitor2);
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

}
