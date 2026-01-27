package cms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * CourseManager contains collections and operations (CRUD, enrollments, evaluations).
 */
public class CourseManager {
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Staff> staff = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    // Evaluation queue (FIFO) commands like "R,S001,5.0" or "P,S001,2.5"
    private Queue<String> evaluationQueue = new LinkedList<>();

    // ---------------- Student & Staff ----------------
    public void addStudent(Student s) {
        if (findStudentById(s.getId()) == null) students.add(s);
    }

    public Student findStudentById(String id) {
        for (Student s : students) if (s.getId().equalsIgnoreCase(id)) return s;
        return null;
    }

    public List<Student> findStudentsByName(String name) {
        ArrayList<Student> res = new ArrayList<>();
        for (Student s : students) if (s.getName().equalsIgnoreCase(name)) res.add(s);
        return res;
    }

    public boolean updateStudent(String id, String newName, Integer newAge, String newMajor) {
        Student s = findStudentById(id);
        if (s == null) return false;
        if (newName != null && !newName.isBlank()) s.setName(newName);
        if (newAge != null) s.setAge(newAge);
        if (newMajor != null) s.setMajor(newMajor);
        return true;
    }

    public boolean deleteStudent(String id) {
        Student s = findStudentById(id);
        if (s == null) return false;
        // remove student from all courses
        for (Course c : courses) c.removeEnrollment(id);
        return students.remove(s);
    }

    public List<Student> getStudentsSortedByName() {
        List<Student> list = new ArrayList<>(students);
        list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return list;
    }
    
    public List<Student> getStudentsSortedByScore() {
        List<Student> list = new ArrayList<>(students);
        list.sort((a, b) -> Double.compare(b.getPerformanceScore(), a.getPerformanceScore()));
        return list;
    }    

    public void addStaff(Staff st) {
        if (findStaffById(st.getId()) == null) staff.add(st);
    }

    public Staff findStaffById(String id) {
        for (Staff st : staff) if (st.getId().equalsIgnoreCase(id)) return st;
        return null;
    }

    // ---------------- Courses & Enrollments ----------------
    public void addCourse(Course c) {
        if (findCourseById(c.getCourseId()) == null) courses.add(c);
    }

    public Course findCourseById(String id) {
        for (Course c : courses) if (c.getCourseId().equalsIgnoreCase(id)) return c;
        return null;
    }

    public boolean updateCourse(String id, String newName, Integer newCapacity, String newInstructorId) {
        Course c = findCourseById(id);
        if (c == null) return false;
        if (newName != null && !newName.isBlank()) {
            // reflect by replacing courseName via reflection? simpler: create new Course copy
            // But we keep it simple: remove and add new with same enrollments
            ArrayList<String> enrolled = c.getEnrolledStudentIds();
            int cap = (newCapacity == null ? c.getCapacity() : newCapacity);
            String instr = (newInstructorId == null ? c.getInstructorId() : newInstructorId);
            Course newC = new Course(id, (newName == null ? c.getCourseName() : newName), cap, instr);
            newC.getEnrolledStudentIds().addAll(enrolled);
            courses.remove(c);
            courses.add(newC);
        } else {
            if (newCapacity != null || newInstructorId != null) {
                int cap = (newCapacity == null ? c.getCapacity() : newCapacity);
                String instr = (newInstructorId == null ? c.getInstructorId() : newInstructorId);
                Course newC = new Course(id, c.getCourseName(), cap, instr);
                newC.getEnrolledStudentIds().addAll(c.getEnrolledStudentIds());
                courses.remove(c);
                courses.add(newC);
            }
        }
        return true;
    }

    public boolean deleteCourse(String id) {
        Course c = findCourseById(id);
        if (c == null) return false;
        return courses.remove(c);
    }

    public boolean enrollStudentInCourse(String studentId, String courseId) {
        Student s = findStudentById(studentId);
        Course c = findCourseById(courseId);
        if (s == null || c == null) return false;
        return c.enroll(studentId);
    }

    public boolean removeStudentFromCourse(String studentId, String courseId) {
        Course c = findCourseById(courseId);
        if (c == null) return false;
        return c.removeEnrollment(studentId);
    }

    // ---------------- Evaluation Queue ----------------
    public void scheduleEvaluation(String command) { evaluationQueue.offer(command); }

    public String processNextEvaluation() {
        String cmd = evaluationQueue.poll();
        if (cmd == null) return "No scheduled evaluations.";
        String[] p = cmd.split(",");
        if (p.length < 3) return "Malformed evaluation command: " + cmd;
        String action = p[0].trim();
        String sid = p[1].trim();
        double amt;
        try { amt = Double.parseDouble(p[2].trim()); } catch (NumberFormatException e) {
            return "Invalid amount in scheduled command: " + cmd;
        }
        Student s = findStudentById(sid);
        if (s == null) return "Student not found: " + sid;
        if (action.equalsIgnoreCase("R")) {
            s.setPerformanceScore(s.getPerformanceScore() + amt);
            return "Rewarded " + sid + " by " + amt + ". New score: " + s.getPerformanceScore();
        } else {
            s.setPerformanceScore(Math.max(0, s.getPerformanceScore() - amt));
            return "Penalized " + sid + " by " + amt + ". New score: " + s.getPerformanceScore();
        }
    }

    public List<String> processAllEvaluations() {
        List<String> results = new ArrayList<>();
        while (!evaluationQueue.isEmpty()) results.add(processNextEvaluation());
        return results;
    }

    // ---------------- Getters for UI & persistence ----------------
    public List<Student> getAllStudents() { return students; }
    public List<Staff> getAllStaff() { return staff; }
    public List<Course> getAllCourses() { return courses; }
    public Queue<String> getEvaluationQueue() { return evaluationQueue; }
}
