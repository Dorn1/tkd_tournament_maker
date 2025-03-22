package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Belt;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;
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



    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public void addClub(String name) {
        Club c = new Club();
        c.setName(name);
        clubRepository.save(c);
    }

    public void addCompetitorToClub(String firstname,
                                    String lastName,
                                    boolean male,
                                    Long birthDate,
                                    Long clubId) throws ObjectNotFoundException {
        Sex competitorSex = Sex.Female;
        if(male) competitorSex = Sex.Male;

        Club club = getClub(clubId);
        Competitor newCompetitor = new Competitor();
        newCompetitor.setFirstName(firstname);
        newCompetitor.setLastName(lastName);
        newCompetitor.setSex(competitorSex);
        newCompetitor.setBirthDate(new Date(birthDate));
        newCompetitor.setClub(club);
        newCompetitor.setBelt(Belt.WHITE);
        club.getCompetitors().add(newCompetitor);
        competitorRepository.save(newCompetitor);
        clubRepository.save(club);




    }

    public void addRefereeToClub(String firstname,
                                    String lastName,
                                    Long clubId) throws ObjectNotFoundException {

        Club club = getClub(clubId);
        Referee newReferee = new Referee();
        newReferee.setFirstName(firstname);
        newReferee.setLastName(lastName);
        newReferee.setClub(club);
        refereeRepository.save(newReferee);
        clubRepository.save(club);


    }

    public Club getClub(Long id) throws ObjectNotFoundException {
        if (clubRepository.findById(id).isPresent())
            return clubRepository.findById(id).get();
        throw new ObjectNotFoundException("Club doesn't exist");
    }
}
