package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Rematch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private Set<TableData> tables;
}
