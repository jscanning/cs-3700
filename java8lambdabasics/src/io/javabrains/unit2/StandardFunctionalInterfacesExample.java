package io.javabrains.unit2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import io.javabrains.unit1.Person;

public class StandardFunctionalInterfacesExample {

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
		performConditionally(people, p -> true, p -> System.out.println(p));
		
		// Step 3: Create a method that prints all people with the last name beginning with 'C'
		performConditionally(people, p -> p.getLastname().startsWith("C"), p -> System.out.println(p));
		
		performConditionally(people, p -> p.getFirstname().startsWith("C"), p -> System.out.println(p.getFirstname()));
	}

	private static void performConditionally(List<Person> people, Predicate<Person> predicate, Consumer<Person> consumer) {
		for(Person p : people)
			if(predicate.test(p))
				consumer.accept(p);
		System.out.println();
	}

}
