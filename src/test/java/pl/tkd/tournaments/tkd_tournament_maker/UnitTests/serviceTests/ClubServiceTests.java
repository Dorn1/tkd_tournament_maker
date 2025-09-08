package pl.tkd.tournaments.tkd_tournament_maker.UnitTests.serviceTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClubServiceTests {
    @Mock
    ClubRepository clubRepository;
    @Mock
    RefereeRepository refereeRepository;
    @Mock
    CompetitorRepository competitorRepository;
    @InjectMocks
    ClubService clubService;

    private Club club;

    @BeforeEach
    public void setup() {
        club = new Club();
        club.setName("TkdFreaks");
        club.setId(1L);
        club.setCompetitors(new HashSet<>());
    }

    @Test
    public void CompetitorAddTest() {

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

    @Test
    public void RefereeAddTest() {
        given(clubRepository.findById(1L)).willReturn(Optional.of(club));

        Assertions.assertDoesNotThrow(
                () -> {
                    clubService.addRefereeToClub("John", "Smith", 1L);
                }
        );

        Assertions.assertThrowsExactly(ObjectNotFoundException.class,
                () -> {
                    clubService.addRefereeToClub("John", "Smith", 0L);
                }
        );
    }
}
