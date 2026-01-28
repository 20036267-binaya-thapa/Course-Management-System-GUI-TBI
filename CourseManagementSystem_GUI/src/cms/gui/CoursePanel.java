package cms.gui;

import cms.Course;
import cms.CourseManager;
import cms.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class CoursePanel {

    public static void showCourseMenu(CourseManager manager) {

        String[] options = {
                "Add Course",
                "View Courses",
                "Enroll Student",
                "Remove Student from Course",
                "Back"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select an option",
                    "Course Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == -1 || choice == 4) break;

            switch (choice) {
                case 0 -> addCourse(manager);
                case 1 -> viewCourses(manager);
                case 2 -> enrollStudent(manager);
                case 3 -> removeStudent(manager);
            }
        }
    }

    // ================= ADD COURSE =================
    private static void addCourse(CourseManager manager) {

        String id = JOptionPane.showInputDialog("Enter Course ID:");
        if (id == null || id.isBlank()) return;

        if (manager.findCourseById(id) != null) {
            JOptionPane.showMessageDialog(null, "Course ID already exists.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter Course Name:");
        if (name == null || name.isBlank()) return;

        String capStr = JOptionPane.showInputDialog("Enter Course Capacity:");
        int capacity = (capStr != null && !capStr.isBlank())
                ? Integer.parseInt(capStr)
                : 1;

        String instructor = JOptionPane.showInputDialog("Enter Instructor (Staff ID - optional):");

        Course course = new Course(id, name, capacity, instructor == null ? "" : instructor);
        manager.addCourse(course);

        JOptionPane.showMessageDialog(null, "Course added:\n" + course);
    }

    // ================= VIEW COURSES (TABLE) =================
    private static void viewCourses(CourseManager manager) {

        List<Course> courses = manager.getAllCourses();
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No courses found.");
            return;
        }

        // ---- Table ----
        String[] columns = {
                "Course ID",
                "Course Name",
                "Capacity",
                "Enrolled Count",
                "Enrolled Students"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        loadCoursesIntoTable(model, courses, manager);

        // ---- Buttons ----
        JButton btnSortId = new JButton("Sort by ID");
        JButton btnSortName = new JButton("Sort by Course Name");
        JButton btnSortEnroll = new JButton("Sort by Enrolled Count");
        JButton btnClose = new JButton("Close");

        btnSortId.addActionListener(e -> {
            courses.sort(Comparator.comparing(Course::getCourseId));
            loadCoursesIntoTable(model, courses, manager);
        });

        btnSortName.addActionListener(e -> {
            courses.sort(Comparator.comparing(Course::getCourseName, String.CASE_INSENSITIVE_ORDER));
            loadCoursesIntoTable(model, courses, manager);
        });

        btnSortEnroll.addActionListener(e -> {
            courses.sort((a, b) ->
                    Integer.compare(
                            b.getEnrolledStudentIds().size(),
                            a.getEnrolledStudentIds().size()
                    ));
            loadCoursesIntoTable(model, courses, manager);
        });

        btnClose.addActionListener(e -> SwingUtilities.getWindowAncestor(btnClose).dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSortId);
        buttonPanel.add(btnSortName);
        buttonPanel.add(btnSortEnroll);
        buttonPanel.add(btnClose);

        // ---- Dialog ----
        JDialog dialog = new JDialog();
        dialog.setTitle("Course List");
        dialog.setSize(850, 450);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    // ================= ENROLL STUDENT =================
    private static void enrollStudent(CourseManager manager) {

        String studentId = JOptionPane.showInputDialog("Enter Student ID to enroll:");
        if (studentId == null || studentId.isBlank()) return;

        String courseId = JOptionPane.showInputDialog("Enter Course ID:");
        if (courseId == null || courseId.isBlank()) return;

        boolean ok = manager.enrollStudentInCourse(studentId, courseId);
        JOptionPane.showMessageDialog(
                null,
                ok ? "Enrollment successful." : "Enrollment failed. Check IDs or capacity."
        );
    }

    // ================= REMOVE STUDENT =================
    private static void removeStudent(CourseManager manager) {

        String studentId = JOptionPane.showInputDialog("Enter Student ID to remove:");
        if (studentId == null || studentId.isBlank()) return;

        String courseId = JOptionPane.showInputDialog("Enter Course ID:");
        if (courseId == null || courseId.isBlank()) return;

        boolean ok = manager.removeStudentFromCourse(studentId, courseId);
        JOptionPane.showMessageDialog(
                null,
                ok ? "Student removed from course." : "Removal failed. Check IDs."
        );
    }

    // ================= HELPER =================
    private static void loadCoursesIntoTable(
            DefaultTableModel model,
            List<Course> courses,
            CourseManager manager
    ) {
        model.setRowCount(0);

        for (Course c : courses) {

            StringBuilder enrolledNames = new StringBuilder();
            for (String sid : c.getEnrolledStudentIds()) {
                Student s = manager.findStudentById(sid);
                enrolledNames.append(
                        s != null ? s.getName() : sid
                ).append(", ");
            }

            if (!enrolledNames.isEmpty()) {
                enrolledNames.setLength(enrolledNames.length() - 2);
            }

            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCapacity(),
                    c.getEnrolledStudentIds().size(),
                    enrolledNames.toString()
            });
        }
    }
}