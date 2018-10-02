package homework.hw2.Lecture5;

public class Deadlock { 

    static class Friend { 

        private final String name; 

        public Friend(String name) { 

            this.name = name; 

        } 

        public String getName() { 

            return this.name; 

        } 

        public synchronized void bow(Friend bower) { 

            System.out.format("%s: %s" 

                + "  has bowed to me!%n",  

                this.name, bower.getName()); 

            bower.bowBack(this); 

        } 

        public synchronized void bowBack(Friend bower) { 

            System.out.format("%s: %s" 

                + " has bowed back to me!%n", 

                this.name, bower.getName()); 

        } 

    } 

 

    public static void main(String[] args) { 

        final Friend alphonse = 

            new Friend("Alphonse"); 

        final Friend gaston = 

            new Friend("Gaston"); 

        new Thread(new Runnable() { 

            public void run() { alphonse.bow(gaston); } 

        }).start(); 

        new Thread(new Runnable() { 

            public void run() { gaston.bow(alphonse); } 

        }).start(); 

    } 

} 

//********************************************* 

//Guarded Blocks  



/*public void guardedJoy() { 

    // Simple loop guard. Wastes processor time. Don't do this! 

    while(!joy) {} 

    System.out.println("Joy has been achieved!"); 

}



//More effecient invoke(call) "Object.wait" to suspend thread until joy is set 

public synchronized void guardedJoy() { 

    // This guard only loops once for each special event, which may not 

    // be the event we're waiting for. 

    while(!joy) { 

        try { 

            wait(); 

        } catch (InterruptedException e) {} 

    } 

    System.out.println("Joy and efficiency have been achieved!"); 

} 

 

//Some other thread will invoke this method 

public synchronized void notifyJoy() { 

    joy = true; 

    notifyAll(); 

}}*/