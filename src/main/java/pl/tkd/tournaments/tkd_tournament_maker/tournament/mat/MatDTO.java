package pl.tkd.tournaments.tkd_tournament_maker.tournament.mat;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentTableDTO;

import java.util.List;

@Data
public class MatDTO {
    private Long id;
    private Long number;
    private TournamentTableDTO tournament;
    private RefereeDTO matLeader;
    private List<RefereeDTO> Referees;
    private List<Long> categoryQueque;
}
