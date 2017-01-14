package me.eddiep.bigdata.http

import me.eddiep.tinyhttp.TinyListener
import me.eddiep.tinyhttp.annotations.GetHandler
import me.eddiep.tinyhttp.net.Request
import me.eddiep.tinyhttp.net.Response

class HttpListener : TinyListener {

    @GetHandler(requestPath = "*")
    fun helloWorld(request: Request, response: Response) {
        response.echo("Hello World :^)")
    }
}