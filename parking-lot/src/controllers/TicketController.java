package controllers;

import dtos.GenerateTicketRequestDto;
import dtos.GenerateTicketResponseDto;
import dtos.ResponseStatus;
import exceptions.InvalidGateException;
import exceptions.NoAvailableSlotException;
import models.Gate;
import models.Ticket;
import models.Vehicle;
import models.VehicleType;
import services.TicketService;

public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /*
    No need to send whole gate object
    public void generateTicket(Vehicle vehicle,
                               Gate gate) {
    }*/

    public GenerateTicketResponseDto generateTicket(GenerateTicketRequestDto request) {

        String vehicleNumber = request.getVehicleNumber();
        VehicleType vehicleType = request.getVehicleType();
        Long gateId = request.getGateId();

        Ticket ticket = new Ticket();
        GenerateTicketResponseDto response = new GenerateTicketResponseDto();

        try {
            ticket= ticketService.generateTicket(gateId,
                    vehicleType,
                    vehicleNumber);
        } catch (InvalidGateException invalidGateException) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage("Gate ID is invalid");
            return response;
        } catch (NoAvailableSlotException noAvailableSlotException) {
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("No parking slot available.");
            return response;
        }

        response.setTicketId(ticket.getId());
        response.setOperatorName(ticket.getOperator().getName());
        response.setSpotNumber(ticket.getParkingSpot().getNumber());
        response.setResponseStatus(ResponseStatus.SUCCESS);

        return response;
    }
}
