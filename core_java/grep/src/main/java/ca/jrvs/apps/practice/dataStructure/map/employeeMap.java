package ca.jrvs.apps.practice.dataStructure.map;

import java.util.Objects;

public class employeeMap {
  private int id;
  private String firstName;
  private String lastName;

  public employeeMap(int id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getId() { return id; }
  public String getFirstName() { return firstName; }
  public String getLastName() { return lastName; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof employeeMap)) return false;
    employeeMap employee = (employeeMap) o;
    return id == employee.id &&
        Objects.equals(firstName, employee.firstName) &&
        Objects.equals(lastName, employee.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName);
  }

  @Override
  public String toString() {
    return firstName + " " + lastName + "(id=" + id + ")";
  }
}

