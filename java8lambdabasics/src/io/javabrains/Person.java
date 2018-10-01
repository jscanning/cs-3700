package io.javabrains;

public class Person {
	private String firstname, lastname;
	private int age;
	
	public Person(String first, String last, int age){
		super();
		this.setFirstname(first);
		this.setLastname(last);
		this.setAge(age);
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString(){
		return "Person [Firstname= " + firstname +", Lastname= " + lastname + ", Age= " + age + " ]";
	}
	
}
