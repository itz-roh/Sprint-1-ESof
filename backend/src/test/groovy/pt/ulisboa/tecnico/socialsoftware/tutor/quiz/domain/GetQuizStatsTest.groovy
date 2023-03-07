package pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution

@DataJpaTest
class GetQuizStatsTest extends SpockTest {
    def teacher
    def teacherDB
    def quiz1
    def quiz2
    def quiz3
    def quiz4
    def quizStats
    def student1
    def student2

    def setup() {
        createExternalCourseAndExecution()
        teacher = new Teacher(USER_1_NAME, false)
        teacher.addCourse(externalCourseExecution)
        userRepository.save(teacher)
        teacherDB = new TeacherDashboard(externalCourseExecution, teacher)

        quiz1 = new Quiz()
        quiz1.setCourseExecution(externalCourseExecution)
        externalCourseExecution.addQuiz(quiz1)

        quiz2 = new Quiz()
        quiz2.setCourseExecution(externalCourseExecution)
        externalCourseExecution.addQuiz(quiz2)

        quiz3 = new Quiz()
        quiz3.setCourseExecution(externalCourseExecution)
        externalCourseExecution.addQuiz(quiz3)

        quiz4 = new Quiz()
        quiz4.setCourseExecution(externalCourseExecution)
        externalCourseExecution.addQuiz(quiz4)

        quizStats = new QuizStats(externalCourseExecution, teacherDB)

        student1 = new Student(USER_2_NAME, false)
        student2 = new Student(USER_3_NAME, false)
        userRepository.save(student1)
        userRepository.save(student2)
        externalCourseExecution.addUser(student1)
        externalCourseExecution.addUser(student2)
    }

    def "create 4 available quizzes"(){
        when: "many quizzes are available"
        quizStats.update()

        then: "the number of quizzes is 4"
        quizStats.getNumQuizzes() == 4
    }


    def "test the total number of quizzes available"() {
        given: "a new quiz on a course"
        externalCourseExecution.addQuiz(new Quiz())

        when: "updating the number of quizzes"
        quizStats.update()

        then: "the number of quizzes is 1"
        quizStats.getNumQuizzes() == 5
    }

    def "test the total number of unique quizzes solved by students"() {
        given: "two student with some quiz answers"
        def quizAnswer1 = new QuizAnswer(student1,quiz1)
        def quizAnswer2 = new QuizAnswer(student1,quiz2)
        def quizAnswer3 = new QuizAnswer(student2,quiz1)
       

        when: "updating the number of unique quizzes resolved"
        quizStats.update()

        then: "the number of unique quizzes resolved is 2"
        quizStats.getNumQuizzes() == 4
        quizStats.getUniqueQuizzesSolved() == 2
    }

    def "test the average number of unique quizzes solved by each student"() {
        given: "two student with some quiz answers"
        def quizAnswer1 = new QuizAnswer(student1,quiz1)
        def quizAnswer2 = new QuizAnswer(student1,quiz2)
        def quizAnswer3 = new QuizAnswer(student2,quiz1)

        when: "updating the average of unique quizzes resolved by student"
        quizStats.update()

        then: "the average of unique quizzes resolved by student is 1.5"
        quizStats.getAverageQuizzesSolved() == 1.5
    }

    def "test QuizStats update when a QuizStats is created"() {
        given: "a new QuizStats"
        def course = createExternalCourseAndExecution()
        def qStats = new QuizStats(course, teacherDB)

        when: "updating the new QUizStats"
        qStats.update()

        then:
        qStats.getNumQuizzes() == 0
        qStats.getUniqueQuizzesSolved() == 0
        qStats.getAverageQuizzesSolved() == 0
    }

    def "test average unique solved quizzes by a student, including quizzes in different courses"() {
        given: "two new different courses and two students enrolled in at least one of the new courses and some new quizzes"
        def course1 = createExternalCourseAndExecution()
        def quiz = new Quiz()
        quiz.setCourseExecution(course1)
        course1.addQuiz(quiz)
        course1.addUser(student2)
        def quizAnswer1 = new QuizAnswer(student1,quiz1)
        def quizAnswer2 = new QuizAnswer(student2,quiz1)
        def quizAnswer3 = new QuizAnswer(student2,quiz)

        when: "one of the students answers to a quiz from another course"
        quizStats.update()

        then: 
        quizStats.getAverageQuizzesSolved() == 1
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}