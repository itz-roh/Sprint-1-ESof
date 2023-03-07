package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.StudentDashboard

import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.dto.CourseExecutionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class ToStringStudentStatsTest extends SpockTest {
    def teacher
    def teacherDashboard

    def  setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacher.addCourse(externalCourseExecution)
    }

    def "convert student stats to string"() {
        given: "a StudentStats"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)

        when:"StudentStats is converted to string"
        def string = studentStats.toString()

        then: "quiz stats are converted to string"
        string == "Student Stats{" +
                    "id=" + studentStats.getId() +
                    ", courseExecution=" + studentStats.getCourseExecution() +
                    ", Number of Students=" + studentStats.getNumStudents() +
                    ", Number of Students who Solved >= 75% Questions=" + studentStats.getNumMore75CorrectQuestions() +
                    ", Number of Students who Solved >= 3 Quizzes=" + studentStats.getNumAtLeast3Quizzes() +
                    '}';
        }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration{}
}