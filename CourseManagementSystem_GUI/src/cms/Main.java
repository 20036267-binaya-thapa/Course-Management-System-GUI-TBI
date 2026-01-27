package cms;

import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;


/**
 * Main CLI. Friendly menus, validation, try/catch for safe user experience.
 */
public class Main {
    private static final String STUDENTS_FILE = "students.csv";
    private static final String STAFF_FILE = "staff.csv";
    private static final String COURSES_FILE = "courses.csv";

    public static void main(String[] args) {
        CourseManager manager = new CourseManager();
        Scanner sc = new Scanner(System.in);

        FileUtil.loadStudents(STUDENTS_FILE, manager);
        FileUtil.loadStaff(STAFF_FILE, manager);
        FileUtil.loadCourses(COURSES_FILE, manager);

        boolean running = true;
        showWelcome();

        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    handleAddStudent(manager, sc);
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "2":
                    handleAddStaff(manager, sc);
                    FileUtil.saveStaff(STAFF_FILE, manager.getAllStaff());
                    break;

                case "3":
                    viewAllStudents(manager, sc);
                    break;

                case "4":
                    viewAllStaff(manager, sc);
                    break;

                case "5": // ✅ SEARCH BY ID OR NAME (ONLY CHANGE)
                    System.out.println("--- Search Student ---");
                    System.out.println("1. Search by ID");
                    System.out.println("2. Search by Name");
                    System.out.print("Enter option: ");
                    String opt = sc.nextLine().trim();

                    if (opt.equals("1")) {
                        System.out.print("Enter student ID: ");
                        String id = sc.nextLine().trim();
                        Student s = manager.findStudentById(id);
                        if (s == null)
                            System.out.println("Student not found.");
                        else
                            System.out.println(s);

                    } else if (opt.equals("2")) {
                        System.out.print("Enter student name (exact match): ");
                        String nm = sc.nextLine().trim();
                        List<Student> found = manager.findStudentsByName(nm);
                        if (found.isEmpty())
                            System.out.println("No students found with that name.");
                        else
                            found.forEach(System.out::println);

                    } else {
                        System.out.println("Invalid search option.");
                    }
                    break;

                case "6":
                    handleUpdateStudent(manager, sc);
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "7":
                    handleDeleteStudent(manager, sc);
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "8":
                    handleAddCourse(manager, sc);
                    FileUtil.saveCourses(COURSES_FILE, manager.getAllCourses());
                    break;

                case "9":
                    viewCourses(manager, sc);
                    break;

                case "10":
                    handleEnroll(manager, sc);
                    FileUtil.saveCourses(COURSES_FILE, manager.getAllCourses());
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "11":
                    handleRemoveEnrollment(manager, sc);
                    FileUtil.saveCourses(COURSES_FILE, manager.getAllCourses());
                    break;

                case "12":
                    handleImmediateEvaluation(manager, sc);
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "13":
                    System.out.print("Enter schedule command (R,studentId,amount) or (P,studentId,amount): ");
                    String cmd = sc.nextLine().trim();
                    manager.scheduleEvaluation(cmd);
                    System.out.println("Scheduled: " + cmd);
                    break;

                case "14":
                    System.out.println(manager.processNextEvaluation());
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "15":
                    List<String> results = manager.processAllEvaluations();
                    if (results.isEmpty()) System.out.println("No scheduled evaluations.");
                    else results.forEach(System.out::println);
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    break;

                case "16":
                    if (manager.getEvaluationQueue().isEmpty())
                        System.out.println("Queue is empty.");
                    else
                        manager.getEvaluationQueue().forEach(System.out::println);
                    break;

                case "17":
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    FileUtil.saveStaff(STAFF_FILE, manager.getAllStaff());
                    FileUtil.saveCourses(COURSES_FILE, manager.getAllCourses());
                    System.out.println("Data saved.");
                    break;

                case "0":
                    FileUtil.saveStudents(STUDENTS_FILE, manager.getAllStudents());
                    FileUtil.saveStaff(STAFF_FILE, manager.getAllStaff());
                    FileUtil.saveCourses(COURSES_FILE, manager.getAllCourses());
                    System.out.println("Saved. Exiting. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number from the menu.");
            }
            System.out.println();
        }
        sc.close();
    }

    private static void showWelcome() {
        System.out.println("======================================");
        System.out.println("Student & Course Management System (CSV mode)");
        System.out.println("======================================\n");
    }

    private static void printMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1. Add student");
        System.out.println("2. Add staff");
        System.out.println("3. View all students");
        System.out.println("4. View all staff");
        System.out.println("5. Search student (by ID or Name)");
        System.out.println("6. Update student");
        System.out.println("7. Delete student");
        System.out.println("8. Add course");
        System.out.println("9. View courses (and enrolled students)");
        System.out.println("10. Enroll student into course");
        System.out.println("11. Remove student from course");
        System.out.println("12. Immediate evaluation (Reward/Penalty)");
        System.out.println("13. Schedule evaluation (add to queue)");
        System.out.println("14. Process next scheduled evaluation");
        System.out.println("15. Process all scheduled evaluations");
        System.out.println("16. View scheduled evaluations queue");
        System.out.println("17. Save now");
        System.out.println("0. Save & Exit");
        System.out.print("Enter choice: ");
    }


    // ---------------- Handler methods with validation ----------------

    private static void handleAddStudent(CourseManager manager, Scanner sc) {
        System.out.println("--- Add Student ---");
        System.out.print("Enter student ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (manager.findStudentById(id) != null) { System.out.println("Student ID already exists."); return; }

        System.out.print("Enter full name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

        int age = readInt(sc, "Enter age (integer): ", 0, 200);

        System.out.print("Enter major (optional): ");
        String major = sc.nextLine().trim();

        double score = readDouble(sc, "Enter initial performance score (or blank = 0): ", 0, Double.MAX_VALUE);

        Student s = new Student(id, name, age, major, score);
        manager.addStudent(s);
        System.out.println("Student added: " + s);
    }

    private static void handleAddStaff(CourseManager manager, Scanner sc) {
        System.out.println("--- Add Staff ---");
        System.out.print("Enter staff ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (manager.findStaffById(id) != null) { System.out.println("Staff ID already exists."); return; }

        System.out.print("Enter full name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

        int age = readInt(sc, "Enter age (integer): ", 18, 120);

        System.out.print("Enter department/role: ");
        String dept = sc.nextLine().trim();

        Staff st = new Staff(id, name, age, dept);
        manager.addStaff(st);
        System.out.println("Staff added: " + st);
    }

    private static void viewAllStudents(CourseManager manager, Scanner sc) {
        List<Student> students = manager.getAllStudents();
    
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
    
        System.out.println("--- VIEW STUDENTS ---");
        System.out.println("1. Sort by ID");
        System.out.println("2. Sort by Name");
        System.out.println("3. Sort by Performance Score");
        System.out.println("4. No sorting");
        System.out.print("Choose option: ");
    
        String choice = sc.nextLine().trim();
    
        switch (choice) {
            case "1":
                Collections.sort(students,
                        Comparator.comparing(Student::getId, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by ID.");
                break;
    
            case "2":
                Collections.sort(students,
                        Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Name.");
                break;
    
            case "3":
                Collections.sort(students,
                        Comparator.comparingDouble(Student::getPerformanceScore).reversed());
                System.out.println("Sorted by Performance Score (High → Low).");
                break;
    
            case "4":
                System.out.println("No sorting applied.");
                break;
    
            default:
                System.out.println("Invalid choice. Showing unsorted list.");
        }
    
        System.out.println("\n--- STUDENT LIST ---");
        students.forEach(System.out::println);
    }    

    private static void viewAllStaff(CourseManager manager, Scanner sc) {
        List<Staff> staffList = manager.getAllStaff();
    
        if (staffList.isEmpty()) {
            System.out.println("No staff found.");
            return;
        }
    
        System.out.println("--- VIEW STAFF ---");
        System.out.println("1. Sort by Staff ID");
        System.out.println("2. Sort by Name");
        System.out.println("3. Sort by Department");
        System.out.println("4. No sorting");
        System.out.print("Choose option: ");
    
        String choice = sc.nextLine().trim();
    
        switch (choice) {
            case "1":
                Collections.sort(staffList,
                        Comparator.comparing(Staff::getId, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Staff ID.");
                break;
    
            case "2":
                Collections.sort(staffList,
                        Comparator.comparing(Staff::getName, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Name.");
                break;
    
            case "3":
                Collections.sort(staffList,
                        Comparator.comparing(
                                s -> s.getDepartment() == null ? "" : s.getDepartment(),
                                String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Department.");
                break;
    
            case "4":
                System.out.println("No sorting applied.");
                break;
    
            default:
                System.out.println("Invalid choice. Showing unsorted list.");
        }
    
        System.out.println("\n--- STAFF LIST ---");
        staffList.forEach(System.out::println);
    }
    

    private static void handleUpdateStudent(CourseManager manager, Scanner sc) {
        System.out.println("--- Update Student ---");
        System.out.print("Enter student ID to update: ");
        String id = sc.nextLine().trim();
        Student s = manager.findStudentById(id);
        if (s == null) { System.out.println("Student not found."); return; }
        System.out.println("Current: " + s);
        System.out.print("Enter new name (leave blank to keep): ");
        String newName = sc.nextLine().trim();
        Integer newAge = null;
        System.out.print("Enter new age (leave blank to keep): ");
        String ageStr = sc.nextLine().trim();
        if (!ageStr.isEmpty()) {
            try { newAge = Integer.parseInt(ageStr); } catch (NumberFormatException e) { System.out.println("Invalid age. Update aborted."); return; }
        }
        System.out.print("Enter new major (leave blank to keep): ");
        String newMajor = sc.nextLine().trim();
        boolean ok = manager.updateStudent(id, newName.isEmpty() ? null : newName, newAge, newMajor.isEmpty() ? null : newMajor);
        System.out.println(ok ? "Student updated." : "Update failed.");
    }

    private static void handleDeleteStudent(CourseManager manager, Scanner sc) {
        System.out.println("--- Delete Student ---");
        System.out.print("Enter student ID to delete: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        boolean ok = manager.deleteStudent(id);
        System.out.println(ok ? "Student deleted." : "Student not found or deletion failed.");
    }

    private static void handleAddCourse(CourseManager manager, Scanner sc) {
        System.out.println("--- Add Course ---");
        System.out.print("Enter course ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("Course ID cannot be empty."); return; }
        if (manager.findCourseById(id) != null) { System.out.println("Course ID already exists."); return; }

        System.out.print("Enter course name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Course name cannot be empty."); return; }

        int cap = readInt(sc, "Enter capacity (integer): ", 1, 1000);

        System.out.print("Enter instructor (staff) ID (leave blank if none): ");
        String instr = sc.nextLine().trim();
        if (!instr.isEmpty() && manager.findStaffById(instr) == null) {
            System.out.println("Warning: instructor ID not found. You can set it later.");
        }

        Course c = new Course(id, name, cap, instr);
        manager.addCourse(c);
        System.out.println("Course added: " + c);
    }

    private static void viewCourses(CourseManager manager, Scanner sc) {
        List<Course> courseList = manager.getAllCourses();
    
        if (courseList.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
    
        System.out.println("--- VIEW COURSES ---");
        System.out.println("1. Sort by Course ID");
        System.out.println("2. Sort by Course Name");
        System.out.println("3. Sort by Enrolled Student Count");
        System.out.println("4. No sorting");
        System.out.print("Choose option: ");
    
        String choice = sc.nextLine().trim();
    
        switch (choice) {
            case "1":
                Collections.sort(courseList,
                        Comparator.comparing(Course::getCourseId, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Course ID.");
                break;
    
            case "2":
                Collections.sort(courseList,
                        Comparator.comparing(Course::getCourseName, String.CASE_INSENSITIVE_ORDER));
                System.out.println("Sorted by Course Name.");
                break;
    
            case "3":
                Collections.sort(courseList,
                        Comparator.comparingInt(
                                (Course c) -> c.getEnrolledStudentIds().size()
                        ).reversed());
                System.out.println("Sorted by Enrolled Student Count (High to Low).");
                break;
    
            case "4":
                System.out.println("No sorting applied.");
                break;
    
            default:
                System.out.println("Invalid choice. Showing unsorted list.");
        }
    
        System.out.println("\n--- COURSE LIST ---");
        courseList.forEach(System.out::println);
    }
    
    

    private static void handleEnroll(CourseManager manager, Scanner sc) {
        System.out.println("--- Enroll Student ---");
        System.out.print("Enter student ID: ");
        String sid = sc.nextLine().trim();
        System.out.print("Enter course ID: ");
        String cid = sc.nextLine().trim();
        if (manager.enrollStudentInCourse(sid, cid)) System.out.println("Enrolled successfully.");
        else System.out.println("Enrollment failed. Check student ID, course ID, or capacity/already enrolled.");
    }

    private static void handleRemoveEnrollment(CourseManager manager, Scanner sc) {
        System.out.println("--- Remove Enrollment ---");
        System.out.print("Enter student ID: ");
        String sid = sc.nextLine().trim();
        System.out.print("Enter course ID: ");
        String cid = sc.nextLine().trim();
        if (manager.removeStudentFromCourse(sid, cid)) System.out.println("Removed from course.");
        else System.out.println("Remove failed. Check IDs.");
    }

    private static void handleImmediateEvaluation(CourseManager manager, Scanner sc) {
        System.out.println("--- Immediate Evaluation (Reward/Penalty) ---");
        System.out.print("Enter student ID: ");
        String sid = sc.nextLine().trim();
        Student s = manager.findStudentById(sid);
        if (s == null) { System.out.println("Student not found."); return; }
        System.out.print("Reward (R) or Penalty (P)? ");
        String rp = sc.nextLine().trim();
        System.out.print("Enter amount: ");
        String amtStr = sc.nextLine().trim();
        double amt;
        try { amt = Double.parseDouble(amtStr); } catch (NumberFormatException e) { System.out.println("Invalid amount."); return; }
        if (rp.equalsIgnoreCase("R")) {
            s.setPerformanceScore(s.getPerformanceScore() + amt);
            System.out.println("Reward applied. New score: " + s.getPerformanceScore());
        } else {
            s.setPerformanceScore(Math.max(0, s.getPerformanceScore() - amt));
            System.out.println("Penalty applied. New score: " + s.getPerformanceScore());
        }
    }

    // helpers for safe input
    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String in = sc.nextLine().trim();
            try {
                if (in.isEmpty()) return min;
                int v = Integer.parseInt(in);
                if (v < min || v > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String in = sc.nextLine().trim();
            if (in.isEmpty()) return 0.0;
            try {
                double v = Double.parseDouble(in);
                if (v < min || v > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
