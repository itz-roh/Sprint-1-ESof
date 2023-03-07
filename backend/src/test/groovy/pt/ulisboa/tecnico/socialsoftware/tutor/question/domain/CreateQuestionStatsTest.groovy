package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import spock.lang.Unroll

@DataJpaTest
class CreateQuestionStatsTest extends SpockTest {
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

    def "create an empty questionStats"() {
		when: "an empty questionStats is created"
		questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)

		then: "verify the atributes"
		questionStats.getTeacherDashboard().equals(teacherDashboard)
		questionStats.getCourseExecution().equals(externalCourseExecution)
		questionStats.getNumAvailable() == 0
		questionStats.getAnsweredQuestionUnique() == 0
		questionStats.getAnsweredQuestionUnique() == 0
    }

	def "test toString"() {
        given:"a questionStats"
        def qStats = new QuestionStats(externalCourseExecution, teacherDashboard)

        when:"a quetionStat is converted to string"
        def string = qStats.toString()

        then: "question stats are converted to string"
        string == "Questions Stats{" +
                "id=" + qStats.getId() +
                ", courseExecution=" + qStats.getCourseExecution() +
                ", Number of Available Questions=" + qStats.getNumAvailable() +
                ", Number of Unique Questions Answered per Student=" + qStats.getAnsweredQuestionUnique() +
                ", Average number of Unique Quetions Answered per Student=" + qStats.getAverageQuestionsAnswered() +
                '}';
	}

	@TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}