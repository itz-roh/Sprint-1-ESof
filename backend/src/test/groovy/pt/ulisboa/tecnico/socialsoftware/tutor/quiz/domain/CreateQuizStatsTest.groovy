package pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;


@DataJpaTest
class CreateQuizStatsTest extends SpockTest {
    def teacher
    def teacherDB
    def qStats 

    def setup() {
        createExternalCourseAndExecution()
        teacher = new Teacher(USER_1_NAME, false)
        teacher.addCourse(externalCourseExecution)
        userRepository.save(teacher)
        teacherDB = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "create an empty quiz stats"() {
        when:"Creating a quizStats"
        qStats = new QuizStats(externalCourseExecution, teacherDB)

        then: "a empty quiz stats is created"
        qStats.getNumQuizzes() == 0
        qStats.getUniqueQuizzesSolved() == 0
        qStats.getAverageQuizzesSolved() == 0
        qStats.getTeacherDashboard().equals(teacherDB)
        qStats.getCourseExecution().equals(externalCourseExecution)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}