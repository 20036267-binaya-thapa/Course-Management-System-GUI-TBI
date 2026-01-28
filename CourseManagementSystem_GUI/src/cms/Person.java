package cms;

/**
 * Abstract base class for Person (demonstrates abstraction & inheritance)
 */
public abstract class Person {
    protected String id;
    protected String name;
    protected int age;

    public Person(String id, String name, int age) {
        this.id = id.trim();
        this.name = name.trim();
        this.age = Math.max(0, age);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name.trim(); }
    public void setAge(int age) { this.age = Math.max(0, age); }

    // Polymorphic type
    public abstract String getType();

    @Override
    public String toString() {
        return id + " | " + name + " | Age: " + age + " | Type: " + getType();
    }
}
