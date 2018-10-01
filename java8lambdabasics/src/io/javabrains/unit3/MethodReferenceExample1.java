package io.javabrains.unit3;

public class MethodReferenceExample1 {

	public static void main(String[] args) {

		Thread t0 = new Thread(MethodReferenceExample1::printMessage); 
		// MethodReferenceExample1::printMessage == () -> printMessage()
		t0.start();
	}

	
	public static void printMessage(){
		System.out.println("Hello");
	}
	
	
	
	
	
	
}
