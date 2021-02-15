package com.afrolone.iusapp.scrapeutils


import android.util.Log
import com.afrolone.iusapp.models.Course
import com.afrolone.iusapp.models.Grade
import com.afrolone.iusapp.models.Student
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object ScrapeUtils {

    // TODO Write comments

    private const val TAG = "SCRAPEUTILS"

    // Variables used
    val USER_AGENT = "\"Mozilla/5.0 (Windows NT\" +" +
            "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\""
    val URL = "http://sis.ius.edu.ba/login.aspx?lang=en-US"
    val URL_BARE = "http://sis.ius.edu.ba"
    val LINK_LIST_SELECTOR_CSS = "#ctl00_treeMenu12"
    val ACADEMIC_TRANSCRIPT_URL = "http://sis.ius.edu.ba/Ogrenci/Ogr0204/Default.aspx?lang=en-US"
    val COURSE_SCHEDULE_URL = "http://sis.ius.edu.ba/Ogrenci/Ogr0205/Default.aspx?lang=en-US"
    val GRADE_DETAILS_URL = "http://sis.ius.edu.ba/Ogrenci/Ogr0201/Default.aspx?lang=en-US"
    val STUDENT_CERTIFICATE_APPLICATION_URL = "http://sis.ius.edu.ba/Ogrenci/Ogr0170/Default.aspx?lang=en-US"
    val IUS_ACADEMIC_CALENDAR_URL = "https://www.ius.edu.ba/academic-calendar"
    val VIEWSTATE_SELECTOR = "__VIEWSTATE"
    val VIEWSTATEGENERATOR_SELECTOR = "__VIEWSTATEGENERATOR"
    val EVENTVALIDATION_SELECTOR = "__EVENTVALIDATION"
    val EVENTTARGET_SELECTOR = "__EVENTTARGET"
    val EVENTARGUMENT_SELECTOR = "__EVENTARGUMENT"

    val cookies: HashMap<String, String> = HashMap()
    val formData: HashMap<String, String> = HashMap()

    // Login forms and related cookies
    lateinit var loginForm: Connection.Response
    lateinit var loginDoc: Document
    lateinit var VIEWSTATE: String
    lateinit var VIEWSTATEGENERATOR: String
    lateinit var EVENTVALIDATION: String

    // Pages to be parsed
    lateinit var homePage: Connection.Response
    lateinit var homePageParsed: Document
    lateinit var gradeDetailsParsed: Document
    lateinit var academicTranscriptParsed: Document
    lateinit var courseScheduleParsed: Document
    lateinit var iusAcademicCalendarParsed: Document
    lateinit var STUDENT: Student
    var COURSES: ArrayList<Course> = ArrayList<Course>()

    lateinit var semesterElements: Elements
    val scheduleRows = ArrayList<ArrayList<String>>()
    var numberOfSemesters = 0

    // User Data
    lateinit var USERNAME: String //
    lateinit var PASSWORD: String //

    // If the user has logged in
    // and if is authenticated
    var IS_LOGGED_IN = false

    // If the data is fetched or not
    var IS_FETCHED = false

    // Error element null if no error
    // nonnull if there is an error
    var errorElement: Element? = null

    var someSize = 0


    fun setUser(user: String, pass: String) {
        USERNAME = user
        PASSWORD = pass
    }

    fun getData(): String{
        IS_LOGGED_IN = true

        getHomepage()

        parseAllOtherData()

        IS_FETCHED = true
        return ""
    }

    private fun parseAllOtherData() {

        gradeDetailsParsed = getPageParsed(GRADE_DETAILS_URL, cookies, USER_AGENT)
        academicTranscriptParsed = getPageParsed(ACADEMIC_TRANSCRIPT_URL, cookies, USER_AGENT)
        courseScheduleParsed = getPageParsed(COURSE_SCHEDULE_URL, cookies, USER_AGENT)

        semesterElements = gradeDetailsParsed.getElementsByClass("card bg-light border-primary mb-12")
        semesterElements.removeAt(0)

        getScheduleParsed()

        for (i in 0 until semesterElements.size){
            COURSES.addAll(
                    getCourses(semesterElements[i])
            )
            someSize++
        }


        STUDENT = getStudent(academicTranscriptParsed)

    }

    private fun getHomepage() {
        loginForm = Jsoup.connect(URL)
            .userAgent(USER_AGENT)
            .method(Connection.Method.GET)
            .execute()

        val loginDoc: Document = loginForm.parse()

        VIEWSTATE = loginDoc
            .select("#" + VIEWSTATE_SELECTOR)
            .attr("value")
        VIEWSTATEGENERATOR = loginDoc
            .select("#" + VIEWSTATEGENERATOR_SELECTOR)
            .attr("value")
        EVENTVALIDATION = loginDoc
            .select("#" + EVENTVALIDATION_SELECTOR)
            .attr("value")

        formData.put(EVENTTARGET_SELECTOR, "")
        formData.put(EVENTARGUMENT_SELECTOR, "")
        formData.put(VIEWSTATE_SELECTOR, VIEWSTATE)
        formData.put(VIEWSTATEGENERATOR_SELECTOR, VIEWSTATEGENERATOR)
        formData.put(EVENTVALIDATION_SELECTOR, EVENTVALIDATION)
        formData.put("txtLogin", USERNAME)
        formData.put("txtPassword", PASSWORD)
        formData.put("btnLogin", "Login")

        cookies.putAll(loginForm.cookies())

        homePage = Jsoup.connect(URL)
            .cookies(cookies)
            .data(formData)
            .method(Connection.Method.POST)
            .execute()

        homePageParsed = homePage.parse()

        errorElement = homePageParsed.getElementById("lblError")
    }

    fun hasError():Boolean {
        getHomepage()
        IS_LOGGED_IN = errorElement == null
        return errorElement != null
    }

    private fun getPageParsed(url: String, cookies: HashMap<String, String>, userAgent: String): Document {
        val page = Jsoup.connect(url)
            .cookies(cookies)
            .userAgent(userAgent)
            .method(Connection.Method.GET)
            .execute()
        return page.parse()
    }

    private fun getStudent(transcriptParsed: Document): Student {
        val name = transcriptParsed.getElementById("ucTranscript1_lblAdi").text()
        val surname = transcriptParsed.getElementById("ucTranscript1_lblSoyadi").text()
        val ID = transcriptParsed.getElementById("ucTranscript1_lblOgrenciNo").text()
        val department = transcriptParsed.getElementById("ucTranscript1_lblDepartman").text()
        val faculty = transcriptParsed.getElementById("ucTranscript1_lblFakulte").text()
        val program = transcriptParsed.getElementById("ucTranscript1_lblDepartmanEns").text()
        val educationLevel = transcriptParsed.getElementById("ucTranscript1_lblProgram").text()

        return Student(
            name,
            surname,
            ID,
            department,
            faculty,
            program,
            educationLevel,
            getGpa(academicTranscriptParsed, semesterElements.size)
        )
    }

    private fun getGrades(gradeElement: Element): ArrayList<Grade> {
        val cards = gradeElement.child(0).children()
        val numOfGrades = cards.size
        val result: ArrayList<Grade> = ArrayList()

        for (i in 0 until numOfGrades) {
            result.add(Grade(
                    cards[i].child(0)
                            .text()
                            .split("(")[0]
                            .trim(),
                    cards[i].child(0)
                            .text()
                            .split("(")[1]
                            .replace(")", "")
                            .replace("%", ""),
                    cards[i].child(1)
                            .text()
            ))
        }
        return result
    }

    private fun getCourses(semester: Element): ArrayList<Course> {
        val result: ArrayList<Course> = ArrayList()

        val semesterChildren = semester
            .getElementsByClass("table table-hover table-sm table-striped table-bordered")
            .first()
            .child(1)
            .children()

        semesterChildren.forEach {child ->
            result.add(Course(
                    child.child(1).text(),
                    getName(child.child(2)).trim(), //child.child(2).text(),
                    child.child(3).text(),
                    child.child(4).text(),
                    child.child(5).text(),
                    getGrades(child.child(6)),
                    child.child(7).text(),
                    child.child(8).text(),
                    child.child(9).text(),
                    child.child(10).text(),
                    child.child(11).text(),
                    child.child(12).text(),
                    semester.getElementsByClass("card-header text-white bg-primary p-2").first().text()
            ))
        }
        return result
    }

    private fun getGpa(transcript: Document, numOfSems: Int): String {
        return if (numOfSems < 10){
            transcript
                    .getElementById("ucTranscript1_rptSemesters_ctl" + "0" + (numOfSems-1) + "_lblTGANO")
                    .text()
        } else {
            transcript
                    .getElementById("ucTranscript1_rptSemesters_ctl" + (numOfSems-1) + "_lblTGANO")
                    .text()
        }

    }

    private fun getName(el: Element): String { //TODO Replace with When
        return if(el.allElements.size == 1){
            el.text()
        } else if(el.allElements.size == 2){
            el.text()
        } else if(el.allElements.size == 4){
            el.allElements.select("a").text()
        } else {
            el.ownText().dropLast(1)
        }
    }

    private fun getScheduleParsed() {
        val ths = courseScheduleParsed.getElementById("grdHaftalikDersProgrami").select("th")
        println(ths.size)
        val els = courseScheduleParsed.getElementById("grdHaftalikDersProgrami").select("td")
        val elsSplit: ArrayList<List<String>> = ArrayList()
        var c = 0

        els.forEach {
            if(c == ths.size) {
                c = 0
            }
            elsSplit.add(it.text().split(" "))
            if(c == 0){
                scheduleRows.add(ArrayList<String>())
                scheduleRows.last().add(it.text().trim())
            } else {
                if (elsSplit.last().isNotEmpty()) {
                    if(elsSplit.last()[0].startsWith("1")) {
                        scheduleRows.last().add(elsSplit.last()[1].dropLast(2))
                    } else {
                        scheduleRows.last().add(elsSplit.last()[0].dropLast(2))
                    }
                } else {
                    scheduleRows.last().add("")
                }
            }
            c += 1
        }
    }

    fun getAcademicCalendar(){
        val iusAcademicCalendar = Jsoup.connect(IUS_ACADEMIC_CALENDAR_URL)
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .execute()
        iusAcademicCalendarParsed = iusAcademicCalendar.parse()
    }

    fun getCalendarRows(): ArrayList<String>{
        val allRows = iusAcademicCalendarParsed.select("tbody").first()
        val numOfRows = allRows.childrenSize()

        val result = ArrayList<String>()

        for (i in 0 until numOfRows ) {
            if(allRows.child(i).childrenSize() == 2) {
                //println(allRows.child(i).child(0).text() + " --- " + allRows.child(i).child(1).text())
                result.add(allRows.child(i).child(0).text() + "#"+ allRows.child(i).child(1).text())
            } else {
                //println(allRows.child(i).text())
                result.add((allRows.child(i).text() + "#" + ""))
            }
        }
        return result
    }

}