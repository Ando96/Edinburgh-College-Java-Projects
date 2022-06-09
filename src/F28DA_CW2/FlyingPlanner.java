package F28DA_CW2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport,Flight>, IFlyingPlannerPartC<Airport,Flight> 
{
	/** Graph for storing Airports(Vertices) and Flights(Edges) **/
	private Graph<Airport, Flight> g = new SimpleDirectedWeightedGraph<Airport, Flight>(Flight.class);
	
	/** A temporary graph used in finding the shortest vertex path from one airport to another **/
	private Graph<Airport, Flight> hopG = new SimpleDirectedWeightedGraph<Airport, Flight>(Flight.class);
	  
	/** Array list used for storing airports **/
	private ArrayList<Airport> AirportList = new ArrayList<>();
	
	/** Array list used for storing flights **/
	private ArrayList<Flight> FlightList = new ArrayList<>();
	 
	
	/** Calls the second populate passing in the airport and flight data **/
	@Override
	public boolean populate(FlightsReader fr) 
	{	
		return populate(fr.getAirports() , fr.getFlights());
	}

	
	/** Populates the graphs and lists with data from the files **/
	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) 
	{	
		
	Iterator<String[]> APsetItr = airports.iterator(); //Iterate through the airport set	
		
		while(APsetItr.hasNext()) //While the airport set still has data
		{
			String[]airport = APsetItr.next(); //Create a airport list from the next item from the iterator
			
			String airportCode = airport[0]; //The first element in the list is the airport code		
			String airportCity = airport[1]; //The second element in the list is the airport city
			String airportName = airport[2]; //The third element in the list is the airport name
			
			Airport tempAirport = new Airport(airportCode,airportCity,airportName); //Create a new temporary airport
			
			g.addVertex(tempAirport); //Add the temporary airport to the graph
			
			hopG.addVertex(tempAirport); //Add the temporary airport to the graph
			
			AirportList.add(tempAirport); //And also add it to the list of airports
		}
		
	Iterator<String[]> FLsetItr = flights.iterator(); //Iterate through the flight set
		
		while(FLsetItr.hasNext()) //While the flight set sill has data
		{	
			String[] test = FLsetItr.next();
			
			String flightCode = test[0]; //The first element in the flight list is the flight code			
			Airport start = airport(test[1]); //The second element in the flight list is the starting airport			
			String FromGMTime = test[2]; //The third element in the flight list is the departure time			
			Airport finish = airport(test[3]); //The fourth element in the flight list is the end airport			
			String ToGMTime = test[4]; //The fifth element in the flight list is the arrival time			
			int flightCost = Integer.parseInt(test[5]); //And the final element in the list is the cost of the flight
			
			//Create a new temporary flight from the data 
			Flight tempFlight = new Flight(flightCode, start , FromGMTime , finish , ToGMTime , flightCost);
			
			FlightList.add(tempFlight); //Add the temporary flight to the flight list
			
			g.addEdge(start, finish, tempFlight); //Create a new edge between the starting and finishing airports in g
			
			hopG.addEdge(start, finish, tempFlight); //Create a new edge between the starting and finishing airports in g
			
			g.setEdgeWeight(start, finish, flightCost); //Set the weight of the new edge to the cost of the flight
			
			hopG.setEdgeWeight(start, finish, 1); //Set the weight of the new edge to 1
		}
		
		
		//If the graph has no vertices, then it is not populated with data and so return false
		if(g.vertexSet().size() == 0)
		{
			return false;
		}
		
		
	//Else return true if there is data in the graph	
	return true;
	}

	
    /** Given a airport code, this method will search the airport list and attempt to find a match **/
	@Override
	public Airport airport(String code) 
	{	
		Airport result = new Airport(); //Holds the result if a match is found
		
	    Airport AirportItr = new Airport(); //Holds the airport from the iterator
		
		Iterator<Airport> APitr = AirportList.iterator(); //Create a iterator for the airport list
		
		while(APitr.hasNext()) //While the airport iterator still has data
		{
			AirportItr = APitr.next(); //Assign the next airport from the iterator to AirportItr
			
			if (code.equals(AirportItr.getCode())) //If the airport code of AirportItr matches the input airport code
			{   			
				result = AirportItr; //Assign the matched airport to the result
			}
		}
		return result; //Return the result
	}

	
	/** Given a flight code, this method will search the flight list and return a flight if a match is found **/
	@Override
	public Flight flight(String code) 
	{	
		Flight result = new Flight(); //Holds the result if a match is found
	    Flight FlightItr = new Flight(); //Holds the flight from the iterator
		
		Iterator<Flight> FLlistItr = FlightList.iterator(); //Create a iterator for the flight list
		
		while(FLlistItr.hasNext()) //While the flight list sill has data
		{
			FlightItr = FLlistItr.next(); //Assign the next flight from the iterator to FlightItr
			
			if(code.equals(FlightItr.getFlightCode())) //If the flight code of FlightItr matches the input flight code
			{
				result = FlightItr; //Assign the matched flight to the result
			}
		}
		return result; //Return the result
	}

	
	/** Attempts to find, if possible, the cheapest path from a starting airport and end airport **/
	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException 
	{			
		if(!g.containsVertex(airport(from)) || !g.containsVertex(airport(to)))
		{
			throw new FlyingPlannerException("The airport(s) does not exist in the graph");		
		}
		//Using DijkstraShortestPath, it will attempt to find the cheapest route (or edges of lowest weight) 
		//through the graph from the starting point to the finish
		GraphPath<Airport, Flight> result = DijkstraShortestPath.findPathBetween(g, airport(from), airport(to));
		
	try
	{	
		List<String> stops = new ArrayList<String>(); //A list used to store each stop of the shortest path
		
		List<String> flights = new ArrayList<String>(); // A list used to store each flight of the shortest path
		
		int duration = 0; //Stores the total air time of the path
		int connect = 0; //Stores the total connecting time of the path
		int totalTime = 0;
		int totalMinutes = 0;
		
		Iterator<Airport> APItr = result.getVertexList().iterator(); //Create a iterator of the resulting short path vertex list
		
		while(APItr.hasNext()) //While the iterator still has data
		{
			Airport tempStop = APItr.next(); //Create a new airport called tempStop and assign it the next element from the iterator
			
			stops.add(tempStop.getCode()); //Add the airport code of the tempStop to the stops list
		}
			
		Iterator<Flight> flItr = result.getEdgeList().iterator(); //Create an iterator of the list of edges from result
		
		while(flItr.hasNext()) //While the iterator still has data
		{
			Flight tempFlight = flItr.next(); //Assign the next flight to tempFlight
			
			flights.add(tempFlight.getFlightCode()); //Add the tempFlight code to the flights list
			duration += tempFlight.getDuration(); //Add the tempFlight duration 		
		}
					
		int numberOfHops = result.getVertexList().size()-1; //Number of airports visited, which is size of vertex list of the shortest path
		int totalCost = (int)result.getWeight(); // Total cost of flights , which is the total weight of the shortest path
		
		if(numberOfHops > 1)
		{
		ArrayList<Integer> Start = new ArrayList<Integer>();
		ArrayList<Integer> Finish = new ArrayList<Integer>();
		
        Flight temp = new Flight();        
        Iterator<Flight> itr2 = result.getEdgeList().iterator();
 
        while(itr2.hasNext()) 
        {
            temp = itr2.next();
            Start.add(Integer.parseInt(temp.getFromGMTime()));
            Finish.add(Integer.parseInt(temp.getToGMTime()));
        }
        
        for (int i = 1; i < Start.size(); i++) 
        {  	
            String finish = Integer.toString(Start.get(i));
            String start = Integer.toString(Finish.get(i-1));
            
            if(start.length() == 3)
            {
            	start = "0" + start;
            }
            if(start.length() == 2)
            {
            	start = "00" + start;
            }
            if(start.length() == 1)
            {
            	start = "000" + start;
            }
            
            if(finish.length() == 3)
            {
            	finish = "0" + finish;
            }
            if(finish.length() == 2)
            {
            	finish = "00" + finish;
            }
            if(finish.length() == 1)
            {
            	finish = "000" + finish;
            }
            
            String hours1 = start.substring(0,2);
    		int inthours1 = Integer.parseInt(hours1);	
    		String minutes1 = start.substring(2,4);
    		int intminutes1 = Integer.parseInt(minutes1);
    		
    		String hours2 = finish.substring(0,2);
    		int inthours2 = Integer.parseInt(hours2);	
    		String minutes2 = finish.substring(2,4);
    		int intminutes2 = Integer.parseInt(minutes2);
    		
    		int firstHour = inthours1;
    		int firstMin = intminutes1;
    		
    		int secondHour = inthours2;
    		int secondMin = intminutes2;
    		
    		int diffMin;
    		if(secondMin < firstMin)
    		{
    			diffMin = 60 + secondMin - firstMin;
    			if(secondHour == 0)
    			{
    				secondHour = 23;
    			}
    			else
    			{
    				secondHour--;
    			}
    		}
    		else
    		{
    			diffMin = secondMin - firstMin;
    		}
    		
    		int diffHour;
    		if(secondHour < firstHour)
    		{
    			diffHour = secondHour + 24 - firstHour;
    		}
    		else
    		{
    			diffHour = secondHour - firstHour;
    		}
    		
    		//total minutes
    		totalMinutes = diffHour * 60;
    		totalMinutes = totalMinutes + diffMin;
    		connect = connect + totalMinutes;     
        }
		
	} 
        
        if(numberOfHops == 1)
		{
			connect = 0;
			totalTime = duration;
		}
          
        totalTime = duration + connect;
        
        //Create a new journey from the resulting shortest path
		Journey journey = new Journey(stops, flights, numberOfHops, totalCost, duration, connect, totalTime);
	
		
		return journey; //Return journey
		
	  }
		//If a path does not exist, catch it and provide a error message
		catch (NullPointerException e)
		{ 
			throw new FlyingPlannerException("Could not find a path"); 
		}
	}


	/** Attempts to find the shortest path of least number of flights(or hops) from starting point to finish **/
	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException 
	{	
		if(!g.containsVertex(airport(from)) || !g.containsVertex(airport(to)))
		{
			throw new FlyingPlannerException("The airport(s) does not exist in the graph");		
		}	
		
		//Use DijkstraShortestPath to find the shortest path through the hop graph
		GraphPath<Airport, Flight> result = DijkstraShortestPath.findPathBetween(hopG, airport(from), airport(to));
		
	try 
	{	
		List<String> stops = new ArrayList<String>(); //A list used to store the stops of the path
		List<String> flights = new ArrayList<String>(); //A list used to store the flights of the path
		
		int duration = 0; //The total air time of the path
		int connect = 0; //The total connection time of the path
		int cost = 0; //The total cost of the path
		int totalTime = 0;
		int totalMinutes = 0;
		
		Iterator<Airport> APItr = result.getVertexList().iterator(); //Iterator for vertices of the shortest path
		
		while(APItr.hasNext()) //While the iterator still has data
		{
			Airport tempStop = APItr.next(); //Assign the next airport to a temporary airport holder
			
			stops.add(tempStop.getCode()); //Add the temporary airport code to the stops list
		}
			
		Iterator<Flight> FLItr = result.getEdgeList().iterator(); //Create an iterator of the list of edges from result
		
		while(FLItr.hasNext()) //While the iterator still has data
		{
			Flight tempFlight = FLItr.next(); //Assign the next flight to the temporary flight holder
				
			flights.add(tempFlight.getFlightCode()); //Add the temporary flight code to the flights list
			duration = duration + tempFlight.getDuration(); //Add the air time of the flight
			cost = cost + tempFlight.getCost(); //Add the cost of the temporary flight
		}
					
		int numberOfHops = result.getVertexList().size()-1; //Number of airports visited, which is the size of the vertex set -1
		
		if(numberOfHops > 1)
		{
			ArrayList<Integer> Start = new ArrayList<Integer>();
			ArrayList<Integer> Finish = new ArrayList<Integer>();
		
			Flight temp = new Flight();        
			Iterator<Flight> itr2 = result.getEdgeList().iterator();
        
        while(itr2.hasNext()) 
        {
            temp = itr2.next();
            Start.add(Integer.parseInt(temp.getFromGMTime()));
            Finish.add(Integer.parseInt(temp.getToGMTime()));
        }
        
        for (int i = 1; i < Start.size(); i++) 
        {
        	String finish = Integer.toString(Start.get(i));
            String start = Integer.toString(Finish.get(i-1));
            
            if(start.length() == 3)
            {
            	start = "0" + start;
            }
            if(start.length() == 2)
            {
            	start = "00" + start;
            }
            if(start.length() == 1)
            {
            	start = "000" + start;
            }
            
            if(finish.length() == 3)
            {
            	finish = "0" + finish;
            }
            if(finish.length() == 2)
            {
            	finish = "00" + finish;
            }
            if(finish.length() == 1)
            {
            	finish = "000" + finish;
            }
             
            String hours1 = start.substring(0,2);
    		int inthours1 = Integer.parseInt(hours1);	
    		String minutes1 = start.substring(2,4);
    		int intminutes1 = Integer.parseInt(minutes1);
    		
    		String hours2 = finish.substring(0,2);
    		int inthours2 = Integer.parseInt(hours2);	
    		String minutes2 = finish.substring(2,4);
    		int intminutes2 = Integer.parseInt(minutes2);
    		
    		int firstHour = inthours1;
    		int firstMin = intminutes1;
    		
    		int secondHour = inthours2;
    		int secondMin = intminutes2;
    		
    		int diffMin;
    		if(secondMin < firstMin)
    		{
    			diffMin = 60 + secondMin - firstMin;
    			if(secondHour == 0)
    			{
    				secondHour = 23;
    			}
    			else
    			{
    				secondHour--;
    			}
    		}
    		else
    		{
    			diffMin = secondMin - firstMin;
    		}
    		
    		int diffHour;
    		if(secondHour < firstHour)
    		{
    			diffHour = secondHour + 24 - firstHour;
    		}
    		else
    		{
    			diffHour = secondHour - firstHour;
    		}
    		
    		//total minutes
    		totalMinutes = diffHour * 60;
    		totalMinutes = totalMinutes + diffMin;
    		connect = connect + totalMinutes;     
        }
	}
		if(numberOfHops == 1)
		{
			connect = 0;
			totalTime = duration;
		}
          
        totalTime = duration + connect;
               
        //Create a new journey and pass it the required data
		Journey journey = new Journey(stops, flights, numberOfHops, cost, duration, connect,totalTime);
		
		return journey; //Return the journey	
	 }
	
		//If a path does not exist, catch it and provide an error message
		catch (NullPointerException e)
		{ 
			throw new FlyingPlannerException("Could not find a path");
		}
	}

	
	/** Attempts to find the cheapest path from starting point to finish, excluding a list of airports **/
	@Override
	public Journey leastCost(String from, String to, List<String> excluding)
	throws FlyingPlannerException 
	{	
		if(!g.containsVertex(airport(from)) || !g.containsVertex(airport(to)))
		{
			throw new FlyingPlannerException("The airport(s) does not exist in the graph");		
		}
		
		List<String> stops = new ArrayList<String>(); //A list of all the airports visited
		
		List<String> flights = new ArrayList<String>(); //A list of all the flights
		
		int duration = 0; //The total air time of the flights
		int connect = 0; //The total connecting time of the flights
		int totalTime = 0;
		int totalMinutes = 0;
		
		Graph<Airport, Flight> tempGraph = g; //Create a temporary graph as the original data shouldn't be altered
	
		Iterator<String> exItr = excluding.iterator(); //Create an iterator for the list excluding
		
		while(exItr.hasNext()) //While the iterator still has data
		{
			String airportToRemove = exItr.next(); //Assign the next element in the iterator to airportToRemove
 			
			Airport tempAirport = airport(airportToRemove); //Use the airport method to attempt to match the airport to one the exists in the graph
				
			if(g.containsVertex(tempAirport)) 
			{
			Set<Flight> tempEdge = tempGraph.edgesOf(tempAirport); //Get the flights of the new temporary airport
			tempGraph.removeAllEdges(tempEdge); //Remove all flights
			
			tempGraph.removeVertex(tempAirport); //Finally remove the airport from the graph
			}
		}		
	try
	  {	
		//Use DijkstraShortestPath to try and find the shortest path after removing the excluding airports from the graph
		GraphPath<Airport, Flight> result = DijkstraShortestPath.findPathBetween(tempGraph, airport(from), airport(to));
		
		Iterator<Airport> APItr = result.getVertexList().iterator(); //Create an iterator for the list of vertices from result
		
		while(APItr.hasNext()) //While the iterator still has data
		{
			Airport tempStop = APItr.next(); //Assign the next airport from the iterator to temporary stop
			
			stops.add(tempStop.getCode()); //Add the airport code from the temporary stop to the stops list
		}
		
	    Iterator<Flight> FLItr = result.getEdgeList().iterator(); //Create an iterator for the list of edges from result
		
	    while(FLItr.hasNext()) //While the iterator still has data
		{
			Flight tempFlight = FLItr.next(); //Assign the next flight from the iterator to temporary flight
			
			flights.add(tempFlight.getFlightCode()); //Add the flight code from temporary flight to the flights list
			duration = duration + tempFlight.getDuration(); //Add the duration of the flight
		}
	    
	    int numberOfHops = result.getVertexList().size() - 1; //Number of airports visited, -1 to not count the starting airport
		int totalCost = (int)result.getWeight(); // Total cost of all the flights of the shortest path
	    
	    
	if(numberOfHops > 1)
	{
		ArrayList<Integer> Start = new ArrayList<Integer>();
		ArrayList<Integer> Finish = new ArrayList<Integer>();
		
        Flight temp = new Flight();        
        Iterator<Flight> itr2 = result.getEdgeList().iterator();
        
        while(itr2.hasNext()) 
        {
            temp = itr2.next();
            Start.add(Integer.parseInt(temp.getFromGMTime()));
            Finish.add(Integer.parseInt(temp.getToGMTime()));
        }
        
        for (int i = 1; i < Start.size(); i++) 
        {
        	String finish = Integer.toString(Start.get(i));
            String start = Integer.toString(Finish.get(i-1));
            
            if(start.length() == 3)
            {
            	start = "0" + start;
            }
            if(start.length() == 2)
            {
            	start = "00" + start;
            }
            if(start.length() == 1)
            {
            	start = "000" + start;
            }
            
            if(finish.length() == 3)
            {
            	finish = "0" + finish;
            }
            if(finish.length() == 2)
            {
            	finish = "00" + finish;
            }
            if(finish.length() == 1)
            {
            	finish = "000" + finish;
            }
             
            String hours1 = start.substring(0,2);
    		int inthours1 = Integer.parseInt(hours1);	
    		String minutes1 = start.substring(2,4);
    		int intminutes1 = Integer.parseInt(minutes1);
    		
    		String hours2 = finish.substring(0,2);
    		int inthours2 = Integer.parseInt(hours2);	
    		String minutes2 = finish.substring(2,4);
    		int intminutes2 = Integer.parseInt(minutes2);
    		
    		int firstHour = inthours1;
    		int firstMin = intminutes1;
    		
    		int secondHour = inthours2;
    		int secondMin = intminutes2;
    		
    		int diffMin;
    		if(secondMin < firstMin)
    		{
    			diffMin = 60 + secondMin - firstMin;
    			if(secondHour == 0)
    			{
    				secondHour = 23;
    			}
    			else
    			{
    				secondHour--;
    			}
    		}
    		else
    		{
    			diffMin = secondMin - firstMin;
    		}
    		
    		int diffHour;
    		if(secondHour < firstHour)
    		{
    			diffHour = secondHour + 24 - firstHour;
    		}
    		else
    		{
    			diffHour = secondHour - firstHour;
    		}
    		
    		//total minutes
    		totalMinutes = diffHour * 60;
    		totalMinutes = totalMinutes + diffMin;
    		connect = connect + totalMinutes; 
        }
	}
	
	if(numberOfHops == 1)
	{
		connect = 0;
		totalTime = duration;
	}
      
    totalTime = duration + connect;
    
	//Create a new journey from the data
	Journey journey = new Journey(stops, flights, numberOfHops, totalCost, duration,connect,totalTime);
			
	 tempGraph = g; //Reset the tempGraph for the user interface
	
		return journey; //Return the journey
	 }
		//If the shortest path does not exist, catch it and provide an error
		catch (NullPointerException e)
		{ 
			throw new FlyingPlannerException("Could not find a path"); 
		}
	}

	
	/** Attempts to find the shortest path through the graph with the least number of hops, excluding a list of airports **/
	@Override
	public Journey leastHop(String from, String to, List<String> excluding)
	throws FlyingPlannerException 
	{	
		if(!g.containsVertex(airport(from)) || !g.containsVertex(airport(to)))
		{
			throw new FlyingPlannerException("The airport(s) does not exist in the graph");		
		}
		
		List<String> stops = new ArrayList<String>(); //List of airports visited
		List<String> flights = new ArrayList<String>(); //List of flights
		
		int duration = 0; //Total air time
		int connect = 0; //Total connection time
		int totalCost = 0; //The total cost of the path
		int totalTime = 0;
		int totalMinutes = 0;
		
		Graph<Airport, Flight> tempGraph = hopG; //Create a temporary graph so the original data is not changed
		
		Iterator<String> exItr = excluding.iterator(); //Create  and iterator for the excluding list
		
		while(exItr.hasNext()) //While the iterator still has data
		{
			String airportToRemove = exItr.next(); //Assign the next element from the list to airportToRemove
			
			Airport tempAirport = airport(airportToRemove); //Use the airport method to match the airport to one that is in the graph
			
			if(g.containsVertex(tempAirport))
			{
			Set<Flight> tempEdges = tempGraph.edgesOf(tempAirport); //Get the flights of the new temporary airport		
			tempGraph.removeAllEdges(tempEdges); //Remove all of the airports flights from the graph
			
			tempGraph.removeVertex(tempAirport); //Remove the temporary airport from the graph
			}
		}
		
		//Attempt to find the least hop path through the graph from one airport to another
		GraphPath<Airport, Flight> result = DijkstraShortestPath.findPathBetween(tempGraph, airport(from), airport(to));
		
	try
	{	
		Iterator<Airport> APItr = result.getVertexList().iterator(); //Create an iterator for the list of vertices from the result
		
		while(APItr.hasNext()) //While the iterator still has data
		{
			Airport tempStop = APItr.next(); //Assign the next airport to tempStop
			
			stops.add(tempStop.getCode()); //Add the airport code of tempStop to the stops list
		}
		
	    Iterator<Flight> FLItr = result.getEdgeList().iterator(); //Create an iterator for the list of edges from result
	    
		while(FLItr.hasNext()) //While the iterator still has data
		{
			Flight tempFlight = FLItr.next(); //Assign the next flight to tempFlight
			
			flights.add(tempFlight.getFlightCode()); //Add the flight code of tempFlight to the flights list
			
			duration = duration + tempFlight.getDuration(); //Add the duration of the flight
			totalCost = totalCost + tempFlight.getCost(); //Add the cost of the temporary flight
		}
		
		int numberOfHops = result.getVertexList().size() - 1; //Number of airports visited -1, to not count the starting airport
		//int totalCost = (int)result.getWeight(); // Total cost of flights
		
		
	if(numberOfHops > 1)
	{
		ArrayList<Integer> Start = new ArrayList<Integer>();
		ArrayList<Integer> Finish = new ArrayList<Integer>();
        Flight temp = new Flight();        
        Iterator<Flight> itr2 = result.getEdgeList().iterator();
        
        while(itr2.hasNext()) 
        {
            temp = itr2.next();
            Start.add(Integer.parseInt(temp.getFromGMTime()));
            Finish.add(Integer.parseInt(temp.getToGMTime()));
        }
        
        for (int i = 1; i < Start.size(); i++) 
        {
        	String finish = Integer.toString(Start.get(i));
            String start = Integer.toString(Finish.get(i-1));
            
            if(start.length() == 3)
            {
            	start = "0" + start;
            }
            if(start.length() == 2)
            {
            	start = "00" + start;
            }
            if(start.length() == 1)
            {
            	start = "000" + start;
            }
            
            if(finish.length() == 3)
            {
            	finish = "0" + finish;
            }
            if(finish.length() == 2)
            {
            	finish = "00" + finish;
            }
            if(finish.length() == 1)
            {
            	finish = "000" + finish;
            }
             
            String hours1 = start.substring(0,2);
    		int inthours1 = Integer.parseInt(hours1);	
    		String minutes1 = start.substring(2,4);
    		int intminutes1 = Integer.parseInt(minutes1);
    		
    		String hours2 = finish.substring(0,2);
    		int inthours2 = Integer.parseInt(hours2);	
    		String minutes2 = finish.substring(2,4);
    		int intminutes2 = Integer.parseInt(minutes2);
    		
    		int firstHour = inthours1;
    		int firstMin = intminutes1;
    		
    		int secondHour = inthours2;
    		int secondMin = intminutes2;
    		
    		int diffMin;
    		if(secondMin < firstMin)
    		{
    			diffMin = 60 + secondMin - firstMin;
    			if(secondHour == 0)
    			{
    				secondHour = 23;
    			}
    			else
    			{
    				secondHour--;
    			}
    		}
    		else
    		{
    			diffMin = secondMin - firstMin;
    		}
    		
    		int diffHour;
    		if(secondHour < firstHour)
    		{
    			diffHour = secondHour + 24 - firstHour;
    		}
    		else
    		{
    			diffHour = secondHour - firstHour;
    		}
    		
    		//total minutes
    		totalMinutes = diffHour * 60;
    		totalMinutes = totalMinutes + diffMin;
    		connect = connect + totalMinutes; 
        }
	}
	
	if(numberOfHops == 1)
	{
		connect = 0;
		totalTime = duration;
	}
      
    totalTime = duration + connect;
	
	
	//Create a new journey using data from result
	Journey journey = new Journey(stops, flights, numberOfHops, totalCost, duration,connect,totalTime);
			
	tempGraph = hopG; //Reset the tempGraph for the user interface
	
	return journey; //Return journey
		
	 }	
		//If a path does not exist through the graph, catch it and provide an error message
		catch (NullPointerException e)
		{ 
			throw new FlyingPlannerException("Could not find a path");
		}
	}


	/** Returns a set of airports that are directly connected(a flight in both directions) to the given airport **/
	@Override
	public Set<Airport> directlyConnected(Airport airport) 
	{
		Set<Airport> connected = new HashSet<>(); //Create a new set to store the directly connected airports
		
		for (Flight f : g.edgesOf(airport)) //For all flights of the given airport
		{	
			Airport oppositeAirport = Graphs.getOppositeVertex(g, f, airport);	//Get an opposite airport
			
			if(g.containsEdge(oppositeAirport, airport)) //Check if there is a flight from the opposite airport to the given airport
			{
				if(g.containsEdge(airport, oppositeAirport)) //Check if there is a flight from the given airport to the opposite airport
				{
				connected.add(oppositeAirport); //If both statements pass then the airports are directly connected
				}
			}
		}
		
		airport.setDicrectlyConnected(connected); //Pass the set to the airport class
		
		return connected; //Return the set of airports
	}


	/** For each airport in the graph, records its set of directly connected airports and its size **/
	@Override
	public int setDirectlyConnected() 
	{		
		Set<Airport> airports = g.vertexSet(); //Create a new set from all the airports in the graph
		
		Set<Airport> result; //A new set to store the result of directly connected airports
		
		int directlyConnectedAirports = 0; //Holds the sum of of the directly connected set sizes
		
		for(Airport a : airports) //For all airports stored in the airport set
		{
			result = directlyConnected(a); //Pass the airport to the directly connected method to find its directly connected airports	
			
			directlyConnectedAirports = directlyConnectedAirports + result.size(); //Add the resulting set size to directlyConnectedAirports
		}	
		
		return directlyConnectedAirports; //Return the sum of the sets
	}

	
	
	/**  **/
	@Override
	public int setDirectlyConnectedOrder() 
	{
		return 0;
	}

	
	/**  **/
	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) 
	{
		return null;
	}

	
	/**  **/
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException 
	{	
		return null;
	}

	
	/**  **/
	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException 
	{
		return null;
	}

	
	/**  **/
	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException 
	{
		return null;
	}

	
}