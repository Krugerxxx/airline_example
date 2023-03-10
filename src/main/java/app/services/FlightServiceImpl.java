package app.services;

import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.services.interfaces.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private FlightSeatRepository flightSeatRepository;

    public FlightServiceImpl(FlightRepository flightRepository, FlightSeatRepository flightSeatRepository) {
        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<FlightSeat> getFreeSeats(Long id) {
        var flight = getById(id);
        if (flight == null) {
            return null;
        }
        Set<FlightSeat> flightSeatSet = flightSeatRepository.findFlightSeatByFlight(flight);

        if(flightSeatSet != null) {
            flightSeatSet = flightSeatSet.stream()
                    .filter(flightSeat -> !flightSeat.getIsRegistered())
                    .filter(flightSeat -> !flightSeat.getIsSold())
                    .collect(Collectors.toSet());
        }

        //TODO раскомментировать после добавление сущностей Seat и Aircraft
        //TODO по другому сделал, эта логика тоже нужна, но по моему мнению не здесь

        //var seats = flight.getAircraft().getSeatList()
        //        .stream().filter(seat -> !seat.getIsSold()).collect(Collectors.toList());
        //Map<String, Integer> freeSeats = new HashMap<>();
        //freeSeats.put("всего свободных", seats.size());
        //freeSeats.put("эконом", (int) seats.stream()
        //        .filter(seat -> seat.getCategory().getCategoryType() == CategoryType.ECONOMY).count());
        //freeSeats.put("бизнес", (int) seats.stream()
        //        .filter(seat -> seat.getCategory().getCategoryType() == CategoryType.BUSINESS).count());
        //return freeSeats;
        //return Map.of("NO DATA", 1);

        return flightSeatSet;
    }

    @Override
    @Transactional(readOnly = true)
    public Flight getFlightByCode(String code) {
        return flightRepository.getByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightByDestinationsAndDates(String from, String to,
                                                        String start, String finish) {
        return getAllFlights().stream()
                .filter(flight -> from == null || flight.getFrom().getCityName().equals(from))
                .filter(flight -> to == null || flight.getTo().getCityName().equals(to))
                .filter(flight -> start == null || flight.getDepartureDateTime().isEqual(LocalDateTime.parse(start)))
                .filter(flight -> finish == null || flight.getArrivalDateTime().isEqual(LocalDateTime.parse(finish)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDestinationsAndDepartureDate(Destination from, Destination to,
                                                                 LocalDate departureDate) {
        return flightRepository.getByFromAndToAndDepartureDate(from, to, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Flight getFlightByIdAndDates(Long id, String start, String finish) {
        var flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            if (flight.get().getDepartureDateTime().isEqual(LocalDateTime.parse(start))
            && flight.get().getArrivalDateTime().isEqual(LocalDateTime.parse(finish))) {
                return flight.get();
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Flight getById(Long id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public void update(Flight updated) {
        flightRepository.saveAndFlush(updated);
    }
}
