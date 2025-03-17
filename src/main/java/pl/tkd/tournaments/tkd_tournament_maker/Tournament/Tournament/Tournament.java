package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.Mat;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;

    public Tournament(String name, String location, Date startDate, Date endDate, Club organizer_Club) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizer_Club = organizer_Club;
    }

    @ManyToOne
    private Club organizer_Club;
    @ManyToMany
    Set<Club> clubs;
    @ManyToMany
    Set<Competitor> competitors;
    @ManyToMany
    Set<Referee> referees;
    @OneToMany
    Set<Mat> mats;
}
