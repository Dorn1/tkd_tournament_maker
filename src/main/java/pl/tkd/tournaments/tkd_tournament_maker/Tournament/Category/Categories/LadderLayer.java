package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Fight;

import java.util.Set;

@Entity
@Getter
@Setter
public class LadderLayer {
    @OneToMany
    private Set<Fight> fights;
    @Setter
    @Id
    private Long id;
}
