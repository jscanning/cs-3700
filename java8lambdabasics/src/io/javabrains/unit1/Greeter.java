
package io.javabrains.unit1;

public class Greeter {

	public void greet(Greeting greeting){
		greeting.perform();
	}
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		
		Greeting lambdaGreeting = () -> System.out.println("Hello World from Lambda!");
		
		Greeting innerClassGreeting = new Greeting() {
			public void perform(){
				System.out.println("Hello World from inner class!");
			}
		};
		
		greeter.greet(lambdaGreeting);
		greeter.greet(innerClassGreeting);
	}

}