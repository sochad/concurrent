public final class UnisexBathroomSC {
   public static void main (String args[]) {
      BathroomControllerSC c = new BathroomControllerSC(); 
      Man m1 = new Man(c,1);	Man m2 = new Man(c,2); Man m3 = new Man(c,3);
      Man m4 = new Man(c,4);	Man m5 = new Man(c,5);
      Woman w1 = new Woman(c,1);	Woman w2 = new Woman(c,2); Woman w3 = new Woman(c,3);
      Woman w4 = new Woman(c,4);	Woman w5 = new Woman(c,5);
      m1.start(); w1.start(); m2.start(); w2.start(); m3.start(); w3.start();
      m4.start(); m5.start(); w4.start(); w5.start();
      try {
        m1.join(); w1.join(); m2.join(); w2.join();
        m3.join(); w3.join();
        m4.join(); w4.join();
        m5.join(); w5.join();
      }
      catch (InterruptedException e) {}
   }
}

final class Man extends TDThread {
	private BathroomControllerSC c;
	private int ID;
	Man (BathroomControllerSC c, int ID) {super("Man"+ID); this.c = c; this.ID = ID; }
	public void run () {
		//System.out.println("Man"+ID+" running");
		c.mensroom(ID);
		//System.out.println("Man"+ID+" finished");
	}
}

final class Woman extends TDThread {
	private BathroomControllerSC c;
	private int ID;
	Woman (BathroomControllerSC c, int ID) {super("Woman"+ID); this.c = c; this.ID = ID; }
	public void run () {		
		//System.out.println("Woman"+ID+" running");
		c.ladiesroom(ID);
		//System.out.println("Woman"+ID+" finished");
	}
}

final class BathroomControllerSC extends monitorSC {

	/* declarations here */
	private volatile int numMen = 0;	// number of men in bathroom
    private volatile int numWomen = 0; // number of women in bathroom

    private volatile int signaledMen = 0;
    private volatile int signaledWomen = 0;
    private boolean men =false;
    private boolean women = false;

    private conditionVariable menQ = new conditionVariable("menQ");
    private conditionVariable womenQ = new conditionVariable("womenQ");

	public BathroomControllerSC() {super("BathroomControllerSC");}

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

		// code here
		while(numWomen > 0 ||numMen>=4||women) 
			menQ.waitC();
		++numMen;
		men=true;
		exerciseEvent("manEnter"+ID);
		//if (numWomen>0) System.out.println("Error: concurrent access");		
		exitMonitor();
	}

	private void manExit(int ID) {
		enterMonitor("manExit");
	
		// code here
		--numMen;
		
		if (numMen == 0 && signaledMen==0)	{
			men= false;
			womenQ.signalCall();
		}
		else
			menQ.signalC();

		exerciseEvent("manExit"+ID);
		exitMonitor();
	}

	private void womanEnter(int ID) {
		enterMonitor("womanEnter");
		exerciseEvent("womanRequest"+ID);
		
		// code here
		while(numMen > 0 || numWomen>=4||men)
			womenQ.waitC();
		++numWomen;
		women=true;
		exerciseEvent("womanEnter"+ID);
		//if (numMen>0) System.out.println("Error: concurrent access");
		exitMonitor();
	}

	private void womanExit(int ID) {
		enterMonitor("womanExit");	
		
		// code here
		--numWomen;
		
		if (numWomen == 0 && signaledWomen==0) {
			women=false;
			menQ.signalCall();
		}
		else
			womenQ.signalC();

		exerciseEvent("womanExit"+ID);
		exitMonitor();
	}
}

/*

C:\Users\som\Desktop\assignments\cs 706\ModernMultithreadingJavaJar>java -classpath .;ModernMultithreading.jar -Dmode=rt -DdeadlockDetection=on -DPVReduction=on -DsymmetryReduction=on RTDriver UnisexB
athroomSC
start:Thu Nov 05 17:45:27 EST 2015
Monitoring for Deadlock.
1/1
25/25
50/50
75/75
100/100
125/125
150/150
175/175
200/200
225/225
250/250
275/275
300/300
325/325
350/350
375/375
400/400
425/425
450/450
475/475
500/500
525/525
550/550
575/575
600/600
625/625
650/650
675/675
700/700
725/725
750/750
775/775
800/800
825/825
850/850
875/875
900/900
925/925
950/950
975/975
1000/1000
1025/1025
1050/1050
1075/1075
1100/1100
1125/1125
1150/1150
1175/1175
1200/1200
1225/1225
1250/1250
1275/1275
1300/1300
1325/1325
1350/1350
1375/1375
1400/1400
1425/1425
1450/1450
1475/1475
1500/1500
1525/1525
1550/1550
1575/1575
1600/1600
1625/1625
1650/1650
1675/1675
1700/1700
1725/1725
1750/1750
1775/1775
1800/1800
1825/1825
1850/1850
1875/1875
1900/1900
1925/1925
1950/1950
1975/1975
2000/2000
2025/2025
2050/2050
2075/2075
2100/2100
2125/2125
2150/2150
2175/2175
2200/2200
2225/2225
2250/2250
2275/2275
2300/2300
2325/2325
2350/2350
2375/2375
2400/2400
2425/2425
2450/2450
2475/2475
2500/2500
2525/2525
2550/2550
2575/2575
2600/2600
2625/2625
2650/2650
2675/2675
2700/2700
2725/2725
2750/2750
2775/2775
2800/2800
2825/2825
2850/2850
2875/2875
2900/2900
2925/2925
2950/2950
2975/2975
3000/3000
3025/3025
3050/3050
3075/3075
3100/3100
3125/3125
3150/3150
3175/3175
3200/3200
3225/3225
3250/3250
3275/3275
3300/3300
3325/3325
3350/3350
3375/3375
3400/3400
3425/3425
3450/3450
3475/3475
3500/3500
3525/3525
3550/3550
3575/3575
3600/3600
3625/3625
3650/3650
3675/3675
3700/3700
3725/3725
3750/3750
3775/3775
3800/3800
3825/3825
3850/3850
3875/3875
3900/3900
3925/3925
3950/3950
3975/3975
4000/4000
4025/4025
4050/4050
4075/4075
4100/4100
4125/4125
4150/4150
4175/4175
4200/4200
4225/4225
4250/4250
4275/4275
4300/4300
4325/4325
4350/4350
4375/4375
4400/4400
4425/4425
4450/4450
4475/4475
4500/4500
4525/4525
4550/4550
4575/4575
4600/4600
4625/4625
4650/4650
4675/4675
4700/4700
4725/4725
4750/4750
4775/4775
4800/4800
4825/4825
4850/4850
4875/4875
4900/4900
4925/4925
4950/4950
4975/4975
5000/5000
5025/5025
5050/5050
5075/5075
5100/5100
5125/5125
5150/5150
5175/5175
5200/5200
5225/5225
5250/5250
5275/5275
5300/5300
5325/5325
5350/5350
5375/5375
5400/5400
5425/5425
5450/5450
5475/5475
5500/5500
5525/5525
5550/5550
5575/5575
5600/5600
5625/5625
5650/5650
5675/5675
*/