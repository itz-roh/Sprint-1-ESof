package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class CreateStudentStatsTest extends SpockTest {
    def teacher
    def teacherDashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
    }

    def "create an empty student stats"() {
        when: "a student stats is created"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)

        then: "an empty student stats is created"
        studentStats.getNumStudents() == 0
        studentStats.getNumMore75CorrectQuestions() == 0
        studentStats.getNumAtLeast3Quizzes() == 0
        studentStats.getTeacherDashboard().equals(teacherDashboard)
        studentStats.getCourseExecution().equals(externalCourseExecution)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration{}
}