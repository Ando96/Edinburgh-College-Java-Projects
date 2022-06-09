package F28DA_CW2;

import java.util.List;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> 
{
	/** A list containing all the stops(airports) of a journey **/
	private List<String> stops;
	
	/** A list containing all the flights of a journey **/
	private List<String> flights;
	
	/** The number of stops made(airports visited) **/
	private int numHops;
	
	/** The cost of the journey **/
	private int cost;
	
	/** The air time of the journey **/
	private int flightAirTime;
	
	/** The connecting time of the journey(time waiting between flights if more than 1) **/
	private int connectingTime;
	
	/** The total time of the entire journey **/
	private int totalTime;
	
	/** Empty journey constructor **/
	Journey(){}
	
	/** Constructor for a journey and its required fields **/
	Journey(List<String>stops, List<String>flights, int numHops, int cost, int flightAirTime, int connectingTime, int totalTime)
	{
		this.stops = stops;
		this.flights = flights;
		this.numHops = numHops;
		this.cost = cost;
		this.flightAirTime = flightAirTime;
		this.connectingTime = connectingTime;
		this.totalTime = totalTime;
	}
	
	
	/** Returns the list of airport codes of the journey **/
	@Override
	public List<String> getStops() 
	{	
		return this.stops;
	}

	
	/** Returns the list of flight codes of the journey **/
	@Override
	public List<String> getFlights() 
	{	
		return this.flights;
	}

	
	/** Returns the total number of airports visited **/
	@Override
	public int totalHop() 
	{
		return this.numHops;
	}

	
	/** Returns the total cost of the journey **/
	@Override
	public int totalCost() 
	{
		return this.cost;
	}

	
	/** Returns the time spent in the air in minutes **/
	@Override
	public int airTime() 
	{
		return this.flightAirTime;
	}

	
	/** Returns the connecting time in minutes **/
	@Override
	public int connectingTime() 
	{		
		return this.connectingTime;
	}

	
	/** Calculates the total time of the journey and returns it in minutes **/
	@Override
	public int totalTime() 
	{	
		return this.totalTime;
	}

	
	/** toString method for displaying journey data **/
	 @Override
	 public String toString() 
	 {
		 return "Stops: " + stops + "\nFlights: " + flights + "\nNumber of hops: " + numHops + "\nCost: " + cost + "\nAir Time: " + flightAirTime + "\nConnecting Time: " 
				 + connectingTime + "\nTotal journey time: " + totalTime;	        
	 }
	
}