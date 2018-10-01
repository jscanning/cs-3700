package io.javabrains;

public class Greeter {

	public void greet(Greeting greeting){
		greeting.perform();
	}
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		Greeting helloWorldGreeting = new HelloWorldGreeting();
		greeter.greet(helloWorldGreeting);
		
		Greeting myLambdaFunction = () -> System.out.print("Hello World from Lambda!");
		
		greeter.greet(myLambdaFunction);
		
	}

}