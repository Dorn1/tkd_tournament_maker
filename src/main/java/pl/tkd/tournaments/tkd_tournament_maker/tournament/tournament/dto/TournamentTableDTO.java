package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto;

import lombok.Data;

@Data
public class TournamentTableDTO {
    Long id;
    String name;
    String date;
    String location;
}
