package cms.gui;

import cms.CourseManager;
import cms.Staff;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StaffPanel {

    public static void showStaffMenu(CourseManager manager) {
        String[] options = {"Add Staff", "View Staff", "Back"};

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select an option",
                    "Staff Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == -1 || choice == 2) break;

            switch (choice) {
                case 0 -> addStaff(manager);
                case 1 -> viewStaff(manager);
            }
        }
    }

    private static void addStaff(CourseManager manager) {
        String id = JOptionPane.showInputDialog("Enter Staff ID:");
        if (id == null || id.isBlank()) return;

        if (manager.findStaffById(id) != null) {
            JOptionPane.showMessageDialog(null, "Staff ID already exists.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter Staff Name:");
        if (name == null || name.isBlank()) return;

        String ageStr = JOptionPane.showInputDialog("Enter Age:");
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid age.");
            return;
        }

        String department = JOptionPane.showInputDialog("Enter Department/Role:");
        if (department == null || department.isBlank()) return;

        Staff staff = new Staff(id, name, age, department);
        manager.addStaff(staff);

        JOptionPane.showMessageDialog(null, "Staff added successfully:\n" + staff);
    }

    private static void viewStaff(CourseManager manager) {
        List<Staff> staffList = manager.getAllStaff();

        if (staffList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No staff found.");
            return;
        }

        // Main dialog frame
        JDialog dialog = new JDialog((Frame) null, "Staff List", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        // Text area
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        refreshStaffList(textArea, staffList);

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel (BOTTOM)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton sortNameBtn = new JButton("Sort by Name");
        JButton sortDeptBtn = new JButton("Sort by Department");
        JButton closeBtn = new JButton("Close");

        sortNameBtn.addActionListener(e -> {
            staffList.sort((s1, s2) ->
                    s1.getName().compareToIgnoreCase(s2.getName()));
            refreshStaffList(textArea, staffList);
        });

        sortDeptBtn.addActionListener(e -> {
            staffList.sort((s1, s2) ->
                    s1.getDepartment().compareToIgnoreCase(s2.getDepartment()));
            refreshStaffList(textArea, staffList);
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(sortNameBtn);
        buttonPanel.add(sortDeptBtn);
        buttonPanel.add(closeBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void refreshStaffList(JTextArea textArea, List<Staff> staffList) {
        StringBuilder sb = new StringBuilder("Staff List:\n\n");
        for (Staff st : staffList) {
            sb.append(st).append("\n");
        }
        textArea.setText(sb.toString());
    }
}
