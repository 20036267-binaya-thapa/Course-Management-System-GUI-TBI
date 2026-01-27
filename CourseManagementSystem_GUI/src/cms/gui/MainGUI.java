package cms.gui;

import cms.CourseManager;
import cms.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    private final CourseManager manager = new CourseManager();

    public MainGUI() {
        // Load Assignment 3 data
        FileUtil.loadStudents("students.csv", manager);
        FileUtil.loadStaff("staff.csv", manager);
        FileUtil.loadCourses("courses.csv", manager);

        setTitle("Course Management System (CMS)");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainMenu(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /* ================= HEADER ================= */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 90));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Course Management System (CMS)");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel subtitle = new JLabel("GUI Mode – Assignment 4 (BINAYA THAPA)");
        subtitle.setForeground(Color.LIGHT_GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    /* ================= MAIN MENU ================= */
    private JPanel createMainMenu() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 247, 250));
    
        // Main menu card
        JPanel menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setPreferredSize(new Dimension(450, 260));
    
        // ===== Heading =====
        JLabel heading = new JLabel("Main Menu", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setBorder(new EmptyBorder(0, 0, 10, 0));
    
        // ===== Buttons Grid =====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
    
        JButton studentBtn = new JButton("Manage Students");
        studentBtn.addActionListener(e -> StudentPanel.showStudentMenu(manager));
    
        JButton staffBtn = new JButton("Manage Staff");
        staffBtn.addActionListener(e -> StaffPanel.showStaffMenu(manager));
    
        JButton courseBtn = new JButton("Courses & Enrollments");
        courseBtn.addActionListener(e -> CoursePanel.showCourseMenu(manager));
    
        JButton evalBtn = new JButton("Evaluations");
        evalBtn.addActionListener(e -> EvaluationPanel.showEvaluationMenu(manager));
    
        JButton saveExitBtn = new JButton("Save & Exit");
        saveExitBtn.addActionListener(e -> saveAndExit());
    
        // Empty filler to balance grid
        buttonPanel.add(studentBtn);
        buttonPanel.add(staffBtn);
        buttonPanel.add(courseBtn);
        buttonPanel.add(evalBtn);
        buttonPanel.add(saveExitBtn);
        buttonPanel.add(new JLabel("")); // spacer
    
        menuPanel.add(heading, BorderLayout.NORTH);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);
    
        centerWrapper.add(menuPanel);
        return centerWrapper;
    }
    

    private void saveAndExit() {
        FileUtil.saveStudents("students.csv", manager.getAllStudents());
        FileUtil.saveStaff("staff.csv", manager.getAllStaff());
        FileUtil.saveCourses("courses.csv", manager.getAllCourses());
    
        JOptionPane.showMessageDialog(
                this,
                "All data saved successfully.",
                "Exit",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }
    

    /* ================= FOOTER ================= */
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(230, 230, 230));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        footer.add(new JLabel("ICT711 Programming & Algorithms"), BorderLayout.WEST);
        footer.add(new JLabel("© KOI – Assignment 4"), BorderLayout.EAST);

        return footer;
    }

    /* ================= RUN DIRECTLY ================= */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
