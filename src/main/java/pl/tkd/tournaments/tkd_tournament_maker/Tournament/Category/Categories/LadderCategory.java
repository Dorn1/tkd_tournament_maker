package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Fight;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LadderCategory extends Category {

    @OneToMany
    private Set<Fight> fights = new HashSet<>();

    @OneToOne
    private Fight firstPlaceFight;

    @OneToOne
    private Fight thridPlaceFight;
    
}
