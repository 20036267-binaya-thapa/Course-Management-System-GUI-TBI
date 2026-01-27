package cms;

import java.util.ArrayList;

/**
 * Course class stores course details & enrolled student IDs.
 */
public class Course {
    private String courseId;
    private String courseName;
    private int capacity;
    private String instructorId; // staff id
    private ArrayList<String> enrolledStudentIds;

    public Course(String courseId, String courseName, int capacity, String instructorId) {
        this.courseId = courseId.trim();
        this.courseName = courseName.trim();
        this.capacity = Math.max(1, capacity);
        this.instructorId = (instructorId == null ? "" : instructorId.trim());
        this.enrolledStudentIds = new ArrayList<>();
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCapacity() { return capacity; }
    public String getInstructorId() { return instructorId; }

    public ArrayList<String> getEnrolledStudentIds() { return enrolledStudentIds; }

    public boolean isFull() { return enrolledStudentIds.size() >= capacity; }

    public boolean enroll(String studentId) {
        if (studentId == null || studentId.isBlank()) return false;
        if (isFull()) return false;
        if (enrolledStudentIds.contains(studentId)) return false;
        enrolledStudentIds.add(studentId);
        return true;
    }

    public boolean removeEnrollment(String studentId) {
        return enrolledStudentIds.remove(studentId);
    }

    // CSV: courseId,courseName,capacity,instructorId,studentA;studentB;studentC
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(courseId).append(",").append(escapeComma(courseName)).append(",").append(capacity)
          .append(",").append(instructorId).append(",");
        for (int i = 0; i < enrolledStudentIds.size(); i++) {
            if (i > 0) sb.append(";");
            sb.append(enrolledStudentIds.get(i));
        }
        return sb.toString();
    }

    private String escapeComma(String s) { return s == null ? "" : s.replace(",", ""); }

    @Override
    public String toString() {
        return courseId + " | " + courseName + " | Capacity: " + capacity + " | Instructor: " + instructorId +
               " | Enrolled: " + enrolledStudentIds.size();
    }
}
