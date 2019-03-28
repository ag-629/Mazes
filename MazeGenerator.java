import java.lang.Math;
import java.util.Random;

public class MazeGenerator {

    public void run(int n) {

	// creates all cells
	Cell[][] mazeMap = new Cell[n][n];
	initializeCells(mazeMap);

	// create a list of all internal walls, and links the cells and walls
	Wall[] walls = getWalls(mazeMap);

	createMaze(walls,mazeMap);

	printMaze(mazeMap);

    }

    // print out the maze in a specific format
    public void printMaze(Cell[][] maze) {
	for(int i = 0; i < maze.length; i++) {
	    // print the up walls for row i
	    for(int j = 0; j < maze.length; j++) {
		Wall up = maze[i][j].up;
		if(up != null && up.visible) System.out.print("+--");
		else System.out.print("+  ");
	    }
	    System.out.println("+");

	    // print the left walls and the cells in row i
	    for(int j = 0; j < maze.length; j++) {
		Wall left = maze[i][j].left;
		if(left != null && left.visible) System.out.print("|  ");
		else System.out.print("   ");
	    }

	    //print the last wall on the far right of row i
	    Wall lastRight = maze[i][maze.length-1].right;
	    if(lastRight != null && lastRight.visible) System.out.println("|");
	    else System.out.println(" ");
	}

	// print the last row's down walls
	for(int i = 0; i < maze.length; i++) {
	    Wall down = maze[maze.length-1][i].down;
	    if(down != null && down.visible) System.out.print("+--");
	    else System.out.print("+  ");
	}
	System.out.println("+");


    }

    // create a new Cell for each position of the maze
    public void initializeCells(Cell[][] maze) {
	for(int i = 0; i < maze.length; i++) {
	    for(int j = 0; j < maze[0].length; j++) {
		maze[i][j] = new Cell();
	    }
	}
    }

    // create all walls and link walls and cells
    public Wall[] getWalls(Cell[][] mazeMap) {

	int n = mazeMap.length;

	Wall[] walls = new Wall[2*n*(n+1)];
	int wallCtr = 0;

	// each "inner" cell adds its right and down walls
	for(int i = 0; i < n; i++) {
	    for(int j = 0; j < n; j++) {
		// add down wall
		if(i < n-1) {
		    walls[wallCtr] = new Wall(mazeMap[i][j], mazeMap[i+1][j]);
		    mazeMap[i][j].down = walls[wallCtr];
		    mazeMap[i+1][j].up = walls[wallCtr];
		    wallCtr++;
		}
		
		// add right wall
		if(j < n-1) {
		    walls[wallCtr] = new Wall(mazeMap[i][j], mazeMap[i][j+1]);
		    mazeMap[i][j].right = walls[wallCtr];
		    mazeMap[i][j+1].left = walls[wallCtr];
		    wallCtr++;
		}
	    }
	}

	// "outer" cells add their outer walls
	for(int i = 0; i < n; i++) {
	    // add left walls for the first column
	    walls[wallCtr] = new Wall(null, mazeMap[i][0]);
	    mazeMap[i][0].left = walls[wallCtr];
	    wallCtr++;

	    // add up walls for the top row
	    walls[wallCtr] = new Wall(null, mazeMap[0][i]);
	    mazeMap[0][i].up = walls[wallCtr];
	    wallCtr++;

	    // add down walls for the bottom row
	    walls[wallCtr] = new Wall(null, mazeMap[n-1][i]);
	    mazeMap[n-1][i].down = walls[wallCtr];
	    wallCtr++;

	    // add right walls for the last column
	    walls[wallCtr] = new Wall(null, mazeMap[i][n-1]);
	    mazeMap[i][n-1].right = walls[wallCtr];
	    wallCtr++;
	}


	return walls;
    }
    /*In the MazeGenerator class, write a method called createMaze that takes an array of
	Walls and a 2-dimensional array of Cells as input and produces a maze. Your method
	should not return anything; the Cells and Walls in the input arrays should contain all
	information needed to display the maze (which you can do by calling my print method).*/
    void createMaze(Wall[] walls, Cell[][] cells){
    	//remove the openings at the top left and bottom right
    	cells[0][0].left.visible = false;
    	cells[cells.length - 1][cells.length - 1].right.visible = false;
    	//how many sets to be made
    	int setCount = 0;
    	UnionFind sets = new UnionFind();
    	//make each cell a singleton set to begin
    	for(int i = 0; i < cells.length; i += 1){
    		for(int j = 0; j < cells.length; j += 1){
    			sets.makeset(cells[i][j]);
    			//after this loop is finished this will store how many sets were made
    			setCount += 1;
    		}
    	}
    	//go until there is one set
    	while(setCount > 1){
    		Random rand = new Random();
    		int wallsIndex = rand.nextInt(walls.length);
    		//grab a random wall
    		Wall w = walls[wallsIndex];
    		//check to see if that cell is on the outer edge
    		if(w.first != null && w.second != null){
    			/*if that cell is not on the outer edge
    			  check to see if the 2 cells on either side 
    			  of the wall are already joined into the same set
    			  if not, turn off the visibility
    			  */
    			if(sets.find(w.first) != sets.find(w.second)){
    				w.visible = false;
    				//union the sets
    				sets.union(w.first,w.second);
    				//decrementing the set count
    				setCount -= 1;
    			}
    		}
    	}
	}
	public static void main(String [] args) {
		if(args.length > 0) {
	    	int n = Integer.parseInt(args[0]);
	    	new MazeGenerator().run(n);
		}
		else new MazeGenerator().run(5);
    	}
}	