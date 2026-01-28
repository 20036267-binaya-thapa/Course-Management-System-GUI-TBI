package cms;

/**
 * Small helper for immediate reward/penalty application (used by menu).
 */
public class Evaluation {

    public static String applyImmediateReward(CourseManager manager, String studentId, double amount) {
        Student s = manager.findStudentById(studentId);
        if (s == null) return "Student not found: " + studentId;
        s.setPerformanceScore(s.getPerformanceScore() + amount);
        return "Reward applied. New score: " + s.getPerformanceScore();
    }

    public static String applyImmediatePenalty(CourseManager manager, String studentId, double amount) {
        Student s = manager.findStudentById(studentId);
        if (s == null) return "Student not found: " + studentId;
        s.setPerformanceScore(Math.max(0, s.getPerformanceScore() - amount));
        return "Penalty applied. New score: " + s.getPerformanceScore();
    }
}
