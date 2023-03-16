import java.util.Random;

/*
 * A Long Haul Flight is a flight that travels a long distance and has two types of seats (First Class and Economy)
 */
public class LongHaulFlight extends Flight
{
	int firstClassPassengers;
		
	public LongHaulFlight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		super(flightNum, airline, dest, departure, flightDuration, aircraft);
		type = Flight.Type.LONGHAUL;
	}

	public LongHaulFlight()
	{

	}

	public void reserveSeat(Passenger p, String seat)
	{
		if (p.getSeatType().equals("First Class")) {
			if (firstClassPassengers >= aircraft.getNumFirstClassSeats()) {
				throw new FirstClassFullException("Flight " + flightNum + " first class full");
			}
			super.reserveSeat(p, seat);
			firstClassPassengers++;
		}
		else {
			super.reserveSeat(p, seat);
		}
	}
	
	public void cancelSeat(Passenger p)
	{
		if (p.getSeatType().equals("First Class")) {
			super.cancelSeat(p);
			firstClassPassengers--;
		}
		else {
			super.cancelSeat(p);
		}
	}
	
	public boolean seatsAvailable(String seatType)  // method to check if seats are available
	{
		if (firstClassPassengers < aircraft.getNumFirstClassSeats())  // if first class seats are greater than first class passengers, then seats are vacant
			return true;
		return false;  // else, they are occupied
	}
	
	public String toString()
	{
		 return super.toString() + "\t LongHaul";
	}
}
