/********************************************************
  * Mark Meadows
  * 02/05/2015
  * Implementation of Percolation 
  *******************************************************/

class Program {
    public static void main(String[] args) {
        Percolation myPerc = new Percolation(4);
        myPerc.open(1,3);
        myPerc.open(1,4);
        myPerc.open(2,3);
        myPerc.open(2,4);
    }
}

public class Percolation {
    private int N;
    private WeightedQuickUnionUF grid;
    private int[] openGrid;
    
    public Percolation(int N) {
        this.N = N;
        grid = new WeightedQuickUnionUF(N*N + 1);
        openGrid = new int[N*N + 1];
    }
    
    private int xyToNode(int x, int y) {
        return ((x-1) * N) + y;
    }
    
    public boolean isOpen(int i, int j) {
        safetyCheck(i, j);
        return openGrid[xyToNode(i, j)] != 0;
    }
    
    public boolean isFull(int i, int j) {
        return !isOpen(i, j);
    }
    
    public void open(int i, int j) {
        safetyCheck(i, j);
        int Node = xyToNode(i, j);
        System.out.println(Node);
        if(openGrid[Node] != 0) return;
        openGrid[Node] = -1;
        if(i > 1) {  //Join Up if not on top row
            if(isOpen(i-1, j)) {
                grid.union(Node, (Node - N));
                System.out.println("JoinedUP");
            };
        };
        
        if(i < N) {         //Joind Down if not bottom row
            if(isOpen(i+1, j)) {
                grid.union(Node, Node + N);
                System.out.println("JoinedDOWN");
            };
        };
        
        if(Node % N != 0) {    //Join Right if not right edge
            if(isOpen(i, j+1)) {
                grid.union(Node, Node + 1);
                System.out.println("JoinedRIGHT");
            };
        };
           
           if((Node - 1) % N != 0) {  //Join Left if not left edge
               if(isOpen(i, j-1)) {
                   grid.union(Node, Node - 1);
                   System.out.println("JoinedLEFT");
               };
        };
    }
    
    public void safetyCheck(int i, int j) {
        if(i <= 0 || i > N) throw new IndexOutOfBoundsException("row index i out of bounds");
        if(j <= 0 || j > N) throw new IndexOutOfBoundsException("row index j out of bounds");
    }
    
}