package cms;

/**
 * Student subclass (inherits Person)
 * includes performanceScore for evaluations and major for information
 */
public class Student extends Person {
    private String major;
    private double performanceScore;

    public Student(String id, String name, int age, String major, double performanceScore) {
        super(id, name, age);
        this.major = (major == null ? "" : major.trim());
        this.performanceScore = Math.max(0.0, performanceScore);
    }

    @Override
    public String getType() { return "Student"; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = (major == null ? "" : major.trim()); }

    public double getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(double performanceScore) { this.performanceScore = Math.max(0.0, performanceScore); }

    // CSV: id,name,age,Student,major,score
    public String toCSV() {
        return id + "," + escapeComma(name) + "," + age + "," + getType() + "," + escapeComma(major) + "," + performanceScore;
    }

    private String escapeComma(String s) {
        return s == null ? "" : s.replace(",", ""); // keep simple
    }

    @Override
    public String toString() {
        return super.toString() + " | Major: " + major + " | Score: " + performanceScore;
    }
}
