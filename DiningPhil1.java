public class DiningPhil1 {

  // a fork -- just provides an object
  static protected class Fork { 
    public int id;
    public Fork(int i) { id = i; }
  }

  // a philosopher
  static class Philosopher extends Thread {

    Fork left;
    Fork right;

    public Philosopher(int i, Fork left, Fork right) {
      id = i;
      this.left = left;
      this.right = right;
      //start();
    }

  public void run() {
      // think!
      synchronized (left) {
        synchronized (right) {
          // eat!
        }
      }
    }
  }
  
  static int nPhilosophers = 6;
  // the main method
  public static void main(String[] args) {
    if (args.length > 0){
      nPhilosophers = Integer.parseInt(args[0]);
    }
    
    //Verify.beginAtomic();
    Fork[] forks = new Fork[nPhilosophers];
    for (int i = 0; i < nPhilosophers; i++) {
      forks[i] = new Fork();
    }
    for (int i = 0; i < nPhilosophers; i++) {
      Philosopher p = new Philosopher(i,forks[i], forks[(i + 1) % nPhilosophers]);
      if(i == nPhilosophers -1 )
        p = new Philosopher(forks[(i + 1) % nPhilosophers], forks[i]); // n-1 philosopher is odd
      p.start();
      
    }
    //Verify.endAtomic();
  }
}