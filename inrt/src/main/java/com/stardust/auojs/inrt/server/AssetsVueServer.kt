package com.stardust.auojs.inrt.server

import com.stardust.app.GlobalAppContext
import fi.iki.elonen.NanoHTTPD
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class AssetsVueServer:NanoHTTPD("0.0.0.0",8080) {
    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        var filename = uri.substring(1)
        var mimetype = "text/html"
        if (uri == "/") filename = "index.html"
        if (filename.contains(".html") || filename.contains(".htm")) {
            mimetype = "text/html"
            return stringResponse(filename, mimetype)
        } else if (filename.contains(".js")) {
            mimetype = "text/javascript"
            return stringResponse(filename, mimetype)
        } else if (filename.contains(".css")) {
            mimetype = "text/css"
            return stringResponse(filename, mimetype)
        } else if (filename.contains(".jpeg") || filename.contains(".jpg")) {
            mimetype = "image/jpeg"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".png")) {
            mimetype = "image/png"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".gif")) {
            mimetype = "image/gif"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".ico")) {
            mimetype = "image/x-icon"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".woff2")) {
            mimetype = "application/font-woff2"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".woff")) {
            mimetype = "application/font-woff"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".ttf")) {
            mimetype = "application/octet-stream"
            return fileResponse(filename, mimetype)
        } else if (filename.contains(".eot")) {
            mimetype = "application/vnd.ms-fontobject"
            return fileResponse(filename, mimetype)
        } else {
            filename = "index.html"
            mimetype = "text/html"
            return stringResponse(filename, mimetype)
        }
    }
    private fun stringResponse(filename : String, mimetype : String): Response {
        var response: String? = ""
        var line: String? = ""
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(GlobalAppContext.get().assets.open("HCWeb/$filename")))
            while (reader.readLine().also { line = it } != null) {
                response += line
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return newFixedLengthResponse(Response.Status.OK, mimetype, response)
    }

    private fun fileResponse(filename : String, mimetype : String): Response {
        var inputFile : InputStream? = null
        try {
            inputFile = GlobalAppContext.get().assets.open("HCWeb/$filename")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return newFixedLengthResponse(Response.Status.OK, mimetype, inputFile, 20480 )
    }
}