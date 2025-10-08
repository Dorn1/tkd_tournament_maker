package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import lombok.Data;

@Data
public class ClubDTO {
    private Long id;
    private String username;
    private String password;
    private boolean admin;
}
