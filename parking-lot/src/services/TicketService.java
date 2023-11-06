package services;

import exceptions.InvalidGateException;
import exceptions.NoAvailableSlotException;
import models.*;
import repositories.IGateRepository;
import repositories.ParkingLotRepository;
import repositories.TicketRepository;
import repositories.VehicleRepository;
import strategies.spotassignmentstrategy.SpotAssignmentStrategy;

import java.util.Date;
import java.util.Optional;

public class TicketService {

    private final IGateRepository gateRepository;
    private final VehicleRepository vehicleRepository;

    private final SpotAssignmentStrategy spotAssignmentStrategy;

    private final TicketRepository ticketRepository;
    private final ParkingLotRepository parkingLotRepository;

    public TicketService(IGateRepository gateRepository,
                         VehicleRepository vehicleRepository,
                         SpotAssignmentStrategy spotAssignmentStrategy,
                         TicketRepository ticketRepository,
                         ParkingLotRepository parkingLotRepository) {
        this.gateRepository = gateRepository;
        this.vehicleRepository = vehicleRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.ticketRepository = ticketRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    public Ticket generateTicket(Long gateId,
                                 VehicleType vehicleType,
                                 String vehicleNumber) throws InvalidGateException, NoAvailableSlotException {

        /*
        Gate = get gate from the db, else throw exception
        Operator = from gate
        Vehicle = check if already in DB. if yes, get ti else create
        ParkingSpot = strategy
        Ticket ticket =
        */

        Optional<Gate> gateOptional = gateRepository.findGateById(gateId);

        if (gateOptional.isEmpty()) {
            throw new InvalidGateException();
        }

        Gate gate = gateOptional.get();
        Operator operator = gate.getCurrentOperator();

        Optional<Vehicle> vehicleOptional = vehicleRepository.findVehicleByNumber(vehicleNumber);

        Vehicle vehicle;

        if (vehicleOptional.isEmpty()) {
            vehicle = new Vehicle();
            vehicle.setVehicleNumber(vehicleNumber);
            vehicle.setVehicleType(vehicleType);
            vehicle = vehicleRepository.save(vehicle);
        } else {
            vehicle = vehicleOptional.get();
        }

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotOfGate(gate);

        if (parkingLotOptional.isEmpty()) {
            throw new RuntimeException();
        }

        Optional<ParkingSpot> parkingSpotOptional = spotAssignmentStrategy.findSpot(
                vehicleType,
                parkingLotOptional.get(),
                gate
        );

        if (parkingSpotOptional.isEmpty()) {
            throw new NoAvailableSlotException();
        }

        ParkingSpot parkingSpot = parkingSpotOptional.get();

        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setGate(gate);
        ticket.setEntryTime(new Date());
        ticket.setVehicle(vehicle);
        ticket.setOperator(operator);

        return ticketRepository.save(ticket);
    }
}
