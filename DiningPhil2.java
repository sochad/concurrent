public class DiningPhil2 {

  static protected class Fork { 
    public int id;
    public Fork(int i) { id = i; }
  }

static protected class Steward {
    /** the number or philosophers sitting at the table currently */
    int sitting;

    /** the max number of philosophers allowed to sit at any one time */
    int max;

    public Steward(int ms) { sitting = 0; max = ms; }

    /** this procedure is called by philosophers. It returns when the
     * calling philosopher is allowed by the steward to sit at the
     * table */
    synchronized void sitDown() {
      while(sitting == max) {
        try { wait(); }
        catch(InterruptedException e) {}
      }
      ++sitting;
    }

    /** this procedure informs the steward that the calling philosopher
     * is done eating and is leaving the table */
    synchronized void getUp() {
      --sitting;
      notify();
    }
  }
    static protected class Phil extends Thread
    {
      public Steward steward;
      public Fork leftFork,rightFork;
      public int id;

      public Phil(int i,Steward b,Fork lf,Fork rf) { 
        steward = b; 
        id = i; 
        leftFork = lf; 
        rightFork = rf; 
      }

      public void run()
      {
        while(true) {
          steward.sitDown();
          synchronized(leftFork) {
            synchronized(rightFork) {
              /** eat */
            }
          }
          steward.getUp();
        }
      }
    }

  static int nPhilosophers = 10;
  public static void main(String [] args)
  {
    if(args.length > 0) {
      Steward steward = new Steward(nPhilosophers);
      Fork [] forks = new Fork [nPhilosophers];
      Phil [] phils = new Phil [nPhilosophers];
      for(int i = 0;i < nPhilosophers;++i) forks[i] = new Fork(i);
      for(int i = 0;i < nPhilosophers;++i) phils[i] = new Phil(i,steward,forks[i],forks[(i + 1) % nPhilosophers]);
      for(int i = 0;i < nPhilosophers;++i) phils[i].start();
    } else System.out.println("usage: jpf DP <philosopher num>"); 
  }
}