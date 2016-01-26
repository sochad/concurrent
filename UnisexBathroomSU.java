public final class UnisexBathroomSU {
   public static void main (String args[]) {
      BathroomControllerSU c = new BathroomControllerSU(); 
      Man m1 = new Man(c,1);	Man m2 = new Man(c,2); Man m3 = new Man(c,3);Man m4 = new Man(c,4);Man m5 = new Man(c,5);
      Woman w1 = new Woman(c,1);	Woman w2 = new Woman(c,2); Woman w3 = new Woman(c,3);Woman w4 = new Woman(c,4);Woman w5 = new Woman(c,5);
      m1.start(); w1.start(); m2.start(); w2.start(); m3.start(); w3.start();m4.start();w4.start();m5.start();w5.start();
      try {
        m1.join(); w1.join(); m2.join(); w2.join();
        m3.join(); w3.join();m4.join(); w4.join();m5.join(); w5.join();
      }
      catch (InterruptedException e) {}
   }
}

final class Man extends TDThread {
	private BathroomControllerSU c;
	private int ID;
	Man (BathroomControllerSU c, int ID) {super("Man"+ID); this.c = c; this.ID = ID; }
	public void run () {
		//System.out.println("Man"+ID+" running");
		c.mensroom(ID);
		//System.out.println("Man"+ID+" finished");
	}
}

final class Woman extends TDThread {
	private BathroomControllerSU c;
	private int ID;
	Woman (BathroomControllerSU c, int ID) {super("Woman"+ID); this.c = c; this.ID = ID; }
	public void run () {		
		//System.out.println("Woman"+ID+" running");
		c.ladiesroom(ID);
		//System.out.println("Woman"+ID+" finished");
	}
}

final class BathroomControllerSU extends monitorSU {

	/* declarations here */
	 private volatile int numWomen = 0;
	 private volatile int numMen = 0;
    private volatile boolean men = false;
     private volatile boolean women = false;
    private conditionVariable menQ = new conditionVariable("menQ"); 
    private conditionVariable womenQ = new conditionVariable("womenQ");
     private conditionVariable turn = new conditionVariable("turn"); 
    private conditionVariable bathroom = new conditionVariable("bathroom");

	public BathroomControllerSU() {super("BathroomControllerSU");}

	public void mensroom(int ID) {
		manEnter(ID); 
		//System.out.println("Man" + ID + " use bathroom");
		try{Thread.sleep(10);}catch(InterruptedException e){}; 
		manExit(ID); 
	}
	public void ladiesroom(int ID) {
		womanEnter(ID); 
		//System.out.println("Woman" + ID + " use bathroom");
		try{Thread.sleep(10);}catch(InterruptedException e){}; 
		womanExit(ID);
	}

	private void manEnter(int ID) {
		enterMonitor("manEnter");		
		exerciseEvent("manRequest"+ID);

		
		while (women||numMen==4||numWomen>0) {
			men = true;
		    menQ.waitC();
		}
		men=true;
		++numMen;
		

		exerciseEvent("manEnter"+ID);
		if (numWomen>0) System.out.println("Error: concurrent access");	
		menQ.signalC_and_exitMonitor();
	
			
		//exitMonitor();
	}

	private void manExit(int ID) {
		enterMonitor("manExit");
	
		--numMen;
		
		exerciseEvent("manExit"+ID);
		if (numMen==0) {
			 men = false;
		    womenQ.signalC_and_exitMonitor();
		    return;
		}else{
			menQ.signalC_and_exitMonitor();
			return;
		}
		
		
	}

	private void womanEnter(int ID) {
		enterMonitor("womanEnter");  
		exerciseEvent("womanRequest"+ID);
		
		while(men || numMen>0||numWomen==4) {
			women = true;
		    womenQ.waitC();
		}
		women=true;
		++numWomen;
		
		exerciseEvent("womanEnter"+ID);
		if (numMen>0) System.out.println("Error: concurrent access");
		womenQ.signalC_and_exitMonitor();
		//exitMonitor();
	}

	private void womanExit(int ID) {
		enterMonitor("womanExit");	
		
	
		--numWomen;
		
		exerciseEvent("womanExit"+ID);
		if (numWomen==0) {
			women=false;
		    menQ.signalC_and_exitMonitor();
		    return;
		}else{

			womenQ.signalC_and_exitMonitor();
return;
		}
		
		
	}
}
/*  output:
Reachability Testing completed.
  Executions:2640 / Sequences Collected:2640
  Elapsed time in minutes: 2.4677333333333333
  Elapsed time in seconds: 148.064
  Elapsed time in milliseconds: 148064
  */
