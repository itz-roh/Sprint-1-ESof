package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class GetStudentStatsTest extends SpockTest {
    def teacher
    def teacherDashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "get student stats and it exists"() {
        given: "a student stats from a course execution"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        
        when: "student stats is retrieved"
        def getStudentStats = teacherDashboard.getStudentStats(externalCourseExecution)

        then: "it is the same student stats"
        studentStats.equals(getStudentStats)
    }

    def "get student stats and it doesn't exist"() {
        when: "getting a student stats"
        teacherDashboard.getStudentStats()
        
        then: "a new student stats is retrieved"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}