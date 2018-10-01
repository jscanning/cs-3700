package io.javabrains.unit3;

import java.util.Arrays;
import java.util.List;
//import java.util.stream.Stream;

import io.javabrains.unit1.Person;

public class StreamsExample1 {

	public static void main(String[] args) {
		List<Person> people = Arrays.asList(
				new Person("Charles", "Dickens", 60),
				new Person("Jeremy", "Canning", 23),
				new Person("Thomas", "Carlyle", 53),
				new Person("Charlotte", "Bronte", 45),
				new Person("Matthew", "Arnold", 39)
				);
		
		
		/* people.stream()
		.filter(p -> p.getLastname().startsWith("C"))
		.forEach(p -> System.out.println(p.getFirstname()));
		*/
		
		long count = people.parallelStream()
		.filter(p -> p.getLastname().startsWith("C"))
		.count();
		
		System.out.println(count);
		
	}

}
