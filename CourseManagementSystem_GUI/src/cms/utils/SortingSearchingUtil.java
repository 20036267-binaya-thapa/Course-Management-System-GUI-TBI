package cms.utils;

import cms.Student;
import cms.Course;

import java.util.Comparator;
import java.util.List;

public class SortingSearchingUtil {

    // Sort students by name ascending
    public static void sortStudentsByName(List<Student> students) {
        students.sort(Comparator.comparing(Student::getName));
    }

    // Sort students by performance descending
    public static void sortStudentsByScoreDesc(List<Student> students) {
        students.sort(Comparator.comparing(Student::getPerformanceScore).reversed());
    }

    // Sort courses by name
    public static void sortCoursesByName(List<Course> courses) {
        courses.sort(Comparator.comparing(Course::getCourseName));
    }

    // Binary search student by ID (list must be sorted by ID)
    public static Student searchStudentById(List<Student> students, String id) {
        int left = 0, right = students.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = students.get(mid).getId().compareToIgnoreCase(id);
            if (cmp == 0) return students.get(mid);
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }
}
