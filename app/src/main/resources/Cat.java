package resources;

public class Cat implements Animal{
	public Cat(){
		System.out.println("Meow");
		walk();
	}
	@Override
	public void walk()
	{
		System.out.println("Walking");
	}
}
