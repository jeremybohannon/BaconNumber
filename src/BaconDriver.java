import java.io.FileReader;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

import bridges.base.*;
	import bridges.connect.*;

	public class BaconDriver {
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
			ElementVisualizer yellow = new ElementVisualizer("yellow");
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
			
                try {
                    if (choice.equals("a")) {

                        System.out.println("Please enter starting Actor: ");
                        start = kb.next();

                        System.out.println("Please enter Actor to find: ");
                        end = kb.next();


                        //Instantiate variables
                        HashMap<String, String> parent = new HashMap<>();
                        HashMap<String, Integer> distance = new HashMap<>();
                        HashMap<String, Boolean> visited = new HashMap<>();

                        LQueue<String> queue = new LQueue<>();

                        SLelement<Edge<String>> state;

                        int count = 0;

                        //Begin queue loading
                        queue.enqueue(start);
                        distance.put(start, 0);
                        parent.put(start, start);

                        while (queue.frontValue() != null) {
                            if (queue.frontValue().equals(end)) {
                                System.out.println("Path Length: " + distance.get(end));
                                break;
                            }

                            String actor = queue.dequeue();

                            visited.put(actor, true);

                            state = graph.getAdjacencyList(actor);

                            while (state != null) {
                                String current = state.getValue().getVertex();

                                if (!visited.containsKey(current)) {
                                    queue.enqueue(current);

                                    visited.put(current, true);

                                    distance.put(current, distance.get(actor) + 1);

                                    parent.put(current, actor);

                                    if(!current.equals(end)) {
                                        graph.getVertices().get(current).setVisualizer(orange);
                                    }
                                }
                                state = state.getNext();
                            }
                            count++;
                        }

                        String parentS = "";
                        String end2 = end;
                        while (!parentS.equals(start)) {
                            parentS = parent.get(end2);
                            System.out.println(parentS);
                            graph.getVertices().get(parentS).setVisualizer(yellow);
                            end2 = parentS;
                        }


                    }
                }catch(NullPointerException e){
                    System.out.println("I'm sorry, that is not a valid search query");
                }
                graph.getVertices().get(start).setVisualizer(red);
                graph.getVertices().get(end).setVisualizer(red);

                //give Bridges the data structure to visualize
				bridges.setDataStructure(graph);

				// send the visualization to the server
				bridges.visualize();
			}
	    }
	}

//				//set the input to red to indicate what was chosen
//				graph.getVertices().get(name).setVisualizer(red);
//
//				//make a variable of the linked list
//				SLelement<Edge<String>> state = graph.getAdjacencyList(name);
//
//				//iterate through linked list changing the nodes connected to the selected to yellow to indicate connection
//				while(state != null){
//
//					//set node to yellow
//					String item = state.getValue().getVertex();
//					graph.getVertices().get(item).setVisualizer(yellow);
//
//					//get next node
//					state = state.getNext();
//				}
