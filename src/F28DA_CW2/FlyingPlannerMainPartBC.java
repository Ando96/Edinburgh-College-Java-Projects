package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class FlyingPlannerMainPartBC 
{
	public static void main(String[] args) 
	{
		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try 
		{
			fi.populate(new FlightsReader());
			Scanner scn = new Scanner(System.in);
			int menuChoice = 99;
			int avoidNum;
			String startAirport;
			String endAirport;
			LinkedList<String> exclude = new LinkedList<String>();		
			
			exclude.add("LHR");
			exclude.add("PRG");
			exclude.add("LGW");
			exclude.add("LCY");
			exclude.add("DUS");
			exclude.add("FRA");
			exclude.add("WAW");
			exclude.add("AMS");
			exclude.add("CDG");
			exclude.add("IST");
			exclude.add("GLA");
			exclude.add("CWL");
			exclude.add("EWR");
			exclude.add("BOS");
			
					
			System.out.println("The user interface uses airport codes as input");
			System.out.println();
			
			while(menuChoice != 0)
			{
				System.out.println("Please select an option 1-5: \n 1: Cheapest flights \n 2: Cheapeast flights excluding"
						+ "\n 3: Shortest journey \n 4: Shortest journey excluding \n 5: Exit");
						
				menuChoice = Integer.parseInt(scn.nextLine());
				
				while(menuChoice > 5)
				{
					System.out.println("Please select an option 1-5: \n 1: Cheapest flights \n 2: Cheapeast flights excluding"
							+ "\n 3: Shortest journey \n 4: Shortest journey excluding \n 5: Exit");
							
					menuChoice = Integer.parseInt(scn.nextLine());
				}
				
				
			if(menuChoice == 1)
			{
				System.out.println("Please enter you starting airport code");
				startAirport = scn.nextLine();
				
				System.out.println("Please enter you destination airport");
				endAirport = scn.nextLine();
				
				System.out.println("The cheapest path is " + fi.leastCost(startAirport, endAirport));
							
			}
			if(menuChoice == 2)
			{
				System.out.println("Please enter you starting airport code");
				startAirport = scn.nextLine();
				
				System.out.println("Please enter you destination airport");
				endAirport = scn.nextLine();
				
				System.out.println("How many airports would you like to avoid");
				avoidNum = Integer.parseInt(scn.nextLine());
				
				System.out.println("Please enter the airport(s) you would like to avoid");				
						
				for(int i = 0; i < avoidNum; i++)
				{
					exclude.add(scn.nextLine());
				}
				
				System.out.println("The cheapest path avoiding the airports selected is " 
									+ fi.leastCost(startAirport, endAirport, exclude));
				
			}
			if(menuChoice == 3)
			{
				System.out.println("Please enter you starting airport code");
				startAirport = scn.nextLine();
				
				System.out.println("Please enter you destination airport");
				endAirport = scn.nextLine();
				
				System.out.println("The least hops path is " + fi.leastHop(startAirport, endAirport));
				
			}
			if(menuChoice == 4)
			{
				System.out.println("Please enter you starting airport code");
				startAirport = scn.nextLine();
				
				System.out.println("Please enter you destination airport");
				endAirport = scn.nextLine();
				
				System.out.println("How many airports would you like to avoid?");
				avoidNum = Integer.parseInt(scn.nextLine());
				
				System.out.println("Please enter the airport(s) you would like to avoid");				
				
					for(int i = 0; i < avoidNum; i++)
					{
						exclude.add(scn.nextLine());
					}
				
				System.out.println("The cheapest path is " + fi.leastHop(startAirport, endAirport, exclude));
			}
			if(menuChoice == 5)
			{
				System.out.println("Exiting program");
				scn.close();
				System.exit(0);
			}
		}	
			
		} 
		
		catch (FileNotFoundException | FlyingPlannerException e) 
		{
			e.printStackTrace();
		}
	}
}