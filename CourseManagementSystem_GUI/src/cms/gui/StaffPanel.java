package cms.gui;

import cms.CourseManager;
import cms.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
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

    // ================= ADD STAFF =================
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

        String department = JOptionPane.showInputDialog("Enter Department / Role:");
        if (department == null || department.isBlank()) return;

        Staff staff = new Staff(id, name, age, department);
        manager.addStaff(staff);

        JOptionPane.showMessageDialog(null, "Staff added successfully:\n" + staff);
    }

    // ================= VIEW STAFF (TABLE) =================
    private static void viewStaff(CourseManager manager) {

        List<Staff> staffList = manager.getAllStaff();
        if (staffList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No staff found.");
            return;
        }

        // ---- Table ----
        String[] columns = {"ID", "Name", "Age", "Department"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        loadStaffIntoTable(model, staffList);

        // ---- Buttons ----
        JButton btnSortId = new JButton("Sort by ID");
        JButton btnSortName = new JButton("Sort by Name");
        JButton btnSortDept = new JButton("Sort by Department");
        JButton btnClose = new JButton("Close");

        btnSortId.addActionListener(e -> {
            staffList.sort(Comparator.comparing(Staff::getId));
            loadStaffIntoTable(model, staffList);
        });

        btnSortName.addActionListener(e -> {
            staffList.sort(Comparator.comparing(Staff::getName, String.CASE_INSENSITIVE_ORDER));
            loadStaffIntoTable(model, staffList);
        });

        btnSortDept.addActionListener(e -> {
            staffList.sort(Comparator.comparing(Staff::getDepartment, String.CASE_INSENSITIVE_ORDER));
            loadStaffIntoTable(model, staffList);
        });

        btnClose.addActionListener(e -> SwingUtilities.getWindowAncestor(btnClose).dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSortId);
        buttonPanel.add(btnSortName);
        buttonPanel.add(btnSortDept);
        buttonPanel.add(btnClose);

        // ---- Dialog ----
        JDialog dialog = new JDialog();
        dialog.setTitle("Staff List");
        dialog.setSize(650, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    // ================= HELPER =================
    private static void loadStaffIntoTable(DefaultTableModel model, List<Staff> staffList) {
        model.setRowCount(0);
        for (Staff st : staffList) {
            model.addRow(new Object[]{
                    st.getId(),
                    st.getName(),
                    st.getAge(),
                    st.getDepartment()
            });
        }
    }
}