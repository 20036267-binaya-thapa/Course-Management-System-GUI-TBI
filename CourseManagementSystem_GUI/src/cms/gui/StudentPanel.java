package cms.gui;

import cms.CourseManager;
import cms.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class StudentPanel {

    public static void showStudentMenu(CourseManager manager) {

        String[] options = {
                "Add Student",
                "View Students",
                "Update Student",
                "Delete Student",
                "Search Student",
                "Back"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select an option",
                    "Student Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == -1 || choice == 5) break;

            switch (choice) {
                case 0 -> addStudent(manager);
                case 1 -> viewStudents(manager);
                case 2 -> updateStudent(manager);
                case 3 -> deleteStudent(manager);
                case 4 -> searchStudent(manager);
            }
        }
    }

    // ================= ADD STUDENT =================
    private static void addStudent(CourseManager manager) {

        String id = JOptionPane.showInputDialog("Enter Student ID:");
        if (id == null || id.isBlank()) return;

        if (manager.findStudentById(id) != null) {
            JOptionPane.showMessageDialog(null, "Student ID already exists.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter Student Name:");
        if (name == null || name.isBlank()) return;

        String ageStr = JOptionPane.showInputDialog("Enter Age:");
        int age = (ageStr != null && !ageStr.isBlank()) ? Integer.parseInt(ageStr) : 0;

        String major = JOptionPane.showInputDialog("Enter Major (optional):");

        String scoreStr = JOptionPane.showInputDialog("Enter Initial Performance Score:");
        double score = (scoreStr != null && !scoreStr.isBlank()) ? Double.parseDouble(scoreStr) : 0.0;

        Student s = new Student(id, name, age, major, score);
        manager.addStudent(s);

        JOptionPane.showMessageDialog(null, "Student added:\n" + s);
    }

    // ================= VIEW STUDENTS (TABLE) =================
    private static void viewStudents(CourseManager manager) {

        List<Student> students = manager.getAllStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No students found.");
            return;
        }

        // ---- Table Model ----
        String[] columns = {"ID", "Name", "Age", "Major", "Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);

        // ---- Load Data ----
        loadStudentsIntoTable(model, students);

        // ---- Buttons ----
        JButton btnSortId = new JButton("Sort by ID");
        JButton btnSortName = new JButton("Sort by Name");
        JButton btnSortScore = new JButton("Sort by Score");
        JButton btnClose = new JButton("Close");

        btnSortId.addActionListener(e -> {
            students.sort(Comparator.comparing(Student::getId));
            loadStudentsIntoTable(model, students);
        });

        btnSortName.addActionListener(e -> {
            students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
            loadStudentsIntoTable(model, students);
        });

        btnSortScore.addActionListener(e -> {
            students.sort(Comparator.comparingDouble(Student::getPerformanceScore).reversed());
            loadStudentsIntoTable(model, students);
        });

        btnClose.addActionListener(e -> SwingUtilities.getWindowAncestor(btnClose).dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSortId);
        buttonPanel.add(btnSortName);
        buttonPanel.add(btnSortScore);
        buttonPanel.add(btnClose);

        // ---- Dialog ----
        JDialog dialog = new JDialog();
        dialog.setTitle("Student List");
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    // ================= HELPER =================
    private static void loadStudentsIntoTable(DefaultTableModel model, List<Student> students) {
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getAge(),
                    s.getMajor(),
                    s.getPerformanceScore()
            });
        }
    }

    // ================= UPDATE STUDENT =================
    private static void updateStudent(CourseManager manager) {

        String id = JOptionPane.showInputDialog("Enter Student ID to update:");
        if (id == null || id.isBlank()) return;

        Student s = manager.findStudentById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(null, "Student not found.");
            return;
        }

        String newName = JOptionPane.showInputDialog("Enter new name (leave blank to keep):", s.getName());
        String ageStr = JOptionPane.showInputDialog("Enter new age (leave blank to keep):", s.getAge());
        Integer newAge = (ageStr != null && !ageStr.isBlank()) ? Integer.parseInt(ageStr) : null;
        String newMajor = JOptionPane.showInputDialog("Enter new major (leave blank to keep):", s.getMajor());

        manager.updateStudent(
                id,
                (newName == null || newName.isBlank()) ? null : newName,
                newAge,
                (newMajor == null || newMajor.isBlank()) ? null : newMajor
        );

        JOptionPane.showMessageDialog(null, "Student updated:\n" + s);
    }

    // ================= DELETE STUDENT =================
    private static void deleteStudent(CourseManager manager) {

        String id = JOptionPane.showInputDialog("Enter Student ID to delete:");
        if (id == null || id.isBlank()) return;

        boolean ok = manager.deleteStudent(id);
        JOptionPane.showMessageDialog(null, ok ? "Student deleted." : "Student not found.");
    }

    // ================= SEARCH STUDENT =================
    private static void searchStudent(CourseManager manager) {

        String[] options = {"Search by ID", "Search by Name"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose search method",
                "Search Student",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) return;

        if (choice == 0) {
            String id = JOptionPane.showInputDialog("Enter Student ID:");
            if (id == null || id.isBlank()) return;

            Student s = manager.findStudentById(id);
            JOptionPane.showMessageDialog(null,
                    s == null ? "No student found." : "Student Found:\n" + s);
        } else {
            String name = JOptionPane.showInputDialog("Enter Student Name:");
            if (name == null || name.isBlank()) return;

            List<Student> found = manager.findStudentsByName(name);
            if (found.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No students found.");
            } else {
                StringBuilder sb = new StringBuilder("Found Students:\n\n");
                for (Student s : found) sb.append(s).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        }
    }
}