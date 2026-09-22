//package com.eventify.config;
//import com.eventify.model.EventsModel;
//import com.eventify.model.VenueModel;
//import com.eventify.repository.VenueRepository;
//import com.eventify.repository.EventsRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import java.time.LocalDate;
//
//@Configuration
//public class DataSeedersConfig {
// @Bean
//    CommandLineRunner seedVenues(VenueRepository venueRepository) {return args -> {
//     venueRepository.save(new VenueModel(1L, "Teatro amira de la rosa", "Barranquilla", 500));
//     venueRepository.save(new VenueModel(2L, "Centro de eventos puerta de oro", "Barranquilla", 1200));
//     venueRepository.save(new VenueModel(3L, "Coliseo elias chegwin", "Barranquilla", 3000));
//    };
// }
//
// @Bean
// CommandLineRunner seedEvents(EventsRepository eventsRepository) {return  args -> {
//     eventsRepository.save(new EventsModel(1L,"Conferencia spring boot", LocalDate.of(2026, 10, 15), "Evento tecnico sobre desarrollo de backend"));
//     eventsRepository.save(new EventsModel(2L, "Meetup de java", LocalDate.of(2026,10,15),"Networking para desarrolladores"));
//     eventsRepository.save(new EventsModel(3L, "Hackathon backend", LocalDate.of(2026, 11, 15),"Competencia de programacion de 24 horas"));
//    };
// }
//}
