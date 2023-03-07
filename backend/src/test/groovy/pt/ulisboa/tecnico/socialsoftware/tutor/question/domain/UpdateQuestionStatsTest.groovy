package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.domain.QuestionSubmission;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import spock.lang.Unroll

@DataJpaTest
class UpdateQuestionStatsTest extends SpockTest {
    def teacher
    def teacherDashboard
	def questionStats

    def setup() {
        createExternalCourseAndExecution()
		teacher = new Teacher(USER_1_NAME, false)
        teacher.addCourse(externalCourseExecution)
        userRepository.save(teacher)
		teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "update the total number of questions available"() {
		when: "a question stat is added to the course"
		questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStats.getTeacherDashboard().equals(teacherDashboard)
		questionStats.getCourseExecution().equals(externalCourseExecution)

        def student = new Student(USER_2_NAME, false)
        externalCourseExecution.addUser(student)

        def course = new Course()
        externalCourseExecution.setCourse(course)
        
        int added_questions = 3
        for (int i = 0; i < added_questions; i++) {
            def question = new Question()
            question.setStatus(Question.Status.AVAILABLE)
            externalCourseExecution.getCourse().addQuestion(question)
        }

        then: "verify the atributes"
        
        questionStats.getNumAvailable() == 0
        questionStats.getAnsweredQuestionUnique() == 0
        questionStats.getAverageQuestionsAnswered() == 0
        questionStats.update()
        questionStats.getNumAvailable() == added_questions
    }
    
    def "update the parameters with 2 students and 3 questions"() {
        when: "a question stat is added to the course"
		questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStats.getTeacherDashboard().equals(teacherDashboard)
		questionStats.getCourseExecution().equals(externalCourseExecution)

        def student1 = new Student(USER_1_NAME, false)
        externalCourseExecution.addUser(student1)
        def student2 = new Student(USER_2_NAME, false)
        externalCourseExecution.addUser(student2)

        def question1 = new Question()
        externalCourseExecution.getCourse().addQuestion(question1)
        question1.setStatus(Question.Status.AVAILABLE)
        def question2 = new Question()
        externalCourseExecution.getCourse().addQuestion(question2)
        question2.setStatus(Question.Status.AVAILABLE)
        def question3 = new Question()
        externalCourseExecution.getCourse().addQuestion(question3)
        question3.setStatus(Question.Status.AVAILABLE)

        def questionSubmission1 = new QuestionSubmission(externalCourseExecution, question1, student1)
        def questionSubmission2 = new QuestionSubmission(externalCourseExecution, question2, student1)
        def questionSubmission3 = new QuestionSubmission(externalCourseExecution, question2, student2)
        def questionSubmission4 = new QuestionSubmission(externalCourseExecution, question3, student2)

        then: "verify the atributes"
        questionStats.update()
        questionStats.getNumAvailable() == 3
        questionStats.getAnsweredQuestionUnique() == 3
        questionStats.getAverageQuestionsAnswered() == 2.0
    } 

    def "update the parameters with 5 students and 6 questions"() {
        when: "a question stat is added to the course"
		questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStats.getTeacherDashboard().equals(teacherDashboard)
		questionStats.getCourseExecution().equals(externalCourseExecution)

        def student1 = new Student(USER_1_NAME, false)
        externalCourseExecution.addUser(student1)
        def student2 = new Student(USER_2_NAME, false)
        externalCourseExecution.addUser(student2)
        def student3 = new Student("USER 3 NAME", false)
        externalCourseExecution.addUser(student3)
        def student4 = new Student("USER 4 NAME", false)
        externalCourseExecution.addUser(student4)
        def student5 = new Student("USER 5 NAME", false)
        externalCourseExecution.addUser(student5)

        def question1 = new Question()
        externalCourseExecution.getCourse().addQuestion(question1)
        question1.setStatus(Question.Status.AVAILABLE)
        def question2 = new Question()
        externalCourseExecution.getCourse().addQuestion(question2)
        question2.setStatus(Question.Status.AVAILABLE)
        def question3 = new Question()
        externalCourseExecution.getCourse().addQuestion(question3)
        question3.setStatus(Question.Status.AVAILABLE)
        def question4 = new Question()
        externalCourseExecution.getCourse().addQuestion(question4)
        question4.setStatus(Question.Status.AVAILABLE)
        def question5 = new Question()
        externalCourseExecution.getCourse().addQuestion(question5)
        def question6 = new Question()
        externalCourseExecution.getCourse().addQuestion(question6)
        question6.setStatus(Question.Status.AVAILABLE)

        def questionSubmission1 = new QuestionSubmission(externalCourseExecution, question1, student1)
        def questionSubmission2 = new QuestionSubmission(externalCourseExecution, question2, student1)
        def questionSubmission3 = new QuestionSubmission(externalCourseExecution, question3, student1)
        def questionSubmission4 = new QuestionSubmission(externalCourseExecution, question3, student1)

        def questionSubmission5 = new QuestionSubmission(externalCourseExecution, question4, student2)

        def questionSubmission6 = new QuestionSubmission(externalCourseExecution, question1, student3)
        def questionSubmission7 = new QuestionSubmission(externalCourseExecution, question4, student3)

        def questionSubmission8 = new QuestionSubmission(externalCourseExecution, question1, student4)
        def questionSubmission9 = new QuestionSubmission(externalCourseExecution, question2, student4)
        def questionSubmission10 = new QuestionSubmission(externalCourseExecution, question3, student4)
        def questionSubmission11 = new QuestionSubmission(externalCourseExecution, question4, student4)
        def questionSubmission13 = new QuestionSubmission(externalCourseExecution, question6, student4)

        def questionSubmission14 = new QuestionSubmission(externalCourseExecution, question2, student5)
        def questionSubmission15 = new QuestionSubmission(externalCourseExecution, question4, student5)
        def questionSubmission16 = new QuestionSubmission(externalCourseExecution, question6, student5)

        then: "verify the atributes"
        questionStats.update()
        questionStats.getNumAvailable() == 5
        questionStats.getAnsweredQuestionUnique() == 5
        questionStats.getAverageQuestionsAnswered() == 2.8f
    }

    def "update atributes, answering quizzes from different courses"() {
        given: "two new different courses and two students"

        questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStats.getTeacherDashboard().equals(teacherDashboard)
		questionStats.getCourseExecution().equals(externalCourseExecution)


        def student1 = new Student(USER_1_NAME, false)
        externalCourseExecution.addUser(student1)
        def question1 = new Question()
        externalCourseExecution.getCourse().addQuestion(question1)
        question1.setStatus(Question.Status.AVAILABLE)
        
        def questionSubmission1 = new QuestionSubmission(externalCourseExecution, question1, student1)

        createExternalCourseAndExecution()
        externalCourseExecution.addUser(student1)
        
        def student2 = new Student(USER_2_NAME, false)
        externalCourseExecution.addUser(student2)
        def question2 = new Question()
        externalCourseExecution.getCourse().addQuestion(question2)
        question2.setStatus(Question.Status.AVAILABLE)
        def questionSubmission2 = new QuestionSubmission(externalCourseExecution, question2, student1)
        def questionSubmission3 = new QuestionSubmission(externalCourseExecution, question2, student2)


        when: "one of the students answers to a question is from another course"
        questionStats.update()

        then:
        questionStats.getNumAvailable() == 1
        questionStats.getAnsweredQuestionUnique() == 1
        questionStats.getAverageQuestionsAnswered() == 1.0f
    }

	@TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}