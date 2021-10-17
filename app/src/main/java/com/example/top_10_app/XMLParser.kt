package com.example.top_10_app

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class Question(val title: String?)



class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Question> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readQuestions(parser)
        }
    }

    private fun readQuestions(parser: XmlPullParser): List<Question> {
        val questions = mutableListOf<Question>()
        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var summary: String? = null
                var id: String? = null

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "im:name" -> title = readTitle(parser)
                        //"summary" -> summary = readSummary(parser)
                      //  "id" -> id = readid(parser)

                        else -> skip(parser)
                    }
                }
                questions.add(Question(title))
            } else {
                skip(parser)
            }
        }
        return questions
    }



    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:name")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:name")
        return title
    }


    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}