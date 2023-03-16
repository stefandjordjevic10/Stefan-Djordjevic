import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Flight System for one single day at YYZ (Print this in title) Departing flights!!


public class FlightReservationSystem
{
	public static void main(String[] args)
	{
		FlightManager manager = null;

		ArrayList<Reservation> myReservations = new ArrayList<Reservation>();	// my flight reservations
		
		try {
			manager = new FlightManager(); // try manager = new FlightManager()
		}
		catch (FlightNotFoundException error) {  // catches file not found exception when text file isnt found and displays error message
			System.out.println("Flights.txt File Not Found");
			System.exit(1);
		}
		catch (IOException error) {  // catches IO exception if file has bad format and displays error message
			System.out.println("Bad File Format flights.txt");
			System.exit(1);
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		while (scanner.hasNextLine())
		{
			String inputLine = scanner.nextLine();
			if (inputLine == null || inputLine.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}

			Scanner commandLine = new Scanner(inputLine);
			String action = commandLine.next();
			try {
				if (action == null || action.equals("")) 
				{
					System.out.print("\n>");
					continue;
				}

				else if (action.equals("Q") || action.equals("QUIT"))
					return;

				else if (action.equalsIgnoreCase("LIST"))
				{
					manager.printAllFlights(); 
				}
				else if (action.equalsIgnoreCase("RES"))
				{
					String flightNum = null;
					String passengerName = "";
					String passport = "";
					String seatType = "";

					if (commandLine.hasNext())
					{
						flightNum = commandLine.next();
					}
					if (commandLine.hasNext())
					{
						passengerName = commandLine.next();
					}
					if (commandLine.hasNext())
					{
						passport = commandLine.next();
					}
					if (commandLine.hasNext())
					{
						seatType = commandLine.next();

						Reservation res = manager.reserveSeatOnFlight(flightNum, passengerName, passport, seatType);
						if (res != null)
						{
							myReservations.add(res);
							res.print();
						}
					}
				}
				else if (action.equalsIgnoreCase("CANCEL"))
				{
					Reservation res = null;
					String flightNum = null;
					String passengerName = "";
					String passport = "";
					String seatType = "";

					if (commandLine.hasNext())
					{
						flightNum = commandLine.next();
					}
					if (commandLine.hasNext())
					{
						passengerName = commandLine.next();
					}
					if (commandLine.hasNext())
					{
						passport = commandLine.next();
				
						int index = myReservations.indexOf(new Reservation(flightNum, passengerName, passport));
						if (index >= 0)
						{
							manager.cancelReservation(myReservations.get(index));
							myReservations.remove(index);
						}
						else
							System.out.println("Reservation on Flight " + flightNum + " Not Found");
					}
				}
				else if (action.equalsIgnoreCase("SEATS"))
				{
					String flightNum = null;
					if (commandLine.hasNext())
					{
						flightNum = commandLine.next();
						manager.seatsAvailable(flightNum);
						System.out.println("\nXX = Ocupied + = First Class ");  // changed original code to say "XX = Ocupied + = First Class" when seat available
					}
				}
				else if (action.equalsIgnoreCase("MYRES"))
				{
					for (int i = 0; i < myReservations.size(); i++)
						myReservations.get(i).print();
				}
				else if (action.equalsIgnoreCase("PASMAN")) {    // checks to see if action = "PASMAN"
					String flightNum = "";
					if (commandLine.hasNext()) {  // if input is "PASMAN" prints the passenger manifest for the flight
						flightNum = commandLine.next();
						manager.printManifest(flightNum);
					}
				}
			}
			catch (FlightNotFoundException error) {  // catches file not found exception when file is not found, prints error message
				System.out.println(error.getMessage());
			}
			catch (FlightFullException error) {  // catches flight full exception when flight is full, prints error message
				System.out.println(error.getMessage());
			}
			catch (DuplicatePassengerException error) {  // catches duplicate passenger exception when passengers accidentally accouted twice, prints error message
				System.out.println(error.getMessage());
			}
			catch (PassengerNotInManifestException error) {  // catches passenger not in manifest exception when passenger is not in manifest, prints error message
				System.out.println(error.getMessage());
			}
			catch (SeatOccupiedException error) {  // catches seat occupied exception when seat is already taken, prints error message
				System.out.println(error.getMessage());
			}
			System.out.print("\n>");
		}
	}


}


