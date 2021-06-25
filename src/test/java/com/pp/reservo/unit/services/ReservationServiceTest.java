package com.pp.reservo.unit.services;

import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.ReservationRepository;
import com.pp.reservo.domain.repositories.specification.ReservationSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.implementations.AppointmentServiceImpl;
import com.pp.reservo.infrastructure.services.implementations.ClientServiceImpl;
import com.pp.reservo.infrastructure.services.implementations.EmployeeServiceImpl;
import com.pp.reservo.infrastructure.services.implementations.ReservationServiceImpl;
import com.pp.reservo.unit.factories.ReservationFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
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
public class ReservationServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private EmployeeServiceImpl employeeService;

    @Mock
    private AppointmentServiceImpl appointmentService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private List<Reservation> reservationEntities;

    @Autowired
    private ReservationFactory reservationFactory;
    
    @BeforeEach
    public void initService() {
        reservationService = new ReservationServiceImpl(modelMapper, 
                reservationRepository, 
                appointmentService, 
                clientService, 
                employeeService
        );
        reservationEntities = initReservations();
    }

    private List<Reservation> initReservations() {
        List<Reservation> reservationEntities = new ArrayList<>();
        reservationEntities.add(reservationFactory.create());
        reservationEntities.add(reservationFactory.create());

        return reservationEntities;
    }
    
    @ParameterizedTest
    @MethodSource("allReservationsDataProvider")
    public void should_return_all_reservations_without_filters(Date byDate,
                                                               Integer clientId,
                                                               Integer employeeId,
                                                               Integer appointmentId,
                                                               Integer page,
                                                               String sortBy,
                                                               Integer expected
    ) {
        testGetReservations(byDate, clientId, employeeId, appointmentId, page, sortBy, expected);
    }

    @Test
    public void should_return_reservations_filtered_by_date() {
        Reservation reservation = reservationEntities.get(0);
        Date byDate = reservation.getStartAt();
        reservationEntities.remove(0);

        testGetReservations(byDate, null, null, null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 1);
    }

    @Test
    public void should_not_return_reservations_filtered_by_date() {
        Reservation reservation = reservationEntities.get(0);
        reservationEntities.clear();
        testGetReservations(reservation.getStartAt(), null, null, null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 0);
    }

    private void testGetReservations(Date byDate,
                                     Integer clientId,
                                     Integer employeeId,
                                     Integer appointmentId,
                                     Integer page,
                                     String sortBy,
                                     Integer expected
    ) {
        int size = PAGE_SIZE;

        Page<Reservation> paginatedReservations = new PageImpl(reservationEntities);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        ReservationSpecification reservationSpecification = new ReservationSpecification(byDate, clientId, employeeId, appointmentId);

        ReservationServiceImpl reservationServiceSpy = Mockito.spy(reservationService);

        doReturn(reservationSpecification)
                .when(reservationServiceSpy)
                .getSpecificationInstance(byDate, clientId, employeeId, appointmentId);

        when(reservationRepository.findAll(reservationSpecification, pageable))
                .thenReturn(paginatedReservations);

        List<ReservationDTO> reservationsDB = reservationServiceSpy.getAllReservations(byDate, clientId, employeeId, appointmentId, page, sortBy);

        assertEquals(expected, reservationsDB.size());
    }

    public static Stream<Arguments> allReservationsDataProvider() {
        return Stream.of(
                Arguments.of(null, null, null, null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 2),
                Arguments.of(null, null, null, null, DEFAULT_PAGE, "start_at", 2),
                Arguments.of(null, null, null, null, 2, "start_at", 2)
        );
    }

    @Test
    public void should_find_existing_reservation() throws EntityNotFoundException {
        Reservation reservation = reservationFactory.create();
        Integer reservationId = reservation.getId();

        ReservationServiceImpl reservationServiceSpy = Mockito.spy(reservationService);

        doReturn(Optional.of(reservation))
                .when(reservationServiceSpy)
                .findById(reservationId);

        ReservationDTO reservationsDB = reservationServiceSpy.getReservationById(reservationId);

        assertEquals(reservation.getClient().getId(), reservationsDB.getClientId());
        assertEquals(reservation.getAppointment().getId(), reservationsDB.getAppointmentId());
        assertEquals(reservation.getEmployee().getId(), reservationsDB.getEmployeeId());
    }

    @Test
    public void should_store_new_reservation() {
        Reservation reservation = reservationFactory.create();


        Appointment appointment = reservation.getAppointment();
        Client client = reservation.getClient();
        Employee employee = reservation.getEmployee();

        reservation.setId(null);
        reservation.setCreatedAt(null);
        reservation.setDuration(appointment.getDuration());

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setClientId(reservation.getClient().getId());
        reservationDTO.setEmployeeId(reservation.getEmployee().getId());
        reservationDTO.setAppointmentId(reservation.getAppointment().getId());

        reservationDTO.setDuration(reservation.getAppointment().getDuration());
        reservationDTO.setStartAt(reservation.getStartAt());

        ReservationServiceImpl reservationServiceSpy = Mockito.spy(reservationService);

        doReturn(reservation)
                .when(reservationRepository)
                .saveAndFlush(reservation);

        doReturn(appointment)
                .when(reservationServiceSpy)
                .getAppointment(reservationDTO);

        doReturn(client)
                .when(reservationServiceSpy)
                .getClient(reservationDTO);

        doReturn(employee)
                .when(reservationServiceSpy)
                .getEmployee(reservationDTO);

        ReservationDTO storedReservation = reservationServiceSpy.storeReservation(reservationDTO);

        assertEquals(reservationDTO, storedReservation);
    }

    @Test
    public void should_delete_existing_reservation() {
        Reservation reservation = reservationEntities.get(0);
        Integer reservationId = reservation.getId();

        when(reservationRepository.existsById(reservationId))
                .thenReturn(true);

        ReservationServiceImpl reservationServiceSpy = Mockito.spy(reservationService);

        reservationServiceSpy.deleteReservation(reservationId);

        Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(reservationId);
    }
}
