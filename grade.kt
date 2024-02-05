import java.io.File

fun main() {
    val real = studentType()
    val f_write= writeFile()
    val studentList = mutableListOf(real)

    // You can add more students to the list as needed
    // val anotherStudent = studentType()
    // studentList.add(anotherStudent)
    f_write
    searchStudent(studentList)
}

fun studentType(): StudentInfo {
    println("학생의 이름을 입력해주세요")
    val studName = readLine().toString()

    println("학번을 입력해주세요")
    val studNum = readLine()!!.toInt()

    println("국어성적을 입력해주세요")
    val c_grade = readLine()!!.toDouble()

    println("수학성적을 입력해주세요")
    val m_grade = readLine()!!.toDouble()

    println("한국사성적을 입력해주세요")
    val ch_grade = readLine()!!.toDouble()

    println("사회성적을 입력해주세요")
    val ts_grade = readLine()!!.toDouble()

    println("과학성적을 입력해주세요")
    val sc_grade = readLine()!!.toDouble()

    return StudentInfo(studName, studNum, c_grade, m_grade, ch_grade, ts_grade, sc_grade)
}


fun writeFile(){
    val fos = File("")
    fos.printWriter().println(studentType())
}
fun searchStudent(studentList: List<StudentInfo>) {
    println("학번을 입력해주세요")
    val stdNumber = readLine()?.toInt()

    val foundStudent = studentList.find { it.schoolNumber == stdNumber }

    if (foundStudent != null) {
        println("학생 정보 찾음:")
        println(foundStudent)
    } else {
        println("해당 학번의 학생을 찾을 수 없습니다.")
    }
}

data class StudentInfo(
    val name: String,
    val schoolNumber: Int,
    var countryLang: Double,
    var mathematics: Double,
    var countryHistory: Double,
    var totalSocial: Double,
    var totalScience: Double
) {
    val totalScore = countryLang + mathematics + countryHistory + totalSocial + totalScience
    val average = totalScore / 5

    override fun toString(): String {
        return "StudentInfo(name='$name', schoolNumber=$schoolNumber, totalScore=$totalScore, average=$average)"
    }
}
