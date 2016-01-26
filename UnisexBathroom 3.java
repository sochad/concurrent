import java.util.concurrent.Semaphore;

public final class UnisexBathroom {
    public static void main (String args[]) {
        BathroomController c = new BathroomController();
        Man m1 = new Man(c,1);
        Man m2 = new Man(c,2);
        Man m3 = new Man(c,3);
        Man m4 = new Man(c,4);
        Man m5 = new Man(c,5);
        Woman w1 = new Woman(c,1);
        Woman w2 = new Woman(c,2);
        Woman w3 = new Woman(c,3);
        Woman w4 = new Woman(c,4);
        Woman w5 = new Woman(c,5);
        m1.start(); w1.start(); m2.start(); w2.start(); m3.start(); w3.start();m4.start(); w4.start();m5.start(); w5.start();
        try {
            m1.join(); w1.join(); m2.join(); w2.join();
            m3.join(); w3.join(); m4.join(); w4.join();m5.join(); w5.join();
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
    
    private countingSemaphore mutexMen = new countingSemaphore(4);
    private countingSemaphore mutexWomen = new countingSemaphore(4);
    private countingSemaphore bathroomEmpty = new countingSemaphore(1);
    private countingSemaphore turn = new countingSemaphore(1);
    private boolean flag = false;
    
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
        
        if(numMen ==0){
            turn.P();
            flag = true;
            bathroomEmpty.P();
            turn.V();
        }
        if(flag){
            mutexMen.P();
             numMen++;
            if (numWomen>0) System.out.println("Error: concurrent access");
            mutexMen.V();
        }
        
       
        
        
        
    }
    
    private void manExit() {
        mutexMen.P();
        numMen --;
        if(numMen == 0)
            bathroomEmpty.V();
        mutexMen.V();
    }
    
    private void womanEnter() {
        
        if(numWomen == 0){
            turn.P();
            flag=false;
            bathroomEmpty.P();
            turn.V();
        }
        if(!flag){
            mutexWomen.P();
            numWomen++;
            if (numMen>0) System.out.println("Error: concurrent access");
            mutexWomen.V();
        }
       
        
    }
    
    private void womanExit() {
        mutexWomen.P();
        numWomen --;
        if(numWomen == 0)
            bathroomEmpty.V();
        mutexWomen.V();
    }
}