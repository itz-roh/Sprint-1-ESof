package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.StudentDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;

import java.util.Set;

import javax.persistence.*;

@Entity
public class StudentStats implements DomainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numStudents = 0;

    private int numMore75CorrectQuestions = 0;

    private int numAtLeast3Quizzes = 0;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    @OneToOne
    private CourseExecution courseExecution;

    public StudentStats() {
    }

    public StudentStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
        teacherDashboard.addStudentStats(this);
    }

    public Integer getId() {
        return id;
    }

    public TeacherDashboard getTeacherDashboard(){
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public int getNumMore75CorrectQuestions() {
        return numMore75CorrectQuestions;
    }

    public void setNumMore75CorrectQuestions(int numMore75CorrectQuestions) {
        this.numMore75CorrectQuestions = numMore75CorrectQuestions;
    }

    public int getNumAtLeast3Quizzes() {
        return numAtLeast3Quizzes;
    }

    public void setNumAtLeast3Quizzes(int numAtLeast3Quizzes) {
        this.numAtLeast3Quizzes = numAtLeast3Quizzes;
    }

    public void update() {
        int more75CorrectQuestions = 0;
        int atLeast3Quizzes = 0;
        Set<Student> students = courseExecution.getStudents();

        for(Student student: students){
            int correct = 0;
            int total = 0;
            int numQuizzes = 0;
            Set<StudentDashboard> dashboards = student.getDashboards();
            for(StudentDashboard dashboard: dashboards){
                numQuizzes += (dashboard.getNumberOfStudentQuizzes() + dashboard.getNumberOfInClassQuizzes());
                correct += dashboard.getNumberOfCorrectInClassAnswers() + dashboard.getNumberOfCorrectStudentAnswers();
                total += dashboard.getNumberOfInClassAnswers() + dashboard.getNumberOfStudentAnswers(); 
            }
            if(total!=0 && correct/total >= 0.75){
                more75CorrectQuestions += 1;
            }
            if(numQuizzes>=3){
                atLeast3Quizzes += 1;
            }
        }

        setNumStudents(courseExecution.getStudents().size());

        setNumMore75CorrectQuestions(more75CorrectQuestions);

        setNumAtLeast3Quizzes(atLeast3Quizzes);

    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "Student Stats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", Number of Students=" + numStudents +
                ", Number of Students who Solved >= 75% Questions=" + numMore75CorrectQuestions +
                ", Number of Students who Solved >= 3 Quizzes=" + numAtLeast3Quizzes +
                '}';
    }

}

