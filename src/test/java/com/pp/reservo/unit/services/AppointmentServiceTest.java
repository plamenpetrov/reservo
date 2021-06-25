package com.pp.reservo.unit.services;

import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.events.publishers.AppointmentEventPublisher;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.domain.repositories.specification.AppointmentSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.implementations.AppointmentServiceImpl;
import com.pp.reservo.unit.factories.AppointmentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.pp.reservo.domain.common.Domain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentEventPublisher appointmentEventPublisher;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private List<Appointment> appointmentEntities;

    @BeforeEach
    public void initService() {
        appointmentService = new AppointmentServiceImpl(modelMapper, appointmentRepository, appointmentEventPublisher);
        appointmentEntities = initAppointments();
    }

    private List<Appointment> initAppointments() {
        List<Appointment> appointmentEntities = new ArrayList<>();
        appointmentEntities.add(AppointmentFactory.create());
        appointmentEntities.add(AppointmentFactory.create());

        return appointmentEntities;
    }
    
    @ParameterizedTest
    @MethodSource("allAppointmentsDataProvider")
    public void should_return_all_appointments_without_filters(String byName, Integer fromDuration, Integer toDuration, Integer page, String sortBy, Integer expected) {
        testGetAppointments(byName, fromDuration, toDuration, page, sortBy, expected);
    }

    @Test
    public void should_return_appointments_filtered_by_name() {
        Appointment appointment = appointmentEntities.get(0);
        String byName = appointment.getName();
        appointmentEntities.remove(0);

        testGetAppointments(byName, 0, 0, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 1);
    }

    @Test
    public void should_not_return_appointments_filtered_by_name() {
        appointmentEntities.clear();
        testGetAppointments("testAppointment", 0, 0, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 0);
    }

    private void testGetAppointments(String byName, Integer fromDuration, Integer toDuration, Integer page, String sortBy, Integer expected) {
        int size = PAGE_SIZE;

        Page<Appointment> paginatedAppointments = new PageImpl(appointmentEntities);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        AppointmentSpecification appointmentSpecification = new AppointmentSpecification(byName, fromDuration, toDuration);

        AppointmentServiceImpl appointmentServiceSpy = Mockito.spy(appointmentService);

        doReturn(appointmentSpecification)
                .when(appointmentServiceSpy)
                .getSpecificationInstance(byName, fromDuration, toDuration);

        when(appointmentRepository.findAll(appointmentSpecification, pageable))
                .thenReturn(paginatedAppointments);

        List<AppointmentDTO> appointmentsDB = appointmentServiceSpy.getAllAppointments(byName, fromDuration, toDuration, page, sortBy);

        assertEquals(expected, appointmentsDB.size());
    }

    public static Stream<Arguments> allAppointmentsDataProvider() {
        return Stream.of(
                Arguments.of("", 0, 0, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 2),
                Arguments.of("", 0, 0, DEFAULT_PAGE, "name", 2),
                Arguments.of("", 0, 0, 2, "name", 2)
        );
    }

    @Test
    public void should_find_existing_appointment() throws EntityNotFoundException {
        Appointment appointment = AppointmentFactory.create();
        Integer appointmentId = appointment.getId();

        AppointmentServiceImpl appointmentServiceSpy = Mockito.spy(appointmentService);

        doReturn(Optional.of(appointment))
                .when(appointmentServiceSpy)
                .findById(appointmentId);

        AppointmentDTO appointmentsDB = appointmentServiceSpy.getAppointmentById(appointmentId);

        assertEquals(appointment.getName(), appointmentsDB.getName());
        assertEquals(appointment.getId(), appointmentsDB.getId());
    }

    @Test
    public void should_store_new_appointment() {
        Appointment appointment = new Appointment();
        appointment.setName(appointment.getName());

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setName(appointment.getName());

        AppointmentServiceImpl appointmentServiceSpy = Mockito.spy(appointmentService);

        doReturn(appointment)
                .when(appointmentRepository)
                .saveAndFlush(appointment);

        AppointmentDTO storedAppointment = appointmentServiceSpy.storeAppointment(appointmentDTO);

        assertEquals(appointmentDTO, storedAppointment);
    }

    @Test
    public void should_delete_existing_appointment() {
        Appointment appointment = appointmentEntities.get(0);
        Integer appointmentId = appointment.getId();

        when(appointmentRepository.existsById(appointmentId))
                .thenReturn(true);

        AppointmentDTO appointmentDTO = this.modelMapper.map(appointment, AppointmentDTO.class);

        AppointmentServiceImpl appointmentServiceSpy = Mockito.spy(appointmentService);

        doReturn(appointmentDTO)
                .when(appointmentServiceSpy)
                .getAppointmentById(appointmentId);

        appointmentServiceSpy.deleteAppointment(appointmentId);

        Mockito.verify(appointmentRepository, Mockito.times(1)).deleteById(appointmentId);
    }
}
