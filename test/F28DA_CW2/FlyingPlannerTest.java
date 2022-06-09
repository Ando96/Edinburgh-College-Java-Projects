package F28DA_CW2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class FlyingPlannerTest 
{
	FlyingPlanner fi;

	@Before
	public void initialize() 
	{
		fi = new FlyingPlanner();
		try 
		{
			fi.populate(new FlightsReader());
		} 
		catch (FileNotFoundException | FlyingPlannerException e) 
		{
			e.printStackTrace();
		}
	}

	
	/**Test to see how the program handles a airport not in the graph
	can be caught if a check is done to see if the airport exists in the graph 
	before finding a the path **/
	@Test
	public void FakeAirport() 
	{
		try 
		{
			Journey i = fi.leastCost("EDI", "ABC");
			i.getStops();
			fail();
		} 
		catch (FlyingPlannerException e) 
		{
			assertTrue(true);
		}
	}
	
	
	/** Test to see how the program handles a path that does not exist **/
	@Test
	public void noPath() 
	{
		try 
		{
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
			Journey i = fi.leastCost("DXB", "EDI", exclude);
			fail();
		} 
		catch (FlyingPlannerException e) 
		{
			assertTrue(true);
		}
	}
	
	
	/** Test for number of hops **/
	@Test
	public void leastHopTestEDItoSYD() 
	{
		try 
		{
			Journey i = fi.leastHop("EDI", "SYD");
			assertEquals(3, i.totalHop());
		} catch (FlyingPlannerException e) 
		{
			fail();
		}
	}
	
	
	/** Test for lowest cost **/
	@Test
	public void leastCostTestEDItoSYD() 
	{
		try 
		{
			Journey i = fi.leastCost("EDI", "SYD");
			assertEquals(1000, i.totalCost());
		} catch (FlyingPlannerException e) 
		{
			fail();
		}
	}
	
	
		/**Test for lowest cost, total hops and total time excluding some airports **/
		@Test
		public void leastCostExclTestEDItoSYD() 
		{
			try 
			{
				LinkedList<String> exclude = new LinkedList<String>();
				exclude.add("BKK");
				exclude.add("AMS");
				exclude.add("LHR");
				exclude.add("FRA");
				exclude.add("HKG");
				Journey i = fi.leastCost("EDI", "SYD",exclude);
				assertEquals(4, i.totalHop());
				assertEquals(2670, i.totalTime());
				assertEquals(1066, i.totalCost());
			} catch (FlyingPlannerException e) 
			{
				fail();
			}
		}
		
				/**Test for lowest hops, excluding some airports **/
				@Test
				public void leastHopExclTestDXBtoEDI() 
				{
					try 
					{
						LinkedList<String> exclude = new LinkedList<String>();
						exclude.add("BRU");
						exclude.add("AMS");
						exclude.add("LHR");
						exclude.add("IST");
						exclude.add("FRA");
						Journey i = fi.leastCost("DXB", "EDI",exclude);
						assertEquals(2, i.totalHop());
					} catch (FlyingPlannerException e) 
					{
						fail();
					}
				}
		
		
				/**Try to exclude airport that does not exist in graph **/
				@Test
				public void leastCostExclInvalidAirports() 
				{
					try 
					{
						LinkedList<String> exclude = new LinkedList<String>();
						exclude.add("ABC"); //Invalid 
						exclude.add("LHR");
						exclude.add("AMS");
						exclude.add("DXB");
						exclude.add("BRU");
						Journey i = fi.leastCost("EDI", "SYD",exclude);
						assertEquals(1016, i.totalCost());
					} catch (FlyingPlannerException e) 
					{
						fail();
					}
				}
				
							
				/** Try to exclude airport that does not exist in graph **/
				@Test
				public void leastHopExclInvalidAirports() 
				{
					try 
					{
						LinkedList<String> exclude = new LinkedList<String>();
						exclude.add("ABC"); //Invalid 
						exclude.add("LHR");
						exclude.add("AMS");
						exclude.add("DXB");
						exclude.add("BRU");
						Journey i = fi.leastHop("EDI", "SYD",exclude);
						assertEquals(3, i.totalHop());
						assertEquals(1339, i.totalCost());
					} catch (FlyingPlannerException e) 
					{
						fail();
					}
				}
		
		
		
		

}
