package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

@Getter
@Setter
@Entity
public class Mat {
    @Id
    private Long id;
    @ManyToOne
    private Tournament tournament;
}
