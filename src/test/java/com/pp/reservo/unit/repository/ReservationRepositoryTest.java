package com.pp.reservo.unit.repository;

import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.ReservationRepository;
import com.pp.reservo.unit.factories.ReservationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
//@ActiveProfiles("test")
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void findAllReservations() {
        reservationRepository.saveAll(Arrays.asList(
                ReservationFactory.create(),
                ReservationFactory.create()
            )
        );

        assertEquals(2, reservationRepository.findAll().size());
    }

    @Test
    public void findReservationById() {
        Reservation reservation = reservationRepository.save(ReservationFactory.create());

        Reservation reservationDB = reservationRepository.getById(reservation.getId());
        assertNotNull(reservationDB);
    }

    @Test
    public void createReservation() {
        Reservation reservation = ReservationFactory.create();

        Reservation reservationDB = reservationRepository.save(reservation);

        assertEquals(reservation.getDuration(), reservationDB.getDuration());
        assertEquals(reservation.getStartAt(), reservationDB.getStartAt());
    }

    @Test
    public void deleteReservation() {
        Reservation reservationObj = ReservationFactory.create();

        Reservation reservation = reservationRepository.save(reservationObj);

        reservationRepository.deleteById(reservation.getId());

        Optional<Reservation> reservationDB = reservationRepository.findById(reservation.getId());

        assertThat(reservationDB.isEmpty());
    }
}
