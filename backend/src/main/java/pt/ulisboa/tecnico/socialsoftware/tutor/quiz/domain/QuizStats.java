package pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.StudentDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;


@Entity
public class QuizStats implements DomainEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numQuizzes;
    private int uniqueQuizzesSolved;
    private float averageQuizzesSolved;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public QuizStats(CourseExecution course, TeacherDashboard teacherDB) {
        numQuizzes = 0;
        uniqueQuizzesSolved=0;
        averageQuizzesSolved=0;
        setCourseExecution(course);
        setTeacherDashboard(teacherDB);
        teacherDB.addQuizStats(this);
    }

    public Integer getId(){
        return id;
    }

    public  TeacherDashboard getTeacherDashboard(){
        return teacherDashboard;
    }

    public  CourseExecution getCourseExecution(){
        return courseExecution;
    }

    public int getNumQuizzes() {
        return numQuizzes;
    }

    public int getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }
    public void setCourseExecution(CourseExecution course) {
        this.courseExecution = course;
    } 

    public void setTeacherDashboard(TeacherDashboard teacherDB) {
        this.teacherDashboard = teacherDB;
    } 


    public void setNumQuizzes(int num) {
        this.numQuizzes = num;
    }

    public void setUniqueQuizzesSolved(int num) {
        this.uniqueQuizzesSolved = num;
    }

    public void setAverageQuizzesSolved(float num) {
        this.averageQuizzesSolved = num;
    }

    private void updateQuizNum(){
        this.numQuizzes = courseExecution.getQuizzes().size();
    }

    private void updateUniqueQuizNum(){
        Set<Quiz> quizzes = courseExecution.getQuizzes();
        this.numQuizzes = quizzes.size();
        for(Quiz q: quizzes) {
            if (!q.getQuizAnswers().isEmpty())
                this.uniqueQuizzesSolved += 1;
        }
    }

    private void updateAverageUniqueQuiz(){
        Set<Student> students = courseExecution.getStudents();
        Set<Quiz> quizzes = new HashSet<>();
        Set<QuizAnswer> answers;
        float total = 0;
        for(Student s: students) {  
            answers = s.getQuizAnswers();
            quizzes.clear();
            for(QuizAnswer answer: answers){ 
                //Verify if quiz is from this.courseExecution
                if(answer.getQuiz().getCourseExecution().equals(courseExecution))
                    //Add quiz to set
                    quizzes.add(answer.getQuiz());  
            }
            //add number of quizzes answered by the student s
            total += quizzes.size();
        }
        if(students.size() > 0)
            this.averageQuizzesSolved = total/students.size();
        else
            this.averageQuizzesSolved = 0;
    }

    public void update(){
        this.updateQuizNum();
        this.updateUniqueQuizNum();
        this.updateAverageUniqueQuiz();
    }

    public void accept(Visitor visitor){

    }

    @Override
    public String toString() {
        return "Quiz Stats{" +
                "id = " + this.getId() +
                ", courseExecution = " + this.getCourseExecution() +
                ", Number of Quizzes = " + this.getNumQuizzes() +
                ", Number of unique quizzes solved = " + this.getUniqueQuizzesSolved() +
                ", Average of unique quizzes solved by student = " + this.getAverageQuizzesSolved() +
                '}';
    }

}
