package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;

import java.util.Date;

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

    @Setter
    @Getter
    @Entity
    public static class Mat {
        @Id
        private Long id;
        private Long onTournamentNumber;
        @ManyToOne
        @JoinColumn(name = "tournament_id")
        private Tournament tournament;
    }
}
