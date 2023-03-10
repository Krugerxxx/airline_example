package app.entities.search;

import app.entities.Flight;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "search_results")
@Data
public class SearchResult {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "depart_flights",
            joinColumns = @JoinColumn(name = "search_result_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    @NotNull
    private List<Flight> departFlight;

    @ManyToMany
    @JoinTable(
            name = "return_flights",
            joinColumns = @JoinColumn(name = "search_result_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    private List<Flight> returnFlight;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Search search;
}
