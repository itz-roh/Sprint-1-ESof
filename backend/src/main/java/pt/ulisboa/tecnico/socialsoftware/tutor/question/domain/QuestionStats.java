package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.Assessment.Status;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class QuestionStats implements DomainEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	private Integer numAvailable = 0;
	private Integer answeredQuestionUnique = 0;
	private float averageQuestionsAnswered = 0;
	
	@ManyToOne
    private TeacherDashboard teacherDashboard;

	@OneToOne
    private CourseExecution courseExecution;
	
	
	public QuestionStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
		setCourseExecution(courseExecution);
		setTeacherDashboard(teacherDashboard);
		teacherDashboard.addQuestionStats(this);
	}
	
	public Integer getId() {
		return id;
	}
	
	public TeacherDashboard getTeacherDashboard() {
		return teacherDashboard;
	}
	
	public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
		this.teacherDashboard = teacherDashboard;
	}
	
	public void setCourseExecution(CourseExecution courseExecution) {
		this.courseExecution = courseExecution;
	}
	
	public CourseExecution getCourseExecution() {
		return courseExecution;
	}

	public Integer getNumAvailable() {
		return numAvailable;
	}

	public void setNumAvailable(Integer numAvailable) {
		this.numAvailable = numAvailable;
	}

	public Integer getAnsweredQuestionUnique() {
		return answeredQuestionUnique;
	}

	public void setAnsweredQuestionUnique(Integer answeredQuestionUnique) {
		this.answeredQuestionUnique = answeredQuestionUnique;
	}

	public float getAverageQuestionsAnswered() {
		return averageQuestionsAnswered;
	}

	public void setAverageQuestionsAnswered(float averageQuestionsAnswered) {
		this.averageQuestionsAnswered = averageQuestionsAnswered;
	}

	public void update() {
		this.numAvailable = totalQuestionsAvailable();
		this.answeredQuestionUnique = totalUniqueQuestionsAnswered();
		this.averageQuestionsAnswered = averageUniqueQuestionsAnswered();
	}

	public void accept(Visitor visitor){}

	private Integer totalQuestionsAvailable() {
		return (int) this.getCourseExecution().getCourse().getQuestions().stream()
		            .filter(q -> q.getStatus().equals(Question.Status.AVAILABLE)).count();
	}

	private Integer totalUniqueQuestionsAnswered() {
		return (int) this.getCourseExecution().getQuestionSubmissions().stream()
		           .map(q -> q.getQuestion())   
				   .distinct()  
				   .count();
	}

	private float averageUniqueQuestionsAnswered() {
		Set<Student> students = this.getCourseExecution().getStudents();
		float totalQuestionCount = 0;
		for (Student student : students) {
			totalQuestionCount += (int) student.getQuestionSubmissions().stream()
				.filter(q -> q.getCourseExecution().equals(getCourseExecution()))
				.map(qs -> qs.getQuestion())
				.distinct()
				.count();
		}
		
		if (students.size() == 0)
			return 0; 
		return totalQuestionCount / students.size();
	}

	@Override
    public String toString() {
        return "Questions Stats{" +
                "id=" + this.getId() +
                ", courseExecution=" + this.getCourseExecution() +
                ", Number of Available Questions=" + this.getNumAvailable() +
                ", Number of Unique Questions Answered per Student=" + this.getAnsweredQuestionUnique() +
                ", Average number of Unique Quetions Answered per Student=" + this.getAverageQuestionsAnswered() +
                '}';
    }

}