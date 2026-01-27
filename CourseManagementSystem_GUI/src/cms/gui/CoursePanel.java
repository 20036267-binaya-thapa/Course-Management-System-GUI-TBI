package cms.gui;

import cms.Course;
import cms.CourseManager;
import cms.Student;

import javax.swing.*;
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

    // ---------------- ADD COURSE ----------------
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
        int cap = capStr != null && !capStr.isBlank() ? Integer.parseInt(capStr) : 1;

        String instr = JOptionPane.showInputDialog("Enter Instructor (Staff) ID (optional):");

        Course c = new Course(id, name, cap, instr == null ? "" : instr);
        manager.addCourse(c);

        JOptionPane.showMessageDialog(null, "Course added:\n" + c);
    }

    // ---------------- VIEW + SORT COURSES ----------------
    private static void viewCourses(CourseManager manager) {
        List<Course> courses = manager.getAllCourses();

        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No courses found.");
            return;
        }

        // Dialog
        JDialog dialog = new JDialog((Frame) null, "Courses", true);
        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(10, 10));

        // Text Area
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();

        JButton sortNameBtn = new JButton("Sort by Course Name");
        JButton sortEnrollBtn = new JButton("Sort by Enrolled Count");
        JButton closeBtn = new JButton("Close");

        buttonPanel.add(sortNameBtn);
        buttonPanel.add(sortEnrollBtn);
        buttonPanel.add(closeBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Helper to refresh text
        Runnable refreshText = () -> {
            StringBuilder sb = new StringBuilder("Courses:\n\n");
            for (Course c : courses) {
                sb.append(c).append("\n");

                if (!c.getEnrolledStudentIds().isEmpty()) {
                    sb.append("  Enrolled Students: ");
                    for (String sid : c.getEnrolledStudentIds()) {
                        Student s = manager.findStudentById(sid);
                        sb.append(s != null ? s.getName() : sid).append("; ");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }
            textArea.setText(sb.toString());
            textArea.setCaretPosition(0);
        };

        // Initial display
        refreshText.run();

        // Sort by name
        sortNameBtn.addActionListener(e -> {
            courses.sort(Comparator.comparing(Course::getCourseName, String.CASE_INSENSITIVE_ORDER));
            refreshText.run();
        });

        // Sort by enrolled count
        sortEnrollBtn.addActionListener(e -> {
            courses.sort((a, b) ->
                    Integer.compare(b.getEnrolledStudentIds().size(), a.getEnrolledStudentIds().size()));
            refreshText.run();
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // ---------------- ENROLL STUDENT ----------------
    private static void enrollStudent(CourseManager manager) {
        String sid = JOptionPane.showInputDialog("Enter Student ID to enroll:");
        if (sid == null || sid.isBlank()) return;

        String cid = JOptionPane.showInputDialog("Enter Course ID:");
        if (cid == null || cid.isBlank()) return;

        boolean ok = manager.enrollStudentInCourse(sid, cid);
        JOptionPane.showMessageDialog(
                null,
                ok ? "Enrollment successful." : "Enrollment failed. Check IDs or capacity."
        );
    }

    // ---------------- REMOVE STUDENT ----------------
    private static void removeStudent(CourseManager manager) {
        String sid = JOptionPane.showInputDialog("Enter Student ID to remove:");
        if (sid == null || sid.isBlank()) return;

        String cid = JOptionPane.showInputDialog("Enter Course ID:");
        if (cid == null || cid.isBlank()) return;

        boolean ok = manager.removeStudentFromCourse(sid, cid);
        JOptionPane.showMessageDialog(
                null,
                ok ? "Removed from course." : "Removal failed. Check IDs."
        );
    }
}
