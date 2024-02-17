import java.io.File

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
 }

class Info {
    private val filePath = "/home/seungbin/IdeaProjects/study/codes/score.txt"
    private val head = "H|학번|이름|국어|수학|영어|사회|과학"
    private val file = File(filePath)

    enum class DataType(val dataType: String) {
        Data("D"),
        Head("H"),
        TAIL("T")
    }
    
    
    fun typingInfo(): StudentInfo {
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
        
        return StudentInfo(stuNum,stuName,krScore,mathScore,engScore,scScore,soScore)
    }

    fun read(): List<StudentInfo> {
        val file = File(filePath)
        val listScore = mutableListOf<StudentInfo>()
        file.readLines().filter { it[0] == 'D' }.forEach { listScore.add(toScore(it)) }
        return listScore.toList()
    }


    fun readWIthNumber() :List<StudentInfo>{
        println("학번을 입력하세요`")
        val number = readlnOrNull()?.toInt()
        val listByNum = mutableListOf<StudentInfo>()
        val file = File(filePath)
        file.readLines().filter { it[1] == number?.toInt()?.toChar() }.forEach { listByNum.add(toScore(it)) }

        return listByNum.toList()
    }
    fun readByName() :List<StudentInfo>{
        println("이름을 입력하세요`")
        val name = readlnOrNull().toString()
        val listByName = mutableListOf<StudentInfo>()
        val file = File(filePath)
        file.readLines().filter { it[2].toString() == name }.forEach { listByName.add(toScore(it)) }
        return listByName.toList()
    }

    fun write(studentInfoList: List<StudentInfo>) {
        clear()
        file.appendText(head + "\n")
        studentInfoList.forEach {
            val line = "${DataType.Data.dataType}|${it.studentNum}|${it.studentName}|${it.koreanGrade}|${it.mathGrade}|${it.englishGrade}|${it.socialGrade}|${it.scienceGrade}\n"
            file.appendText(line)
        }
    }

    private fun clear() {
        file.writeText(head)
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

        fun breakpoint() {
            return
        }
    }
}

class Menu {
    private val infoInstance = Info()

    fun firstMenu(): Any {
        println("1.성적입력 2.성적조회 3.통계보기 4.저장하기 0.종료")
        val selected = kotlin.runCatching {
            readln().toInt()
        }.getOrDefault(1)

        when (selected) {
            1 -> {
                val stuInformation = infoInstance.typingInfo()
                return stuInformation

            }
            2 ->{
                println("조회 방법을 선택해주세요")
                println("1.이름 2.학번 3.전체")
                val secondSelection = kotlin.runCatching { readln().toInt() }.getOrDefault(3)
                when(secondSelection){
                    1->{
                        infoInstance.readByName()
                    }
                    2->{
                        infoInstance.readWIthNumber()
                    }
                    3 ->{
                        infoInstance.read()
                    }
                }
            }
            3->{}

            4->{}

            0->{
                println("종료하시겠습니까?")
                println("예/아니요")
                val userDecision = kotlin.runCatching { readlnOrNull().toString() }.getOrDefault("예")
                if(userDecision == "예"){
                    println("종료하겠습니다")
                }else{
                    return firstMenu()
                }
            }
        }
    }
}


fun main() {
    val information = Menu()
    information.firstMenu()
}
