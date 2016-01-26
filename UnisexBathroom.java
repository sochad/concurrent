import java.util.concurrent.Semaphore;

public final class UnisexBathroom {
	public static void main (String args[]) {
		BathroomController c = new BathroomController(); 
		Man m1 = new Man(c,1);	
		Man m2 = new Man(c,2); 
		Man m3 = new Man(c,3);
		Woman w1 = new Woman(c,1);	
		Woman w2 = new Woman(c,2); 
		Woman w3 = new Woman(c,3);
		m1.start(); w1.start(); m2.start(); w2.start(); m3.start(); w3.start();
		try {
			m1.join(); w1.join(); m2.join(); w2.join();
			m3.join(); w3.join(); 
		}
		catch (InterruptedException e) {}
	}
}

final class Man extends TDThread {
	private BathroomController c;
	private int num;
	Man (BathroomController c, int num) {super("Man"+num); this.c = c; this.num = num; }
	public void run () {
		System.out.println("Man"+num+" running");
		c.mensroom(num);
		System.out.println("Man"+num+" finished");
	}
}

final class Woman extends TDThread {
	private BathroomController c;
	private int num;
	Woman (BathroomController c, int num) {super("Woman"+num); this.c = c; this.num = num; }
	public void run () {		
		System.out.println("Woman"+num+" running");
		c.ladiesroom(num);
		System.out.println("Woman"+num+" finished");
	}
}

final class BathroomController {
   /* declare variables and countingSemaphores here */
	int numMen = 0;	// number of men in bathroom
	int numWomen = 0; // number of women in bathroom

	private countingSemaphore mutexMen = new countingSemaphore(1);
	private countingSemaphore mutexWomen = new countingSemaphore(1);
	private countingSemaphore bathroomEmpty = new countingSemaphore(1);

	public BathroomController() { }

	public void mensroom(int num) {
		manEnter(); 
		
		System.out.println("Man" + num + " use bathroom");
		//try{Thread.sleep(10);}catch(InterruptedException e){}; 
		
		manExit(); 
	}
	public void ladiesroom(int num) {
		womanEnter(); 
		
		System.out.println("Woman" + num + " use bathroom");  
		//try{Thread.sleep(10);}catch(InterruptedException e){}; 
		
		womanExit();
	}

	private void manEnter() {
		if(numMen ==0)
      bathroomEmpty.P();
    mutexMen.P();
    numMen = numMen + 1;
	
		if (numWomen>0) System.out.println("Error: concurrent access");
		mutexMen.V();		
	}

	private void manExit() {
		mutexMen.P();
		numMen = numMen -1;
		if(numMen == 0)
			bathroomEmpty.V();
		mutexMen.V();
	}

	private void womanEnter() {
		if(numWomen == 0)
      bathroomEmpty.P();
    mutexWomen.P();
    numWomen = numWomen + 1;
		if (numMen>0) System.out.println("Error: concurrent access");
		mutexWomen.V();
	}

	private void womanExit() {
		mutexWomen.P();
		numWomen = numWomen - 1;
		if(numWomen == 0)
			bathroomEmpty.V();
		mutexWomen.V();
	}
}


/*

Reachability Testing completed.
  Executions:720 / Sequences Collected:720
  Elapsed time in minutes: 0.0668
  Elapsed time in seconds: 4.008
  Elapsed time in milliseconds: 4008

*/

