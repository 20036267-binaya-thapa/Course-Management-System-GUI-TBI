package cms;

import java.io.*;
import java.util.*;

/**
 * Responsible for reading/writing CSV files.
 *
 * students.csv -> only students
 * staff.csv    -> only staff
 * courses.csv  -> courses (unchanged)
 *
 * Student CSV format: id,name,age,major,score
 * Staff  CSV format: id,name,age,department
 *
 * This class handles missing files gracefully and prints friendly messages.
 */
public class FileUtil {

    // -------------------- LOAD STUDENTS --------------------
    public static void loadStudents(String filename, CourseManager manager) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("Note: " + filename + " not found. Starting with empty students list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // expected: id,name,age,major,score
                String[] p = line.split(",", -1);
                if (p.length < 5) {
                    System.out.println("Skipping malformed student line: " + line);
                    continue;
                }
                String id = p[0].trim();
                String name = p[1].trim();
                int age = safeInt(p[2]);
                String major = p[3].trim();
                double score = safeDouble(p[4]);

                Student s = new Student(id, name, age, major, score);
                manager.addStudent(s);
                count++;
            }
            System.out.println("Loaded " + count + " students from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- SAVE STUDENTS --------------------
    public static void saveStudents(String filename, List<Student> students) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Student s : students) {
                // id,name,age,major,score
                pw.println(s.getId() + "," + sanitize(s.getName()) + "," + s.getAge() + "," +
                           sanitize(s.getMajor()) + "," + s.getPerformanceScore());
            }
            System.out.println("Saved " + students.size() + " students to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving students to " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- LOAD STAFF --------------------
    public static void loadStaff(String filename, CourseManager manager) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("Note: " + filename + " not found. Starting with empty staff list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // expected: id,name,age,department
                String[] p = line.split(",", -1);
                if (p.length < 4) {
                    System.out.println("Skipping malformed staff line: " + line);
                    continue;
                }
                String id = p[0].trim();
                String name = p[1].trim();
                int age = safeInt(p[2]);
                String dept = p[3].trim();

                Staff st = new Staff(id, name, age, dept);
                manager.addStaff(st);
                count++;
            }
            System.out.println("Loaded " + count + " staff from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- SAVE STAFF --------------------
    public static void saveStaff(String filename, List<Staff> staff) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Staff st : staff) {
                // id,name,age,department
                pw.println(st.getId() + "," + sanitize(st.getName()) + "," + st.getAge() + "," + sanitize(st.getDepartment()));
            }
            System.out.println("Saved " + staff.size() + " staff to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving staff to " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- LOAD COURSES (unchanged) --------------------
    public static void loadCourses(String filename, CourseManager manager) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("Note: " + filename + " not found. Starting with empty courses list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 4) {
                    System.out.println("Skipping malformed course line: " + line);
                    continue;
                }
                String id = p[0].trim();
                String name = p[1].trim();
                int cap = safeInt(p[2]);
                String instr = p[3].trim();
                Course c = new Course(id, name, cap, instr);
                if (p.length >= 5 && !p[4].trim().isEmpty()) {
                    String[] sids = p[4].split(";");
                    for (String sid : sids) if (!sid.isBlank()) c.getEnrolledStudentIds().add(sid.trim());
                }
                manager.addCourse(c);
                count++;
            }
            System.out.println("Loaded " + count + " courses from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- SAVE COURSES (unchanged) --------------------
    public static void saveCourses(String filename, List<Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Course c : courses) pw.println(c.toCSV());
            System.out.println("Saved " + courses.size() + " courses to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving courses to " + filename + ": " + e.getMessage());
        }
    }

    // -------------------- Helpers --------------------
    private static int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static double safeDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }

    private static String sanitize(String s) {
        if (s == null) return "";
        return s.replace(",", ""); // simple sanitization for our CSV
    }
}
