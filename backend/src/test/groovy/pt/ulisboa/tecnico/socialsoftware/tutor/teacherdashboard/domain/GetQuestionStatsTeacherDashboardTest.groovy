package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;


@DataJpaTest
class GetQuestionStatsTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDB

    def setup() {
        createExternalCourseAndExecution()
        teacher = new Teacher(USER_1_NAME, false)
        teacher.addCourse(externalCourseExecution)
        userRepository.save(teacher)
        teacherDB = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "Get question stats when question stats doesn't exist"() {
        when: "getting question stats"
        def qStats = teacherDB.getQuestionStats(externalCourseExecution)

        then: "a empty set is recieved"
        qStats == null
    }

    def "Add a question stat to a teacher dashboard"(){
        when: "A new question stats is created and assigned to a teacher dashboard"
        def qStats = new QuestionStats(externalCourseExecution, teacherDB)

        then: "The set has that question stats"
        teacherDB.getQuestionStats(externalCourseExecution).equals(qStats)
    }

    def "Add two quizzes to a teacher dashboard"(){
        when: "Two new quizz stats are added to the same teacher's dashboard"
        def qStats1 = new QuestionStats(externalCourseExecution, teacherDB)
        def course1 = createExternalCourseAndExecution()
        def qStats2 = new QuestionStats(course1, teacherDB)

        then: "The set has two question Stats"
        teacherDB.getQuestionStats(course1).equals(qStats2)
    }

    def "get question stats and it exists"() {
        given: "a question stats from a course execution"
        def quizStats = new QuestionStats(externalCourseExecution, teacherDB)
        
        when: "question stats is retrieved"
        def getQuestionStats = teacherDB.getQuestionStats(externalCourseExecution)

        then: "it is the same question stats"
        quizStats.equals(getQuestionStats)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}