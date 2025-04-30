package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;
    private final RefereeRepository refereeRepository;


    @Autowired
    public ClubService(ClubRepository clubRepository,
                       CompetitorRepository competitorRepository,
                       RefereeRepository refereeRepository) {
        this.clubRepository = clubRepository;
        this.competitorRepository = competitorRepository;
        this.refereeRepository = refereeRepository;
    }


    public void addClub(String name) {
        Club c = new Club();
        c.setName(name);
        clubRepository.save(c);
    }

    public void addCompetitorToClub(String firstname,
                                    String lastName,
                                    boolean male,
                                    Long birthYear,
                                    Long clubId) throws ObjectNotFoundException {
        Sex competitorSex = Sex.Female;
        if (male) competitorSex = Sex.Male;

        Club club = getClubById(clubId);
        Competitor newCompetitor = new Competitor();
        newCompetitor.setFirstName(firstname);
        newCompetitor.setLastName(lastName);
        newCompetitor.setSex(competitorSex);
        newCompetitor.setBirthYear(birthYear);
        newCompetitor.setClub(club);
        newCompetitor.setBelt(1);
        club.getCompetitors().add(newCompetitor);
        competitorRepository.save(newCompetitor);
        clubRepository.save(club);
    }

    public void addRefereeToClub(String firstname,
                                 String lastName,
                                 Long clubId) throws ObjectNotFoundException {

        Club club = getClubById(clubId);
        Referee newReferee = new Referee();
        newReferee.setFirstName(firstname);
        newReferee.setLastName(lastName);
        newReferee.setClub(club);
        refereeRepository.save(newReferee);
        clubRepository.save(club);


    }

    public Club getClubById(Long id) throws ObjectNotFoundException {
        if (clubRepository.findById(id).isPresent())
            return clubRepository.findById(id).get();
        throw new ObjectNotFoundException("Club doesn't exist");
    }

    public List<Club> getClubByName(String name) {
        return clubRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Referee> getRefereeByName(String firstname, String lastname) {
        return refereeRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstname, lastname);
    }

    public Competitor getCompetitorById(Long id) throws ObjectNotFoundException {
        if (competitorRepository.findById(id).isPresent())
            return competitorRepository.findById(id).get();
        throw new ObjectNotFoundException("Competitor doesn't exist");
    }
}
