package pl.tkd.tournaments.tkd_tournament_maker.security.authentication;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeClass;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.AuthenticationRequest;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.AuthenticationResponse;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.RegisterRequest;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.jwt.JwtService;

import java.util.InputMismatchException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final RefereeRepository refereeRepository;
    private final CompetitorRepository competitorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final String ROLE = "role";
    private static final String NAME = "name";
    private static final String ADMIN = "amin";
    private static final String REFEREE_CLASS = "referee_class";
    private static final String LASTNAME = "lastName";
    private static final String BELT = "belt";
    private static final String SEX = "sex";
    private static final String BIRTHYEAR = "birthYear";
    private static final String WEIGHT = "weight";
    private static final String CLUBID = "clubId";
    private static final String REQUIRED_ARGUMENTS_MISSING_MESSAGE = "clubName";
    private static final String NON_EXISTING_CLUB_MESSAGE = "provided non existing Club";

    @Value("${first.user.name}")
    private String firstUserName;
    @Value("${first.user.password}")
    private String firstUserPassword;

    @PostConstruct
    private void init() {
        if (clubRepository.count() == 0) {
            Club firstUser = Club.builder()
                    .admin(true)
                    .password(passwordEncoder.encode(firstUserPassword))
                    .userName(firstUserName)
                    .role(Role.valueOf("CLUB"))
                    .build();
            clubRepository.save(firstUser);
            log.info("Club {} created", firstUserName);
        }
    }

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            if (!StringUtils.hasText(request.getPassword()) ||
                    !StringUtils.hasText(request.getUserName()))
                throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

            Map<String, String> variables = request.getVariables();
            String role = variables.get(ROLE);
            switch (role) {
                case "CLUB":
                    Club club = Club.builder()
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .admin(false)
                            .userName(request.getUserName())
                            .build();
                    clubRepository.save(club);
                    String clubToken = jwtService.generateToken(club);
                    log.info("Club {} registered", club.getUsername());
                    return AuthenticationResponse
                            .builder()
                            .token(clubToken)
                            .role(role)
                            .build();

                case "COMPETITOR":
                    if (!StringUtils.hasText(variables.get(NAME)) ||
                            !StringUtils.hasText(variables.get(LASTNAME)) ||
                            !StringUtils.hasText(variables.get(BELT)) ||
                            !StringUtils.hasText(variables.get(SEX)) ||
                            !StringUtils.hasText(variables.get(BIRTHYEAR)) ||
                            !StringUtils.hasText(variables.get(CLUBID)) ||
                            !StringUtils.hasText(variables.get(WEIGHT))
                    )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club competitorClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));

                    Competitor competitor = Competitor.builder()
                            .userName(request.getUserName())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .belt(Integer.valueOf(variables.get(BELT)))
                            .sex(Sex.valueOf(variables.get(SEX)))
                            .birthYear(Long.valueOf(variables.get(BIRTHYEAR)))
                            .firstName(variables.get(NAME))
                            .lastName(variables.get(LASTNAME))
                            .weight(Double.valueOf(variables.get(WEIGHT)))
                            .club(competitorClub)
                            .build();
                    competitorRepository.save(competitor);
                    String competitorToken = jwtService.generateToken(competitor);
                    log.info("Competitor {} registered", competitor.getUsername());
                    return AuthenticationResponse
                            .builder()
                            .token(competitorToken)
                            .role(role)
                            .build();

                case "REFEREE":
                    if (!StringUtils.hasText(variables.get(NAME)) ||
                            !StringUtils.hasText(variables.get(LASTNAME)) ||
                            !StringUtils.hasText(variables.get(CLUBID))
                    )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club refereeClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));
                    Referee referee = Referee.builder()
                            .userName(request.getUserName())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .firstName(variables.get(NAME))
                            .lastName(variables.get(LASTNAME))
                            .club(refereeClub)
                            .refereeClass(RefereeClass.valueOf(variables.get(REFEREE_CLASS)))
                            .build();
                    refereeRepository.save(referee);
                    String refereeToken = jwtService.generateToken(referee);
                    log.info("Referee {} registered", referee.getUsername());
                    return AuthenticationResponse
                            .builder()
                            .token(refereeToken)
                            .role(role)
                            .build();

                default:
                    throw new IllegalArgumentException("Invalid role");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return AuthenticationResponse.builder().build();
        }


    }

    public AuthenticationResponse update(RegisterRequest request) {
        try {
            if (!StringUtils.hasText(request.getPassword()) ||
                    !StringUtils.hasText(request.getUserName()))
                throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

            Map<String, String> variables = request.getVariables();
            String role = variables.get(ROLE);
            switch (role) {
                case "CLUB":
                    Club updatedClub = clubRepository.findByUsername(request.getUserName());
                    updatedClub.setPassword(passwordEncoder.encode(request.getPassword()));
                    updatedClub.setAdmin(Boolean.parseBoolean(request.getVariables().get(ADMIN)));
                    updatedClub.setUsername(request.getUserName());
                    clubRepository.save(updatedClub);

                    String clubToken = jwtService.generateToken(updatedClub);

                    log.info("Club {} updated", updatedClub.getUsername());

                    return AuthenticationResponse
                            .builder()
                            .token(clubToken)
                            .role(role)
                            .build();

                case "COMPETITOR":
                    if (!StringUtils.hasText(variables.get(NAME)) ||
                            !StringUtils.hasText(variables.get(LASTNAME)) ||
                            !StringUtils.hasText(variables.get(BELT)) ||
                            !StringUtils.hasText(variables.get(SEX)) ||
                            !StringUtils.hasText(variables.get(BIRTHYEAR)) ||
                            !StringUtils.hasText(variables.get(CLUBID)) ||
                            !StringUtils.hasText(variables.get(WEIGHT))
                    )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club competitorClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));

                    Competitor updatedCompetitor = competitorRepository.findByUsername(request.getUserName());
                    updatedCompetitor.setPassword(passwordEncoder.encode(request.getPassword()));
                    updatedCompetitor.setBelt(Integer.valueOf(variables.get(BELT)));
                    updatedCompetitor.setBirthYear(Long.valueOf(variables.get(BIRTHYEAR)));
                    updatedCompetitor.setFirstName(variables.get(NAME));
                    updatedCompetitor.setLastName(variables.get(LASTNAME));
                    updatedCompetitor.setWeight(Double.valueOf(variables.get(WEIGHT)));
                    competitorRepository.save(updatedCompetitor);
                    String competitorToken = jwtService.generateToken(updatedCompetitor);
                    log.info("Competitor {} updated", updatedCompetitor.getUsername());
                    return AuthenticationResponse
                            .builder()
                            .token(competitorToken)
                            .role(role)
                            .build();

                case "REFEREE":
                    if (!StringUtils.hasText(variables.get(NAME)) ||
                            !StringUtils.hasText(variables.get(LASTNAME)) ||
                            !StringUtils.hasText(variables.get(CLUBID))
                    )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club refereeClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));

                    Referee updatedReferee = refereeRepository.findByUsername(request.getUserName());
                    updatedReferee.setPassword(passwordEncoder.encode(request.getPassword()));
                    updatedReferee.setFirstName(variables.get(NAME));
                    updatedReferee.setLastName(variables.get(LASTNAME));
                    updatedReferee.setRefereeClass(RefereeClass.valueOf(variables.get(REFEREE_CLASS)));;

                    refereeRepository.save(updatedReferee);
                    String refereeToken = jwtService.generateToken(updatedReferee);
                    log.info("Referee {} updated", updatedReferee.getUsername());
                    return AuthenticationResponse
                            .builder()
                            .token(refereeToken)
                            .role(role)
                            .build();

                default:
                    throw new IllegalArgumentException("Invalid role");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return AuthenticationResponse.builder().build();
        }


    }

    public void deleteUser(String userName){
        if (userRepository.findByUsername(userName).isPresent())
            userRepository.delete(userRepository.findByUsername(userName).get());
        else
            throw new IllegalArgumentException("User not found");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()
                    )
            );
            User user = userRepository.findByUsername(authenticationRequest.getUserName()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            log.info("User {} authenticated", user.getUsername());
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .role(String.valueOf(user.getRole()))
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder().build();
        }

    }

    public Long getUserId(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getId();
    }
    public String getUserType(String username) {
        User user = userRepository.findById(getUserId(username)).orElseThrow();
        return user.getRole().toString();
    }
    public boolean isAdmin(String username){
        Club club = clubRepository.findByUsername(username);
        return  club.isAdmin();
    }
}
