import java.io.FileReader;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

import bridges.base.*;
	import bridges.connect.*;

	public class BaconDriverPartTwo {
		static GraphAdjList<String, String> graph;
		
		public static void main(String[] args) throws Exception{
	    	
			//create the Bridges object
			Bridges<String, String> bridges = new Bridges<String, String>(50, 
								"934069953630", "jeremybohannon");

			//instantiate the data structure
			graph = new GraphAdjList<String, String>();
			
			//instantiate variables
			Scanner kb = new Scanner(System.in);
			Scanner text = null;
			Boolean flag = false, flagActor = false, flagMovie = false;
			String choice, name = null;
			
			//Here I ask the user to choose a small or large data set depending on what they want to test
			//I use a do while for error handling 
			do{
				//if it isnt an option 
				if(flag){
					System.out.println("Incorrect option, try again...");
				}
				
				System.out.print("(L)arge data set, or (S)mall dataset: ");
				choice = kb.next().toLowerCase();
				
				flag = (!choice.equals("l") && !choice.equals("s"));
			
			}while(flag);
			
			//Make flag false again just to be sure 
			flag = false;
			
			//Read in file depending on what the user asked for
			if(choice.equals("s")){
				//get small input
				text = new Scanner(new FileReader("small_imdb.txt"));
			} else if(choice.equals("l")){
				//get large input
				text = new Scanner(new FileReader("imdb.txt"));	
			}
			
			//Make choice null again just to be sure
			choice = null;
			
			//instantiate colors 
			ElementVisualizer bisque = new ElementVisualizer("bisque");
			ElementVisualizer red = new ElementVisualizer("red");
			ElementVisualizer aqua = new ElementVisualizer("aqua");
			ElementVisualizer lightsteelblue = new ElementVisualizer("lightsteelblue");
            ElementVisualizer orange = new ElementVisualizer("orange");
			
			//Read in each actor and movie pair and create the data set
			while(text.hasNext()){	
				//get actor and movie from file
				String actor = text.next();
				String movie = text.next();
			
				//if the graph doesn't contain the actor or movie, add it to the set 
				if(!graph.getVertices().containsKey(actor)){
					graph.addVertex(actor, "actor");
					graph.getVertices().get(actor).setVisualizer(lightsteelblue);
				}
				if(!graph.getVertices().containsKey(movie)){
					graph.addVertex(movie, "movie");
					graph.getVertices().get(movie).setVisualizer(lightsteelblue);
				}
				
				//Add a bidirectional edge 
				graph.addEdge(movie, actor, 0);
				graph.addEdge(actor, movie, 0);

			}
			//close file reader
			text.close();

            String start = "";
            String end = "";

			//create while true to let the user continue the program unless chosen otherwise 
			while(true){
				//do while to ensure that the user chooses an actor, movie, or to quit
				do{
					if(flag) System.out.println("Incorrect option, Please try again...");
					System.out.print("(A)ctor, (M)ovie, (Q)uit: ");
					choice = kb.next();
					choice.toLowerCase();
					flag = (!choice.equals("a") && !choice.equals("m") && !choice.equals("q"));
				}while (flag);
				
				//if the user chooses to quit, terminate the program... with style!
				if(choice.equals("q")){
					System.out.println("Terminating program...");
					Thread.sleep(4000);
					System.out.println("Goodbye.");
					System.exit(0);
				} 

                //Didn't have time to add individual error handling so try catch in case an input combination is invalid
                try {

                    System.out.println("Please enter starting Node: ");
                    start = kb.next();

                    if (choice.equals("a")) {
						System.out.println("Please enter Actor to find: ");
					} else {
						System.out.println("Please enter Movie to find: ");
					}
                    end = kb.next();

                        //Instantiate variables
                        //Parent hashmap to hold the parents of each node to use for path finding
                        HashMap<String, String> parent = new HashMap<>();
                        //Distance hasmap to hold the distance between nodes
                        HashMap<String, Integer> distance = new HashMap<>();
                        //Visited hasmap to check if a node has been visited
                        HashMap<String, Boolean> visited = new HashMap<>();
                        //Queue to use in BFS search
                        LQueue<String> queue = new LQueue<>();
                        //SLement to get linkedlist of nodes
                        SLelement<Edge<String>> state;

                        //Begin queue loading
                        queue.enqueue(start);
                        distance.put(start, 0);
                        parent.put(start, start);

                        //BFS search, while there is something in the queue
                        while (queue.frontValue() != null) {
                            //If the node in the queue is the node we are looking for
                            if (queue.frontValue().equals(end)) {
                                System.out.println("Path Length: " + distance.get(end));
                                break;
                            }

                            //Take current actor out of the queue
                            String actor = queue.dequeue();
                            //We visited the actor
                            visited.put(actor, true);
                            //Get linked list of the node
                            state = graph.getAdjacencyList(actor);

                            //While there is a node left in linked list
                            while (state != null) {
                                //get value in linked list
                                String current = state.getValue().getVertex();

                                //if we have yet to visit the node
                                if (!visited.containsKey(current)) {
                                    //add node into queue
                                    queue.enqueue(current);
                                    //We visited the node
                                    visited.put(current, true);
                                    //Record the distance of the node from current node
                                    distance.put(current, distance.get(actor) + 1);
                                    //Record the parent of the node
                                    parent.put(current, actor);
                                    //A really bad way to make sure we don't set the end node to orange
                                    if(!current.equals(end)) {
                                        graph.getVertices().get(current).setVisualizer(orange);
                                    }
                                }
                                //get next element in linked list
                                state = state.getNext();
                            }
                        }

                        //Instantiate variables
                        String parentS = "";
                        String currNode = end;

                        //While loop to find the path from starting node to end node
                        while (!parentS.equals(start)) {
                            //Get the parent node of the last node
                            parentS = parent.get(currNode);
                            //Set the node to aqua
                            graph.getVertices().get(parentS).setVisualizer(aqua);
                            //Set the current node to the parent of the last node
                            currNode = parentS;
                        }



                }catch(NullPointerException e){
                    System.out.println("I'm sorry, that is not a valid search query");
                }
                //Ensure the starting and end node are red
                graph.getVertices().get(start).setVisualizer(red);
                graph.getVertices().get(end).setVisualizer(red);

                //give Bridges the data structure to visualize
				bridges.setDataStructure(graph);

				// send the visualization to the server
				bridges.visualize();
			}
	    }
}

