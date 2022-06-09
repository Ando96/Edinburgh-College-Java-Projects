package F28DA_CW2;

import java.util.Set;

public class Airport implements IAirportPartB, IAirportPartC 
{
	/** The unique code of an airport **/
	private String airportCode; 
	
	/** The city of the airport **/
	private String airportCity;
	
	/** The name of the airport **/
	private String airportName; 
	
	/** A set containing airports directly connected to the selected airport **/
	private Set<Airport>directlyConnected;
	
	/** The number of directly connected airports **/
	private int DicrectlyConnectedOrder;
	
	/** Empty airport constructor **/
	Airport(){}
	
	/** Constructor for a airport and its required fields **/
	Airport(String airportCode,String airportCity,String airportName)
	{
		this.airportCode = airportCode;
		this.airportCity = airportCity;
		this.airportName = airportName;
	}
	
	
	/** Return the code of an airport **/
	@Override
	public String getCode() 
	{
		return this.airportCode;
	}

	
	/** Return the name of an airport **/
	@Override
	public String getName() 
	{
		return this.airportName;
	}
	
	
	/** Return the city of an airport **/
	public String getCity() 
	{
		return this.airportCity;
	}

	
	/** Set the directly connected airports (airports that have a flight both to and from)**/
	@Override
	public void setDicrectlyConnected(Set<Airport> directlyConnected) 
	{
		this.directlyConnected = directlyConnected;
	}

	
	/** Return the set of directly connected airports **/
	@Override
	public Set<Airport> getDicrectlyConnected() 
	{	
		return this.directlyConnected;
	}


	/**  **/
	@Override
	public void setDicrectlyConnectedOrder(int order) 
	{
		this.DicrectlyConnectedOrder = order;
	}

	
	/** **/
	@Override
	public int getDirectlyConnectedOrder() 
	{
		return this.DicrectlyConnectedOrder;
	}
	
	
	/** toString method for displaying airport data **/
	 @Override
	 public String toString() 
	 {
		 return "Code: " + airportCode + "\nCity: " + airportCity + "\nName:" + airportName + "\n";	        
	 }
	 
}