package cms.gui;

import cms.CourseManager;
import cms.Student;

import javax.swing.*;
import java.util.List;
import java.util.Queue;

public class EvaluationPanel {

    public static void showEvaluationMenu(CourseManager manager) {

        String menuText =
                "Evaluation Menu\n\n" +
                "• Immediate Reward/Penalty: Update a student's score instantly\n" +
                "• Schedule Evaluation: Queue a reward or penalty to process later\n" +
                "• Process Next Evaluation: Apply the next queued evaluation (FIFO)\n" +
                "• Process All Evaluations: Apply all queued evaluations\n" +
                "• View Evaluation Queue: View pending evaluations\n";

        String[] options = {
                "Immediate Reward / Penalty",
                "Schedule Evaluation",
                "Process Next",
                "Process All",
                "View Queue",
                "Back"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    menuText,
                    "Student Evaluation System",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

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

    /* ================= IMMEDIATE EVALUATION ================= */
    private static void immediateEvaluation(CourseManager manager) {

        String sid = JOptionPane.showInputDialog(
                "Immediate Evaluation\n\nEnter Student ID:"
        );
        if (sid == null || sid.isBlank()) return;

        Student s = manager.findStudentById(sid);
        if (s == null) {
            JOptionPane.showMessageDialog(null, "Student not found.");
            return;
        }

        String info =
                "Student Found:\n" +
                "Name: " + s.getName() + "\n" +
                "Current Score: " + s.getPerformanceScore();

        JOptionPane.showMessageDialog(null, info);

        String[] actions = {"Reward (Increase Score)", "Penalty (Decrease Score)"};
        int action = JOptionPane.showOptionDialog(
                null,
                "Select evaluation type:",
                "Immediate Evaluation",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                actions,
                actions[0]
        );

        if (action == -1) return;

        String amtStr = JOptionPane.showInputDialog(
                "Enter score amount (positive number):"
        );
        if (amtStr == null || amtStr.isBlank()) return;

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
            return;
        }

        if (action == 0) {
            s.setPerformanceScore(s.getPerformanceScore() + amount);
        } else {
            s.setPerformanceScore(Math.max(0, s.getPerformanceScore() - amount));
        }

        JOptionPane.showMessageDialog(
                null,
                "Evaluation applied successfully.\n\nUpdated Score: " + s.getPerformanceScore()
        );
    }

    /* ================= SCHEDULE EVALUATION ================= */
    private static void scheduleEvaluation(CourseManager manager) {

        String message =
                "Schedule Evaluation\n\n" +
                "This will add an evaluation to the queue.\n\n" +
                "Format:\n" +
                "R,studentId,amount   → Reward\n" +
                "P,studentId,amount   → Penalty\n\n" +
                "Example:\n" +
                "R,S101,5";

        String cmd = JOptionPane.showInputDialog(message);
        if (cmd == null || cmd.isBlank()) return;

        manager.scheduleEvaluation(cmd);

        JOptionPane.showMessageDialog(
                null,
                "Evaluation successfully scheduled:\n" + cmd
        );
    }

    /* ================= PROCESS NEXT ================= */
    private static void processNext(CourseManager manager) {

        String result = manager.processNextEvaluation();

        JOptionPane.showMessageDialog(
                null,
                result == null || result.isBlank()
                        ? "No evaluations in the queue."
                        : result
        );
    }

    /* ================= PROCESS ALL ================= */
    private static void processAll(CourseManager manager) {

        List<String> results = manager.processAllEvaluations();

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No scheduled evaluations.");
            return;
        }

        StringBuilder sb = new StringBuilder("Evaluation Results:\n\n");
        for (String r : results) {
            sb.append("• ").append(r).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    /* ================= VIEW QUEUE ================= */
    private static void viewQueue(CourseManager manager) {

        Queue<String> queue = manager.getEvaluationQueue();

        if (queue.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Evaluation queue is empty.");
            return;
        }

        StringBuilder sb = new StringBuilder("Pending Evaluations (FIFO Order):\n\n");
        int count = 1;
        for (String cmd : queue) {
            sb.append(count++).append(". ").append(cmd).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }
}