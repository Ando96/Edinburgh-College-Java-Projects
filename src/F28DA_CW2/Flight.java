package F28DA_CW2;

public class Flight implements IFlight 
{
	/** The code of the flight **/
	private String flightCode;
	
	/** The starting airport or 'departure airport' **/
	private Airport start = new Airport();
	
	/** The departure time of the flight **/
	private String FromGMTime;
	
	/** The finishing airport or 'arrival airport' **/
	private Airport finish = new Airport();
	
	/** The arrival time of the flight **/
	private String ToGMTime;
	
	/** The cost of the flight **/
	private int flightCost;
	
	/** The air time of the flight, or how long it takes to get from start to finish **/
	private int flightAirTime;
	
	/** Empty flight constructor **/
	Flight(){}

	/** Constructor for a flight and the required data **/
	Flight(String flightCode, Airport start, String FromGMTime, Airport finish, String ToGMTime, int flightCost)
	{
		this.flightCode = flightCode; 
		this.start = start;
		this.FromGMTime = FromGMTime;
		this.finish = finish;
		this.ToGMTime = ToGMTime;
		this.flightCost = flightCost;
	}
	
	/** Returns the code of a flight **/
	@Override
	public String getFlightCode() 
	{
		return this.flightCode;
	}

	
	/** Returns the destination of a flight **/
	@Override
	public Airport getTo() 
	{
		return this.finish;
	}

	
	/** Returns the starting airport of a flight **/
	@Override
	public Airport getFrom() 
	{
		return this.start;
	}

	
	/** Returns the departing time of the flight **/
	@Override
	public String getFromGMTime() 
	{
		return this.FromGMTime;
	}

	
	/** Returns the arrival time of the flight **/
	@Override
	public String getToGMTime() 
	{
		return this.ToGMTime;
	}

	
	/** Returns the cost of the flight in **/
	@Override
	public int getCost() 
	{
		return this.flightCost;
	}
	
	
	/** Returns the total air time of the flight **/
	public int getDuration()
	{
		//int fromHours; //From hours
		//int fromMins; //From minutes
		//int toHours; //To hours
		//int toMins; //To minutes
		
		//Parse the Strings containing the times into integers
        //int fromTime = Integer.parseInt(this.getFromGMTime());
        //int toTime = Integer.parseInt(this.getToGMTime());
        
        //To handle cases were the input departure time is between 00:00 and 09:59
        //if(Integer.toString(fromTime).length() < 3)
        //{
        	//fromHours = 24;
        	//fromMins = fromTime % 60;
        //}
        //else
        //{
        	//Convert the numbers into hours and minutes
        	//fromHours = fromTime / 100;
        	//fromMins = (fromTime - fromHours * 100) % 60;
        //}
        
        //To handle cases were the input arrival time is between 00:00 and 09:59
        //if(Integer.toString(toTime).length() < 3)
        //{
        	//toHours = 24;
        	//toMins = toTime % 60;
        //}
        //else
        //{
        	//toHours = toTime / 100;
        	//toMins = (toTime - toHours * 100) % 60;
        //}
        
        
        //Find the total hours and minutes
        //int hours = toHours - fromHours;
        //int mins = toMins - fromMins;
        
        //Find the total minutes
        //int totalMinutes = (hours * 60) + mins;
        
        //Make flight air time equal to the result
        //this.flightAirTime = totalMinutes; 
        
		String start = this.getFromGMTime();
		String finish = this.getToGMTime();
		
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
		
		//diffHour + diffMin is the flight time
		
		
		//total minutes
		int totalMinutes = diffHour * 60;
		totalMinutes += diffMin;
		
		this.flightAirTime = totalMinutes;	
		
        return this.flightAirTime; //Return the air time
	}
	
	
	/** toString method for displaying flight data **/
	 @Override
	 public String toString() 
	 {
		 return "Code: " + flightCode + "\nStart: " + start + "\nDepart: " + FromGMTime + "\nfinish: " + finish 
				 + "\nArrive: " + ToGMTime + "\nFlight Cost: " + "£ "+flightCost + "\nAir Time: " + flightAirTime + "\n";	        
	 }
	
	
}