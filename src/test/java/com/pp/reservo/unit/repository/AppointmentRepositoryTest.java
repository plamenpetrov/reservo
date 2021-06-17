package com.pp.reservo.unit.repository;

import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.unit.factories.AppointmentFactory;
import org.junit.jupiter.api.BeforeEach;
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
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private AppointmentFactory appointmentFactory;

    @BeforeEach
    public void init() {
        this.appointmentFactory = new AppointmentFactory();
    }

    @Test
    public void findAllAppointments() {
        appointmentRepository.save(appointmentFactory.create());
        assertEquals(1, appointmentRepository.findAll().size());
    }

    @Test
    public void findAppointmentById() {
        Appointment appointmentDB = appointmentRepository.save(appointmentFactory.create());

        Appointment appointment = appointmentRepository.getById(appointmentDB.getId());
        assertNotNull(appointment);
    }

    @Test
    public void createAppointment() {
        Appointment appointment = appointmentRepository.save(appointmentFactory.create());

        Appointment appointmentDB = appointmentRepository.save(appointment);

        assertEquals(appointment.getName(), appointmentDB.getName());
    }

    @Test
    public void deleteAppointment() {
        Appointment appointment = appointmentRepository.save(appointmentFactory.create());

        appointmentRepository.deleteById(appointment.getId());

        Optional<Appointment> appointmentDB = appointmentRepository.findById(appointment.getId());

        assertThat(appointmentDB.isEmpty());
    }
}
