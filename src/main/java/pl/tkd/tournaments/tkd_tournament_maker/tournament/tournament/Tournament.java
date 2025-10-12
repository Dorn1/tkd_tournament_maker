package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.Mat;

import java.time.LocalDate;
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


    @ManyToOne
    private Club organizerClub;
    @ManyToMany
    Set<Club> clubs;
    @ManyToMany
    Set<Competitor> competitors;
    @ManyToMany
    Set<Referee> referees;
    @OneToMany
    Set<Mat> mats;

    @Builder
    public Tournament(String name, String location, Date startDate, Date endDate, Club organizer_Club) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizerClub = organizer_Club;
    }

}
