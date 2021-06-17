package com.pp.reservo.unit.repository;

import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.ReservationRepository;
import com.pp.reservo.unit.factories.ReservationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationFactory reservationFactory;

    @Test
    public void findAllReservations() {
        reservationRepository.save(reservationFactory.create());
        assertEquals(1, reservationRepository.findAll().size());
    }

    @Test
    public void findReservationById() {
        Reservation reservation = reservationRepository.save(reservationFactory.create());

        Reservation reservationDB = reservationRepository.getById(reservation.getId());
        assertNotNull(reservationDB);
    }

    @Test
    public void createReservation() {
        Reservation reservation = reservationFactory.create();

        Reservation reservationDB = reservationRepository.save(reservation);

        assertEquals(reservation.getAppointment().getName(), reservationDB.getAppointment().getName());
        assertEquals(reservation.getClient().getName(), reservationDB.getClient().getName());
        assertEquals(reservation.getEmployee().getName(), reservationDB.getEmployee().getName());
    }

    @Test
    public void deleteReservation() {
        Reservation reservation = reservationRepository.save(reservationFactory.create());

        reservationRepository.deleteById(reservation.getId());

        Optional<Reservation> reservationDB = reservationRepository.findById(reservation.getId());

        assertThat(reservationDB.isEmpty());
    }
}
