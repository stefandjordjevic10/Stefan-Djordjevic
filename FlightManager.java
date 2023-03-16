import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;


public class FlightManager
{
  TreeMap<String, Flight> flights = new TreeMap<String, Flight>();
  
  String[] cities 	= 	{"Dallas", "New York", "London", "Paris", "Tokyo"};
  final int DALLAS = 0;  final int NEWYORK = 1;  final int LONDON = 2;  final int PARIS = 3; final int TOKYO = 4;
  
  int[] flightTimes = { 3, // Dallas
  											1, // New York
  											7, // London
  											8, // Paris
  											16,// Tokyo
  										};
  
	public FlightManager() throws FileNotFoundException 
	{
	File file = new File("C:\\Users\\stefa\\Downloads\\Assignment 2\\flights.txt");  // reads text file
	Scanner sc = new Scanner(file);
	String airline = "";  // initializing variables to either empty strings or 0
	String destination = "";
	int newDestination = 0;
	String depTime = "";
	int capacity = 0;
	for (int i = 0; i < 8; i++) {  // loop to go through individual lines in text file
		String f = sc.nextLine();  // new variable introduced to represent the line most recently looped through
		Scanner s = new Scanner(f);
		if (s.hasNext()) {  // sets the first word of the line equal airline
			String fl = s.next();
			if (fl.contains("_")) {
				String flt = fl.replace("_", " ");  // replaces all underscores with spaces as indicated in the instructions
				airline = flt;
			}
		}
		if (s.hasNext()) {  // this is statement checks the next word in the line and sets it equal to destination
			destination = s.next();
			if (destination.contains("_")) {
				String d = destination.replace('_', ' ');  // also replaces underscores with spaces
				destination = d;
			}
			if (destination.equals("Dallas")) {  // using the old constructor method, depending on the airline, I set its according destination
				newDestination = 0;
			}
			else if (destination.equals("New York")) {
            	newDestination = 1;
			}
            else if (destination.equals("London")) {
            	newDestination = 2;
            }            
            else if (destination.equals("Paris")) {
            	newDestination = 3;
            }
            else if (destination.equals("Tokyo")) {
            	newDestination = 4;
            }
		}
		if (s.hasNext()) { // keeps going through each individual line, this time third word is set to the departure time
			depTime = s.next();
		}
		if (s.hasNextInt()) {  // finally, the last word, or in this case integer, is set to capacity
			capacity = s.nextInt();
			if (capacity >= 140) {  // only 2 types of flight, economy or long haul, if capacity is greater than 140, then it is a long haul flight
				airplanes.add(new Aircraft(capacity, 4, "Boeing 737"));
				String flightNum = generateFlightNumber(airline);
                Flight flight = new LongHaulFlight(flightNum, airline, destination, depTime, flightTimes[newDestination], airplanes.get(airplanes.size() -1)); 
                flights.put(flightNum, flight);
	        }
			else if (capacity < 140) {  // if the capacity is less than 140, will be economy flight
				if (140 > capacity && capacity >= 100) {
					airplanes.add(new Aircraft(capacity, "Airbus 320"));
				}
		        else if (100 > capacity && capacity >= 75) {
		        	airplanes.add(new Aircraft(capacity, "Airbus 320"));
		        }
		        else if (75 > capacity && capacity >= 50) {
		            airplanes.add(new Aircraft(capacity, "Dash-8 100"));
		        }
		        else if (50 > capacity && capacity >= 25) {
		            airplanes.add(new Aircraft(capacity, "Bombardier 5000"));
		        }
		        else if (25 > capacity) {
		        	airplanes.add(new Aircraft(capacity, "Airbus 320"));
		        }
				String flightNum = generateFlightNumber(airline);
                Flight flight = new Flight(flightNum, airline, destination, depTime, flightTimes[newDestination], airplanes.get(airplanes.size() -1)); 
                flights.put(flightNum, flight);
			}
		}
		}
	}	
	
  ArrayList<Aircraft> airplanes = new ArrayList<Aircraft>();  
  ArrayList<String> flightNumbers = new ArrayList<String>();
  
  String errMsg = null;
  Random random = new Random();
  
  private String generateFlightNumber(String airline)
  {
  	String word1, word2;
  	Scanner scanner = new Scanner(airline);
  	word1 = scanner.next();
  	word2 = scanner.next();
  	String letter1 = word1.substring(0, 1);
  	String letter2 = word2.substring(0, 1);
  	letter1.toUpperCase(); letter2.toUpperCase();
  	
  	// Generate random number between 101 and 300
  	boolean duplicate = false;
  	int flight = random.nextInt(200) + 101;
  	String flightNum = letter1 + letter2 + flight;
   	return flightNum;
  }
  
  public void printAllFlights() // method to print all flights - used in list
  {
  	for (String i : flights.keySet()) {  // searches through flights
  		Flight f = flights.get(i);  // prints corresponding flight info for each key when looped through
  		System.out.println(f);
  	}
  }
  
  public void seatsAvailable(String flightNum) // method to check if seats are available
  {
    Flight flight = null;
    if (!flights.containsKey(flightNum)) {  // if key isnt found, raise flight not found error
    	throw new FlightNotFoundException("Flight " + flightNum + " not found");
    }
    flight = flights.get(flightNum);  // else, get the flightNum associated with the key
    flight.printSeats();
  }
  
  public Reservation reserveSeatOnFlight(String flightNum, String name, String passport, String seat)  // method to reserve seat on flight
  {
	  Flight flight = null;
	  if (!flights.containsKey(flightNum)) {  // if key isnt found, raise flight not found error
		  	throw new FlightNotFoundException("Flight " + flightNum + " not found");
	  }
	  flight = flights.get(flightNum);  // if key is found, get corresponding flightNum
	  String seatType = "";
	  if (seat.length() == 3 && seat.charAt(2) == '+') {  // if charAt(2) equals a plus sign, it is a first class seat
		  seatType = "First Class";
	  }
	  else {
		  seatType = "Economy";  // else,  must be an economy seat
	  }
	  Passenger p = new Passenger(name, passport, seat, seatType);  // loads new passenger
	  flight.reserveSeat(p, seat);  // reserves seat to the loaded passenger, p
	  Reservation res = new Reservation(flight.getFlightNum(), flight.toString(), name, passport, seat);
	  return res;  // sets and returns new reservation
  }
  
  public void cancelReservation(Reservation res)  // method to cancel reservation
  {
	  Flight flight = null;
	  if (!flights.containsKey(res.getFlightNum())) {  // if key isnt found, raise flight not found error
		  throw new FlightNotFoundException("Flight " + res.getFlightNum() + " not found");
	  }
	  flight = flights.get(res.getFlightNum());  //if found, set flight to corresponding flightNum
	  Passenger p = new Passenger(res.name, res.passport, res.seat, res.seatType);  // loads new passenger p with info from res
	  flight.cancelSeat(p);  // cancels seat for passenger p
  }
  
  public void printManifest(String flightNum) {
	  Flight flight = flights.get(flightNum);
	  flight.printPassengerManifest();
  }
}