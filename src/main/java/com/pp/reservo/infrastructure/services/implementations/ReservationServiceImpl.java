package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.models.response.BaseReservationsByClientResponseDTO;
import com.pp.reservo.domain.models.response.BaseReservationsByEmployeeResponseDTO;
import com.pp.reservo.domain.models.response.ReservationsByClientResponseDTO;
import com.pp.reservo.domain.models.response.ReservationsByEmployeeResponseDTO;
import com.pp.reservo.domain.repositories.ReservationRepository;
import com.pp.reservo.domain.repositories.specification.ReservationSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.AppointmentService;
import com.pp.reservo.infrastructure.services.ClientService;
import com.pp.reservo.infrastructure.services.EmployeeService;
import com.pp.reservo.infrastructure.services.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pp.reservo.domain.common.Domain.PAGE_SIZE;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;
    private final AppointmentService appointmentService;
    private final ClientService clientService;
    private final EmployeeService employeeService;

    public ReservationServiceImpl(ModelMapper modelMapper,
                                  ReservationRepository reservationRepository,
                                  AppointmentService appointmentService,
                                  ClientService clientService,
                                  EmployeeService employeeService
    ) {
        this.modelMapper = modelMapper;
        this.reservationRepository = reservationRepository;
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.employeeService = employeeService;
    }

    @Override
    public List<ReservationDTO> getAllReservations(
            Date byDate,
            Integer clientId,
            Integer employeeId,
            Integer appointmentId,
            Integer page,
            String sortBy
    ) {
        PageRequest pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.Direction.ASC,
                sortBy
        );

        ReservationSpecification specification = getSpecificationInstance(byDate, clientId, employeeId, appointmentId);

        return this.reservationRepository
                .findAll(specification, pageable)
                .stream()
                .map(r -> this.modelMapper.map(r, ReservationDTO.class))
                .collect(Collectors.toList());
    }

    public ReservationSpecification getSpecificationInstance(Date byDate,
                                                              Integer clientId,
                                                              Integer employeeId,
                                                              Integer appointmentId
    ) {
        return new ReservationSpecification(
                byDate,
                clientId,
                employeeId,
                appointmentId
        );
    }

    @Override
    public ReservationDTO getReservationById(Integer reservationId) throws EntityNotFoundException {
        Optional<Reservation> reservationOptional = findById(reservationId);

        if (reservationOptional.isPresent()) {
            ReservationDTO reservationDTO = convertToDto(reservationOptional.get());
            return reservationDTO;
        }

        throw new EntityNotFoundException("Reservation with the given id was not found!");
    }

    public Optional<Reservation> findById(Integer reservationId) {
        return this.reservationRepository.findById(reservationId);
    }

    @Override
    public ReservationDTO storeReservation(ReservationDTO reservationDTO) {
        Reservation reservation = convertToEntity(reservationDTO);
        this.reservationRepository.saveAndFlush(reservation);

        return reservationDTO;
    }

    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        Appointment appointment = getAppointment(reservationDTO);
        Client client = getClient(reservationDTO);
        Employee employee = getEmployee(reservationDTO);

        reservation.setClient(client);
        reservation.setAppointment(appointment);
        reservation.setEmployee(employee);

        reservation.setStartAt(reservationDTO.getStartAt());
        reservation.setDuration(appointment.getDuration());

        return reservation;
    }

    private ReservationDTO convertToDto(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();

        reservationDTO.setClientId(reservation.getClient().getId());
        reservationDTO.setAppointmentId(reservation.getAppointment().getId());
        reservationDTO.setEmployeeId(reservation.getEmployee().getId());

        reservationDTO.setStartAt(reservation.getStartAt());
        reservationDTO.setDuration(reservation.getDuration());

        return reservationDTO;
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        if(reservationRepository.existsById(reservationId)) {
            reservationRepository.deleteById(reservationId);
        }
    }

    @Override
    public BaseReservationsByClientResponseDTO getReservationsByClient(Integer clientId) {
        ClientDTO client = clientService.getClientById(clientId);
        List<Reservation> clientReservations = reservationRepository.findAllByClientId(clientId);

        List<ReservationsByClientResponseDTO> reservationsDTOList = new ArrayList<>();

        for(Reservation clientReservation: clientReservations) {

            ReservationsByClientResponseDTO reservationsByClientResponseDTO = new ReservationsByClientResponseDTO();

            reservationsByClientResponseDTO.setAppointmentName(clientReservation.getAppointment().getName());
            reservationsByClientResponseDTO.setEmployeeName(clientReservation.getEmployee().getName());
            reservationsByClientResponseDTO.setStartAt(clientReservation.getStartAt().toLocalDateTime().toString());
            reservationsByClientResponseDTO.setEndAt(calcEndAt(clientReservation));

            reservationsDTOList.add(reservationsByClientResponseDTO);
        }

        BaseReservationsByClientResponseDTO baseReservationsByClientResponseDTO = new BaseReservationsByClientResponseDTO();
        baseReservationsByClientResponseDTO.setClientName(client.getName());
        baseReservationsByClientResponseDTO.setReservations(reservationsDTOList);

        return baseReservationsByClientResponseDTO;
    }

    @Override
    public BaseReservationsByEmployeeResponseDTO getReservationsByEmployee(Integer employeeId) {
        EmployeeDTO employee = employeeService.getEmployeeById(employeeId);
        List<Reservation> employeeReservations = reservationRepository.findAllByEmployeeId(employeeId);

        List<ReservationsByEmployeeResponseDTO> reservationsDTOList = new ArrayList<>();

        for(Reservation employeeReservation: employeeReservations) {

            ReservationsByEmployeeResponseDTO reservationsByEmployeeResponseDTO = new ReservationsByEmployeeResponseDTO();

            reservationsByEmployeeResponseDTO.setClientName(employeeReservation.getClient().getName());
            reservationsByEmployeeResponseDTO.setAppointmentName(employeeReservation.getAppointment().getName());
            reservationsByEmployeeResponseDTO.setStartAt(employeeReservation.getStartAt().toLocalDateTime().toString());
            reservationsByEmployeeResponseDTO.setEndAt(calcEndAt(employeeReservation));

            reservationsDTOList.add(reservationsByEmployeeResponseDTO);
        }

        BaseReservationsByEmployeeResponseDTO baseReservationsByEmployeeResponseDTO = new BaseReservationsByEmployeeResponseDTO();
        baseReservationsByEmployeeResponseDTO.setEmployeeName(employee.getName());
        baseReservationsByEmployeeResponseDTO.setReservations(reservationsDTOList);

        return baseReservationsByEmployeeResponseDTO;
    }

    public Appointment getAppointment(ReservationDTO reservationDTO) {
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(reservationDTO.getAppointmentId());
        return this.modelMapper.map(appointmentDTO, Appointment.class);
    }

    public Employee getEmployee(ReservationDTO reservationDTO) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
        return this.modelMapper.map(employeeDTO, Employee.class);
    }

    public Client getClient(ReservationDTO reservationDTO) {
        ClientDTO clientDTO = clientService.getClientById(reservationDTO.getClientId());
        return this.modelMapper.map(clientDTO, Client.class);
    }

    private String calcEndAt(Reservation clientReservation) {
        Timestamp startTime = clientReservation.getStartAt();
        return startTime.toLocalDateTime().plus(Duration.of(clientReservation.getDuration(), ChronoUnit.MINUTES)).toString();
    }
}
