package homework.hw2.Lecture4;

public class MsLunch { 

    @SuppressWarnings("unused")
	private long c1 = 0; 
    @SuppressWarnings("unused")
	private long c2 = 0; 
    private Object lock1 = new Object(); 
    private Object lock2 = new Object(); 

    public void inc1() { 
        synchronized(lock1) { 
            c1++; 
        } 
    } 

    public void inc2() { 
        synchronized(lock2) { 
            c2++; 
        } 
    } 
}
