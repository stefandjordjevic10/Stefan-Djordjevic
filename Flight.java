import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Flight
{
	public enum Status {DELAYED, ONTIME, ARRIVED, INFLIGHT};
	public static enum Type {SHORTHAUL, MEDIUMHAUL, LONGHAUL};
	public static enum SeatType {ECONOMY, FIRSTCLASS, BUSINESS};

	protected String flightNum;
	private String airline;
	private String origin, dest;
	private String departureTime;
	private Status status;
	private int flightDuration;
	protected Aircraft aircraft;
	protected int numPassengers;
	protected Type type;
	protected SeatType seatType;
	
	protected ArrayList<Passenger> manifest = new ArrayList<Passenger>();
	protected TreeMap<String, Passenger> seatMap = new TreeMap<String, Passenger>();
	
	public Flight()
	{

	}
	
	public Flight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		this.flightNum = flightNum;
		this.airline = airline;
		this.dest = dest;
		this.origin = "Toronto";
		this.departureTime = departure;
		this.flightDuration = flightDuration;
		this.aircraft = aircraft;
		numPassengers = 0;
		status = Status.ONTIME;
		type = Type.MEDIUMHAUL;
	}
	
	public Type getFlightType()
	{
		return type;
	}
	
	public String getFlightNum()
	{
		return flightNum;
	}
	public void setFlightNum(String flightNum)
	{
		this.flightNum = flightNum;
	}
	public String getAirline()
	{
		return airline;
	}
	public void setAirline(String airline)
	{
		this.airline = airline;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getDepartureTime()
	{
		return departureTime;
	}
	public void setDepartureTime(String departureTime)
	{
		this.departureTime = departureTime;
	}
	
	public Status getStatus()
	{
		return status;
	}
	public void setStatus(Status status)
	{
		this.status = status;
	}
	public int getFlightDuration()
	{
		return flightDuration;
	}
	public void setFlightDuration(int dur)
	{
		this.flightDuration = dur;
	}
	
	public ArrayList<Passenger> getNumPassengers()
	{
		return manifest;
	}
	public void printPassengerManifest()
	{
		for (Passenger p : manifest) {
			System.out.println(p);
		}
	}
	
	public void printSeats() {  // method to print seats
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  // indicate alphabet
		String[][] layout = aircraft.getSeatLayout();  // get seat layout
		int rowNum = 0; // initialize row and column number
		int columnNum = 0;
		
		System.out.println();
		for (int i = 0; i < layout[0].length; i++) {  // loop through row
			for (int j = 0; j < layout.length; j++) {  // loop through column
				rowNum = i;  // set row and column accordingly
				columnNum = j + 1;
				String seat = "" + columnNum + alpha.charAt(rowNum);  // set string seat to column number and character at the row number in alphabet
				if (seatMap.containsKey(seat)) {  // param if key seat is in seatMap
					System.out.print("XX ");  // if yes, print XX
				}
				else {
					System.out.print(seat + " ");  // if not, print seat
				}
			}
		if (rowNum == 1) {
			System.out.println();
		}
		System.out.println();
		}
	}
	
	public boolean seatsAvailable() // method to check if seats are available
	{
		if (numPassengers < aircraft.numEconomySeats) return true;  // checks to see if there are more economy seats than number of passengers
		return false;
	}
	
	public void cancelSeat(Passenger p)  // method to cancel seat
	{
		int index = manifest.indexOf(p);  // set variable index to index of p in manifest
		if (index == -1) {  // if index is -1 throw exception to say passenger is not in manifest since no indexes were found
			throw new PassengerNotInManifestException(flightNum + "Passenger " + p.getName() + " " + p.getPassport());
		}
		seatMap.remove(p.getSeat());  // remove seat
		manifest.remove(p);  // remove passenger
		if (p.getSeatType().equals("Economy")) {  // if seat type is economy, reduce number of passengers by 1
			numPassengers--;
		}
	}
	
	public void reserveSeat(Passenger p, String seat)  // method to reserve seat
	{
		if (p.getSeatType().equals("Economy") && numPassengers >= aircraft.getNumSeats()) {  // if seat type is economy and more passengers than number of seats
			throw new FlightFullException("Flight" + flightNum + " full");  // throw flight full exception indicating flight is full
		}
		int index = manifest.indexOf(p);  // set variable index to index of p in manifest
		if (index >= 0) {  // if index is more than 0 throw duplicate passenger exception, indicating that passenger has been accounted for too many times mistakenly
			throw new DuplicatePassengerException("Duplicate Passenger " + p.getName() + " " + p.getPassport());
		}
		if (seatMap.containsKey(seat)) {  // if seatMap contains key seat, thro seat already occupied exception since this seat is taken
			throw new SeatOccupiedException("Seat " + seat + " already occupied");
		}
		manifest.add(p);  // add passenger p to manifest
		seatMap.put(seat, p); 
		if (p.getSeatType().equals("Economy")) {  // if seat type is economy, add 1 more passenger to number of passengers 
			numPassengers++;
		}
	}
	
	public boolean equals(Object other)
	{
		Flight otherFlight = (Flight) other;
		return this.flightNum.equals(otherFlight.flightNum);
	}
	
	public String toString()
	{
		 return airline + "\t Flight:  " + flightNum + "\t Dest: " + dest + "\t Departing: " + departureTime + "\t Duration: " + flightDuration + "\t Status: " + status;
	}
}

class FlightNotFoundException extends RuntimeException {  // method creates flight not found exception to indicate when the specified flight is not found
	public FlightNotFoundException() {}
	public FlightNotFoundException(String message) {
		super(message);
	}
}

class FlightFullException extends RuntimeException {  // method creates flight full exception to indicate when flights are full
	public FlightFullException() {}
	public FlightFullException(String message) {
		super(message);
	}
}

class DuplicatePassengerException extends RuntimeException {  // method creates duplicate passenger exception for when passenger is accidentally counted twice
	public DuplicatePassengerException() {}
	public DuplicatePassengerException(String message) {
		super(message);
	}
}

class PassengerNotInManifestException extends RuntimeException {  // method creates passenger not in manifest exception for when passenger not found in manifest
	public PassengerNotInManifestException() {}
	public PassengerNotInManifestException(String message) {
		super(message);
	}
}

class SeatOccupiedException extends RuntimeException {  // method creates seat occupied exception for when seat is already occupied
	public SeatOccupiedException() {}
	public SeatOccupiedException(String message) {
		super(message);
	}
}

class FirstClassFullException extends RuntimeException {  // method creates first class full exception for when first class is full
	public FirstClassFullException() {}
	public FirstClassFullException(String message) {
		super(message);
	}
}