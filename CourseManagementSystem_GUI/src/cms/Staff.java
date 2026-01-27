package cms;

/**
 * Staff subclass (inherits Person)
 * includes department or role.
 */
public class Staff extends Person {
    private String department;

    public Staff(String id, String name, int age, String department) {
        super(id, name, age);
        this.department = (department == null ? "Unknown" : department.trim());
    }

    @Override
    public String getType() { return "Staff"; }

    public String getDepartment() { return department; }
    public void setDepartment(String dept) { this.department = dept; }

    // CSV: id,name,age,Staff,department,0
    public String toCSV() {
        return id + "," + escapeComma(name) + "," + age + "," + getType() + "," + escapeComma(department) + ",0";
    }

    private String escapeComma(String s) {
        return s == null ? "" : s.replace(",", "");
    }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department;
    }
}
