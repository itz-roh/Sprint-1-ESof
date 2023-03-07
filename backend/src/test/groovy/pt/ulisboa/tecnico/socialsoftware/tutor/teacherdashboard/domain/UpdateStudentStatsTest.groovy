package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.StudentDashboard

import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.dto.CourseExecutionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTestIT
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.FailedAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler


@DataJpaTest
class UpdateStudentStatsTest extends SpockTest {

    def teacher
    def teacherDashboard

    def  setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacher.addCourse(externalCourseExecution)

    }

    def "Update StudentStats without results"() {
        when: "updating a new studentStats"
        def stats = new StudentStats(externalCourseExecution, teacherDashboard)
        stats.update()

        then: "all equal zero"
        stats.getNumStudents() == 0
        stats.getNumMore75CorrectQuestions() == 0
        stats.getNumAtLeast3Quizzes() == 0
    }

    def "Update number of students in StudentStats"() {
        given: "2 students"
        def student1 = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        student1.addCourse(externalCourseExecution)
        userRepository.save(student1)
        def student2 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.TECNICO)
        student2.addCourse(externalCourseExecution)
        userRepository.save(student2)

        and: "each dashBoard"
        def dashboard1 = new StudentDashboard(externalCourseExecution, student1)
        studentDashboardRepository.save(dashboard1)
        def dashboard2 = new StudentDashboard(externalCourseExecution, student2)
        studentDashboardRepository.save(dashboard2)

        and: "its student stats"
        def stats = new StudentStats(externalCourseExecution, teacherDashboard)

        when: "updating number of students"
        stats.update()

        then: "Number of students is equal to 2"
        stats.getNumStudents() == 2
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration{}
}