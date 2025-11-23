package ca.jrvs.apps.practice.dataStructure;

public class Employee implements Comparable<Employee> {

  private int id;
  private String name;
  private int age;
  private long salary;


  // constructors + getters/setters ...
  public Employee(int id, String name, int age, long salary) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.salary = salary;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public long getSalary() {
    return salary;
  }

  public void setSalary(long salary) {
    this.salary = salary;
  }

  // ----------------------------
  // Comparable: natural ordering
  // ----------------------------
  @Override
  public int compareTo(Employee other) {
    // age ascending
    return Integer.compare(this.age, other.age);
  }

}
