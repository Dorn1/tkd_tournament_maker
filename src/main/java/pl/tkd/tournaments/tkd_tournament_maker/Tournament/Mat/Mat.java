package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Category;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@Getter
@Setter
@Entity
public class Mat {
    @Id
    private Long id;
    @ManyToOne
    private Tournament tournament;
    @OneToMany
    private Set<Category> categoryQueque;
}
