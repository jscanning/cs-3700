package io.javabrains.unit3;

import java.util.Arrays;
import java.util.List;

import io.javabrains.unit1.Person;

public class CollectionIterationExample {

	public static void main(String[] args) {
		
		List<Person> people = Arrays.asList(
				new Person("Charles", "Dickens", 60),
				new Person("Jeremy", "Canning", 23),
				new Person("Thomas", "Carlyle", 53),
				new Person("Charlotte", "Bronte", 45),
				new Person("Matthew", "Arnold", 39)
				);
		
		System.out.println("Using for loop");
		for (int i = 0; i < people.size(); i++) {
			System.out.println(people.get(i));
		}
		
		System.out.println("Using for in loop");
		for(Person p : people){
			System.out.println(p);
		}
		
		System.out.println("Using lambda for each loop");
		people.forEach(System.out::println);

	}

}
