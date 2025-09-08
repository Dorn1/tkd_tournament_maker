package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;

import java.util.*;

@Entity
@Getter
@Setter

public class LadderCategory extends Category {

    @OneToMany
    private Set<Fight> fights = new HashSet<>();

    @OneToOne
    private Fight firstPlaceFight;

    @OneToOne
    private Fight thridPlaceFight;
    
}
