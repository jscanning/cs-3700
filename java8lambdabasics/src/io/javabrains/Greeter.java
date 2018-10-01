package io.javabrains;

public class Greeter {

	public void greet(Greeting greeting){
		greeting.perform();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Greeter greeter = new Greeter();
		greeter.greet(new HelloWorldGreeting());
	}

}
