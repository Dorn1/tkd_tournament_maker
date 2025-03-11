package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import jakarta.persistence.*;
import lombok.Getter;
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
public class Tournament {
    @Id
    private Long id;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "organizer_club_id")
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
