package app.entities;

import app.enums.Airport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destination")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destination")
    @SequenceGenerator(name = "seq_destination", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "airport_code")
    @Enumerated(EnumType.STRING)
    private Airport airportCode;

    @Column(name = "airport_name")
    @NotBlank
    @Size(min = 3, max = 15)
    private String airportName;

    @Column(name = "city_name")
    @NotBlank
    @Size(min = 3, max = 15)
    private String cityName;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "country_name")
    @NotBlank
    @Size(min = 3, max = 30)
    private String countryName;
}
