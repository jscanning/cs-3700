package io.javabrains.unit2;

public class ThisReferenceExample {
	
	public void doProcess(int i, Process p){
		p.process(i);
	}
	
	public void execute(){
		// this == ?
		doProcess(10, i -> {
			System.out.println("Value of i is " + i);
			System.out.println(this);
		});
	}
	
	public static void main(String[] args) {
		ThisReferenceExample thisReferenceExample = new ThisReferenceExample();
		
		/*thisReferenceExample.doProcess(10, i -> {
			System.out.println("Value of i is " + i);
			//System.out.println(this); this will not work
		});*/
		
		thisReferenceExample.execute();
		
	}
	
	public String toString(){
		return "this is the main ThisReferenceExample class";
	}
}
