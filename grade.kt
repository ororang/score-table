import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt


data class StudentInfo(
    val studentNum: Int,
    val studentName: String,
    val koreanGrade: Double,
    val mathGrade: Double,
    val englishGrade: Double,
    val socialGrade: Double,
    val scienceGrade: Double
){
    private val totalScore = koreanGrade + mathGrade + englishGrade + socialGrade + scienceGrade
    val average = totalScore/5


    fun calculateStandardDeviation(): Double {
        val squaredDifferences = listOf(
            (koreanGrade - average).pow(2),
            (mathGrade - average).pow(2),
            (englishGrade - average).pow(2),
            (socialGrade - average).pow(2),
            (scienceGrade - average).pow(2)
        )

        val meanOfSquaredDifferences = squaredDifferences.average()

        return sqrt(meanOfSquaredDifferences)
    }
 }

class Info {
    private val filePath = "/home/seungbin/IdeaProjects/study/codes/score.txt"
    private val head = "H|학번|이름|국어|수학|영어|사회|과학"
    private val file = File(filePath)
    val listScore = loadScores()

    enum class DataType(val dataType: String) {
        Data("D"),
        Head("H"),
        TAIL("T")
    }
    
    
    fun addScore(){
        println("학번을 입력하세요")
        val stuNum = readln().toInt()

        println("이름을 입력하세요")
        val stuName = readlnOrNull().toString()
        
        println("국어성적을 입력하세요")
        val krScore = readln().toDouble()

        println("수학성적을 입력하세요")
        val mathScore = readln().toDouble()
        
        println("영어성적을 입력하세요")
        val engScore = readln().toDouble()

        println("과학성적을 입력하세요")
        val scScore = readln().toDouble()

        println("사회성적을 입력하세요")
        val soScore = readln().toDouble()
        
        listScore.add(StudentInfo(stuNum,stuName,krScore,mathScore,engScore,scScore,soScore))
    }

    fun loadScores(): MutableList<StudentInfo> {
        val file = File(filePath)
        var listScore = mutableListOf<StudentInfo>()
        file.readLines().filter { it[0] == 'D' }.forEach { listScore.add(toScore(it)) }
        println(listScore)
        return listScore
    }


    fun loadByNumber(): MutableList<StudentInfo> {
        println("학번을 입력하세요")
        val stuNum = readln().toInt()
        val file = File(filePath)
        var listScore = mutableListOf<StudentInfo>()
        file.readLines()
            .filter { it.startsWith(DataType.Data.dataType) }
            .map { toScore(it) }
            .filter { it.studentNum == stuNum }
            .forEach { listScore.add(it) }
        println(listScore)
        return listScore


    }
    fun loadByName() :List<StudentInfo>{
        println("이름을 입력하세요`")
        val name = readln()
        val file = File(filePath)
        var listScore = mutableListOf<StudentInfo>()
        file.readLines()
            .filter { it.startsWith(DataType.Data.dataType) }
            .map { toScore(it) }
            .filter { it.studentName == name }
            .forEach { listScore.add(it) }
        println(listScore)
        return listScore

    }

    fun writeScore() {
        writeHead()
        file.appendText(head + "\n")
        listScore.forEach {
            val line = "${DataType.Data.dataType}|${it.studentNum}|${it.studentName}|${it.koreanGrade}|${it.mathGrade}|${it.englishGrade}|${it.socialGrade}|${it.scienceGrade}\n"
            file.appendText(line)
        }
    }

    private fun writeHead() {
        file.writeText(head)
    }

    fun calculateOverallSubjectAverages(): Map<String, Double> {
        val subjectAverages = mutableMapOf<String, Double>()

        // 과목별 성적을 더한 합계 초기화
        val subjectTotals = mutableMapOf(
            "korean" to 0.0,
            "math" to 0.0,
            "english" to 0.0,
            "social" to 0.0,
            "science" to 0.0
        )

        for (studentInfo in listScore) {
            subjectTotals["korean"] = subjectTotals["korean"]!! + studentInfo.koreanGrade
            subjectTotals["math"] = subjectTotals["math"]!! + studentInfo.mathGrade
            subjectTotals["english"] = subjectTotals["english"]!! + studentInfo.englishGrade
            subjectTotals["social"] = subjectTotals["social"]!! + studentInfo.socialGrade
            subjectTotals["science"] = subjectTotals["science"]!! + studentInfo.scienceGrade
        }

        // 과목별 평균 계산
        for ((subject, total) in subjectTotals) {
            subjectAverages[subject] = total / listScore.size
        }

        return subjectAverages
    }

    companion object {
        fun toScore(fileFormat: String): StudentInfo {
            val scoreAry = fileFormat.split("|")
            return StudentInfo(
                scoreAry[1].toInt(),
                scoreAry[2],
                scoreAry[3].toDouble(),
                scoreAry[4].toDouble(),
                scoreAry[5].toDouble(),
                scoreAry[6].toDouble(),
                scoreAry[7].toDouble()
            )
        }
    }
}

class Menu {
    private val scoreTable = Info()

    fun firstMenu() {
        println("1.성적입력 2.성적조회 3.통계보기 4.저장하기 0.종료")
        val selected = kotlin.runCatching {
            readln().toInt()
        }.getOrDefault(1)

        when (selected) {
            1 -> {
                scoreTable.addScore()
            }
            2 -> {
                println("조회 방법을 선택해주세요")
                println("1.이름 2.학번 3.전체")
                val secondSelection = kotlin.runCatching { readln().toInt() }.getOrDefault(3)
                when (secondSelection) {
                    1 -> {
                        scoreTable.loadByName()
                    }
                    2 -> {
                        scoreTable.loadByNumber()
                    }
                    3 -> {
                        scoreTable.loadScores()
                    }
                }
            }
            3 -> {
                thirdMenu()
            }
            4 -> {
                scoreTable.writeScore()
            }
            0 -> {
                println("종료하시겠습니까?")
                println("예/아니요")
                val userDecision =
                    kotlin.runCatching { readlnOrNull().toString() }.getOrDefault("예")
                if (userDecision == "예") {
                    println("종료하겠습니다")
                    return
                }
            }
            else->{
                println("숫자를 제대로 입력해주십시오")
            }
        }
        firstMenu()
    }

    private fun thirdMenu() {
        println("1.평균 2.표준편차 3.모두보기 4.과목별평균")
        val thirdSelection = kotlin.runCatching { readln().toInt() }.getOrDefault(3)

        when (thirdSelection) {
            1 -> {
                val studentInfoList = scoreTable.loadScores()
                val totalAverage = studentInfoList.map { it.average }.average()
                println("전체 학생의 평균: $totalAverage")
            }
            2 -> {
                val studentInfoList = scoreTable.loadScores()
                val totalStandardDeviation = studentInfoList.map { it.calculateStandardDeviation() }.average()
                println("전체 학생의 평균 표준편차: $totalStandardDeviation")
            }
            3-> {
                val studentInfoList = scoreTable.loadScores()
                val totalStatistics = studentInfoList.map { it.average }.average()
                val totalStatistics2 = studentInfoList.map { it.calculateStandardDeviation() }.average()
                println("전체 통계| 평균:$totalStatistics 표준편차:$totalStatistics2")
            }
            4 -> {
                val overallSubjectAverages = scoreTable.calculateOverallSubjectAverages()
                println("전체 학생들의 과목별 평균:")
                for ((subject, average) in overallSubjectAverages) {
                    println("$subject: $average")
                }
            }
            else -> {
                println("잘못된 선택입니다.")
            }
        }
        firstMenu()
    }
}

fun main() {
    val information = Menu()
    information.firstMenu()
}

