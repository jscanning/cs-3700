package io.javabrains.unit2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import io.javabrains.unit1.Person;

public class Unit1ExerciseSolutionJava8 {

	public static void main(String[] args) {
		List<Person> people = Arrays.asList(
				new Person("Charles", "Dickens", 60),
				new Person("Jeremy", "Canning", 23),
				new Person("Thomas", "Carlyle", 53),
				new Person("Charlotte", "Bronte", 45),
				new Person("Matthew", "Arnold", 39)
				);
		
		// Step 1: Sort list by last name
		Collections.sort(people,(p1, p2) -> p1.getLastname().compareTo(p2.getLastname()));
		
		// Step 2: Create a method that prints all elements in the list
		printConditionally(people, p -> true);
		
		// Step 3: Create a method that prints all people with the last name beginning with 'C'
		printConditionally(people, p -> p.getLastname().startsWith("C"));
		
		printConditionally(people, p -> p.getFirstname().startsWith("C"));
	}

	private static void printConditionally(List<Person> people, Predicate<Person> predicate) {
		for(Person p : people)
			if(predicate.test(p))
				System.out.println(p);
		System.out.println();
	}

}
