package cms;

/**
 * Represents an enrollment record (optional - used to display student & course).
 */
public class Enrollment {
    private String studentId;
    private String courseId;

    public Enrollment(String studentId, String courseId) {
        this.studentId = studentId.trim();
        this.courseId = courseId.trim();
    }

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }

    @Override
    public String toString() {
        return "Student: " + studentId + " -> Course: " + courseId;
    }
}
