package cms.gui;

import cms.CourseManager;
import cms.Student;

import javax.swing.*;
import java.util.List;
import java.util.Queue;

public class EvaluationPanel {

    public static void showEvaluationMenu(CourseManager manager) {
        String[] options = {"Immediate Reward/Penalty", "Schedule Evaluation", "Process Next Evaluation",
                            "Process All Evaluations", "View Evaluation Queue", "Back"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Select an option", "Evaluation Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == -1 || choice == 5) break;

            switch (choice) {
                case 0 -> immediateEvaluation(manager);
                case 1 -> scheduleEvaluation(manager);
                case 2 -> processNext(manager);
                case 3 -> processAll(manager);
                case 4 -> viewQueue(manager);
            }
        }
    }

    private static void immediateEvaluation(CourseManager manager) {
        String sid = JOptionPane.showInputDialog("Enter Student ID:");
        if (sid == null || sid.isBlank()) return;
        Student s = manager.findStudentById(sid);
        if (s == null) { JOptionPane.showMessageDialog(null, "Student not found."); return; }

        String[] choices = {"Reward", "Penalty"};
        int action = JOptionPane.showOptionDialog(null, "Select action", "Immediate Evaluation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
        if (action == -1) return;

        String amtStr = JOptionPane.showInputDialog("Enter amount:");
        if (amtStr == null || amtStr.isBlank()) return;
        double amt = Double.parseDouble(amtStr);

        if (action == 0) s.setPerformanceScore(s.getPerformanceScore() + amt);
        else s.setPerformanceScore(Math.max(0, s.getPerformanceScore() - amt));

        JOptionPane.showMessageDialog(null, "Updated score: " + s.getPerformanceScore());
    }

    private static void scheduleEvaluation(CourseManager manager) {
        String cmd = JOptionPane.showInputDialog("Enter command: (R,studentId,amount) or (P,studentId,amount)");
        if (cmd == null || cmd.isBlank()) return;
        manager.scheduleEvaluation(cmd);
        JOptionPane.showMessageDialog(null, "Scheduled: " + cmd);
    }

    private static void processNext(CourseManager manager) {
        String result = manager.processNextEvaluation();
        JOptionPane.showMessageDialog(null, result);
    }

    private static void processAll(CourseManager manager) {
        List<String> results = manager.processAllEvaluations();
        if (results.isEmpty()) JOptionPane.showMessageDialog(null, "No scheduled evaluations.");
        else {
            StringBuilder sb = new StringBuilder();
            for (String r : results) sb.append(r).append("\n");
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    private static void viewQueue(CourseManager manager) {
        Queue<String> queue = manager.getEvaluationQueue();
        if (queue.isEmpty()) JOptionPane.showMessageDialog(null, "Evaluation queue is empty.");
        else {
            StringBuilder sb = new StringBuilder("Scheduled Evaluations:\n");
            for (String cmd : queue) sb.append(cmd).append("\n");
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }
}
