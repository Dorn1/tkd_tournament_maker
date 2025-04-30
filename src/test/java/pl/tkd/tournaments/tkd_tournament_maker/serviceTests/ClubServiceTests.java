package pl.tkd.tournaments.tkd_tournament_maker.serviceTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClubServiceTests {
    @Mock
    ClubRepository clubRepository;
    @Mock
    CompetitorRepository competitorRepository;
    @Mock
    RefereeRepository refereeRepository;
    @InjectMocks
    ClubService clubService;

    @Test
    public void CompetitorAddTest(){
        Club club = new Club();
        club.setName("TkdFreaks");
        club.setId(1L);
        club.setCompetitors(new HashSet<>());
        Calendar goodAge = new GregorianCalendar(2010, Calendar.MAY, 14);
        given(clubRepository.findById(1L)).willReturn(Optional.of(club));
        Assertions.assertDoesNotThrow(
                () -> {
                    clubService.addCompetitorToClub("John",
                            "Smith",
                            true,
                            goodAge.getTimeInMillis(),
                            1L);
                }
        );

        Assertions.assertThrowsExactly(ObjectNotFoundException.class,
                () -> {
                    clubService.addCompetitorToClub("John",
                            "Smith",
                            true,
                            goodAge.getTimeInMillis(),
                            0L);
                }
        );
    }
}
