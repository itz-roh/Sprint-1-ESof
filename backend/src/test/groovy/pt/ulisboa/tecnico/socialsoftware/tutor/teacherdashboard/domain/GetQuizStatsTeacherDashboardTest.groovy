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
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;


@DataJpaTest
class GetQuizStatsTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDB

    def setup() {
        createExternalCourseAndExecution()
        teacher = new Teacher(USER_1_NAME, false)
        teacher.addCourse(externalCourseExecution)
        userRepository.save(teacher)
        teacherDB = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "Get quiz stats when quiz stats doesn't exist"() {
        when: "getting quiz stats"
        def qStats = teacherDB.getQuizStats(externalCourseExecution)

        then: "a empty set is recieved"
        qStats == null
    }

    def "Add a quiz stat to a teacher dashboard"(){
        when: "A new quiz stats is created and assigned to a teacher dashboard"
        def qStats = new QuizStats(externalCourseExecution, teacherDB)

        then: "The set has that quiz stats"
        teacherDB.getQuizStats(externalCourseExecution).equals(qStats)
    }

    def "Add two quizzes to a teacher dashboard"(){
        when: "Two new quizz stats are added to the same teacher's dashboard"
        def qStats1 = new QuizStats(externalCourseExecution, teacherDB)
        def course1 = createExternalCourseAndExecution()
        def qStats2 = new QuizStats(course1, teacherDB)

        then: "The set has two quiz Stats"
        teacherDB.getQuizStats(course1).equals(qStats2)
    }

    def "get quiz stats and it exists"() {
        given: "a quiz stats from a course execution"
        def quizStats = new QuizStats(externalCourseExecution, teacherDB)
        
        when: "quiz stats is retrieved"
        def getQuizStats = teacherDB.getQuizStats(externalCourseExecution)

        then: "it is the same quiz stats"
        quizStats.equals(getQuizStats)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}