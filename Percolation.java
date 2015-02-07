/********************************************************
  * Mark Meadows
  * 02/05/2015
  * Implementation of Percolation 
  *******************************************************/

class Program {
    public static void main(String[] args) {
        PercolationStats pc = new PercolationStats(2, 100);
        System.out.println(pc.mean());
        System.out.println(pc.stddev());
        System.out.println(pc.confidenceLo());
        System.out.println(pc.confidenceHi());
    }
}

public class Percolation {
    private int N;
    private WeightedQuickUnionUF grid;
    private int[] openGrid;
    
    public Percolation(int N) {
        this.N = N;
        grid = new WeightedQuickUnionUF(N*N + 2);
        openGrid = new int[N*N + 1];
        for(int i = 1; i<=N; i++) {                //Create virtual nodes
            grid.union(0, i);
            grid.union((N*(N-1)) + i, N*N+1);
        }
    }
    
    private int xyToNode(int x, int y) {
        return ((x-1) * N) + y;
    }
    
    public boolean isOpen(int i, int j) {
        safetyCheck(i, j);
        return openGrid[xyToNode(i, j)] != 0;
    }
    
    public boolean isFull(int i, int j) {
        safetyCheck(i, j);
        int aNode = xyToNode(i, j);
        return (isOpen(i, j) && grid.connected(0, aNode));
    }
    
    public boolean percolates() {
        return grid.connected(0,N*N+1);
    }
    
    public void open(int i, int j) {
        safetyCheck(i, j);
        int Node = xyToNode(i, j);
        if(openGrid[Node] != 0) return;
        openGrid[Node] = -1;
        if(i > 1) {  //Join Up if not on top row
            if(isOpen(i-1, j)) {
                grid.union(Node, (Node - N));
            };
        };
        
        if(i < N) {         //Joind Down if not bottom row
            if(isOpen(i+1, j)) {
                grid.union(Node, Node + N);
            };
        };
        
        if(Node % N != 0) {    //Join Right if not right edge
            if(isOpen(i, j+1)) {
                grid.union(Node, Node + 1);
            };
        };
           
           if((Node - 1) % N != 0) {  //Join Left if not left edge
               if(isOpen(i, j-1)) {
                   grid.union(Node, Node - 1);
               };
        };
    }
    
    public void safetyCheck(int i, int j) {
        if(i <= 0 || i > N) throw new IndexOutOfBoundsException("row index i out of bounds");
        if(j <= 0 || j > N) throw new IndexOutOfBoundsException("row index j out of bounds");
    }
    
}

class PercolationStats {
    private double ratio[];
    public PercolationStats(int N, int T) {
        ratio = new double[T];
        for(int ii=0; ii<T; ii++) {
            double counter = 0;
            Percolation aGrid = new Percolation(N);
            while(!aGrid.percolates()) {
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);
//System.out.print("("+i+", "+j+") ");
                if(!aGrid.isOpen(i, j)) {              //Open if not previously opened
                    aGrid.open(i, j);
                    counter++;
                };
            }
            ratio[ii] = counter / ((N*N));
        };
    }
    
    public double mean() {
        return StdStats.mean(ratio);
    }
    
    public double stddev() {
        return StdStats.stddev(ratio);
    }
    
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / (Math.sqrt(ratio.length)));
    }
    
    public double confidenceHi() {
    return mean() + ((1.96 * stddev()) / (Math.sqrt(ratio.length)));
    }
}
