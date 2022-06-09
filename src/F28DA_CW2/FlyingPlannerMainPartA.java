package F28DA_CW2;

import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

public class FlyingPlannerMainPartA 
{
	public static void main(String[] args) 
	{	
		// The following code is from HelloJGraphT.java of the org.jgrapth.demo package
		
		System.err.println("The example code is from HelloJGraphT.java from the org.jgrapt.demo package.");
		System.err.println("Use similar code to build the small graph from Part A by hand.");
		System.err.println("Note that you will need to use a different graph class as your graph is not just a Simple Graph.");
		System.err.println("Once you understand how to build such graph by hand, move to Part B to build a more substantial graph.");
		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)
        Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        
        String v1 = "Edinburgh"; //Edinburgh
        String v2 = "Heathrow"; //Heathrow
        String v3 = "Dubai"; //Dubai
        String v4 = "Kuala Lumpur"; //Kuala Lumpur 

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        
        // add edges to create a circuit
        DefaultWeightedEdge edge1 = g.addEdge(v1, v2);
        DefaultWeightedEdge edge2 = g.addEdge(v2, v3);
        DefaultWeightedEdge edge3 = g.addEdge(v3, v4);
        DefaultWeightedEdge edge4 = g.addEdge(v4, v1);
        
        DefaultWeightedEdge edge5 = g.addEdge(v2, v1);
        DefaultWeightedEdge edge6 = g.addEdge(v3, v2);
        DefaultWeightedEdge edge7 = g.addEdge(v4, v3);
        DefaultWeightedEdge edge8 = g.addEdge(v1, v4);
        
        // add weight to edges
        g.setEdgeWeight(edge1,80);       
        g.setEdgeWeight(edge2,90);       
        g.setEdgeWeight(edge3,140);       
        g.setEdgeWeight(edge4,130);   
        
        g.setEdgeWeight(edge5,80);     
        g.setEdgeWeight(edge6,90);      
        g.setEdgeWeight(edge7,140);
        g.setEdgeWeight(edge8,130);
           
        // note undirected edges are printed as: {<v1>,<v2>}
        //System.out.println("-- toString output");
        // @example:toString:begin
        //System.out.println(g.toString());
        // @example:toString:end
        //System.out.println();
		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)   
        
        Scanner scn = new Scanner(System.in); 
        System.out.println("The following airports are used... ");
        System.out.println(g.vertexSet());
        
        System.out.println("Please enter starting airport");
        String source = scn.nextLine();
        
        System.out.println("Please enter the destination airport");
        String destination = scn.nextLine();
        scn.close();
        
        System.out.println("Shortest (i.e. cheapest) path:");
        GraphPath<String, DefaultWeightedEdge> result = DijkstraShortestPath.findPathBetween(g, source, destination);   
        System.out.println(result);
        
        System.out.println("£ " + result.getWeight());
        
	}
}
