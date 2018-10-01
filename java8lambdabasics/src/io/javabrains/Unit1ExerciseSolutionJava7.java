package io.javabrains;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Unit1ExerciseSolutionJava7 {

	public static void main(String[] args) {
		List<Person> people = Arrays.asList(
				new Person("Charles", "Dickens", 60),
				new Person("Jeremy", "Canning", 23),
				new Person("Thomas", "Carlyle", 53),
				new Person("Charlotte", "Bronte", 45),
				new Person("Matthew", "Arnold", 39)
				);
		
		// Step 1: Sort list by last name
		Collections.sort(people, new Comparator<Person>(){
			@Override
			public int compare(Person o1, Person o2) {
				return o1.getLastname().compareTo(o2.getLastname());
			}
		});
		
		
		// Step 2: Create a method that prints all elements in the list
		printAll(people);
		
		// Step 3: Create a method that prints all people with the last name beginning with 'C'
		printConditionally(people, new Condition(){

			@Override
			public boolean test(Person p) {
				return p.getLastname().startsWith("C");
			}
			
		});
	}

	private static void printConditionally(List<Person> people, Condition condition) {
		for(Person p : people)
			if(condition.test(p))
				System.out.println(p);
		System.out.println();
	}

	private static void printAll(List<Person> people) {
		for(Person p : people)
			System.out.println(p);
		System.out.println();
	}

}

interface Condition{
	boolean test(Person p);
}
