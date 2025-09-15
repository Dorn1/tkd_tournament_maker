package pl.tkd.tournaments.tkd_tournament_maker.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;
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
    private static final String LASTNAME = "lastName";
    private static final String BELT = "belt";
    private static final String SEX = "sex";
    private static final String BIRTHYEAR = "birthYear";
    private static final String WEIGHT = "weight";
    private static final String CLUBID = "clubId";
    private static final String REQUIRED_ARGUMENTS_MISSING_MESSAGE = "clubName";
    private static final String NON_EXISTING_CLUB_MESSAGE = "provided non existing Club";

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            if (!StringUtils.hasText(request.getPassword())||
                !StringUtils.hasText(request.getEmail()))
                throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

            Map<String, String> variables = request.getVariables();
            String role = variables.get(ROLE);
            switch (role) {
                case "CLUB":
                    if (!StringUtils.hasText(variables.get(NAME)))
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club club = Club.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .admin(false)
                            .name(variables.get(NAME))
                            .build();
                    clubRepository.save(club);
                    String clubToken = jwtService.generateToken(club);
                    return AuthenticationResponse
                            .builder()
                            .token(clubToken)
                            .success(true)
                            .build();

                case "COMPETITOR":
                    if (!StringUtils.hasText(variables.get(NAME))||
                        !StringUtils.hasText(variables.get(LASTNAME))||
                        !StringUtils.hasText(variables.get(BELT))||
                        !StringUtils.hasText(variables.get(SEX))||
                        !StringUtils.hasText(variables.get(BIRTHYEAR))||
                        !StringUtils.hasText(variables.get(CLUBID))||
                        !StringUtils.hasText(variables.get(WEIGHT))
                        )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club competitorClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));

                    Competitor competitor = Competitor.builder()
                            .email(request.getEmail())
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
                    return AuthenticationResponse
                            .builder()
                            .token(competitorToken)
                            .success(true)
                            .build();

                case "REFEREE":
                    if (!StringUtils.hasText(variables.get(NAME))||
                            !StringUtils.hasText(variables.get(LASTNAME))||
                            !StringUtils.hasText(variables.get(CLUBID))
                    )
                        throw new IllegalArgumentException(REQUIRED_ARGUMENTS_MISSING_MESSAGE);

                    Club refereeClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException(NON_EXISTING_CLUB_MESSAGE));
                    Referee referee = Referee.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .firstName(variables.get(NAME))
                            .lastName(variables.get(LASTNAME))
                            .club(refereeClub)
                            .build();
                    refereeRepository.save(referee);
                    String refereeToken = jwtService.generateToken(referee);
                    return AuthenticationResponse
                            .builder()
                            .token(refereeToken)
                            .success(true)
                            .build();

                default:
                    throw new IllegalArgumentException("Invalid role");
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
            return AuthenticationResponse.builder().success(false).build();
        }


    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .role(String.valueOf(user.getRole()))
                    .success(true)
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder().success(false).build();
        }

    }
}
