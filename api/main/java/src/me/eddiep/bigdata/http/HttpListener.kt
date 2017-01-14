package me.eddiep.bigdata.http

import me.eddiep.bigdata.data.VideoStore
import me.eddiep.bigdata.data.video.Video
import me.eddiep.bigdata.data.video.VideoType
import me.eddiep.bigdata.util.Constants
import me.eddiep.tinyhttp.TinyListener
import me.eddiep.tinyhttp.annotations.GetHandler
import me.eddiep.tinyhttp.net.Request
import me.eddiep.tinyhttp.net.Response

class HttpListener : TinyListener {

    @GetHandler(requestPath = "/v1/m?.*")
    fun fetchV1(request: Request, response: Response) {
        var count = 1
        var type = "mix"
        val params = request.fileRequest.split("?")[1]
        if (!params.trim().equals("")) {
            val options = params.split("&")
            for (option in options) {
                val key = option.split("=")[0]
                val value = option.split("=")[1]

                if (key.toLowerCase().equals("count")) {
                    count = value.toInt()
                } else if (key.toLowerCase().equals("type")) {
                    type = value
                }
            }
        }

        if (count == 1) {
            var video = if (type.equals("mix"))
                VideoStore.getRandomVideo()
            else
                VideoStore.getRandomVideo(VideoType.parse(type))

            response.echo(Constants.GSON.toJson(video))
        } else if (count > 1) {
            var videos = if (type.equals("mix"))
                VideoStore.getRandomVideos(count)
            else
                VideoStore.getRandomVideos(count, VideoType.parse(type))

            response.echo(Constants.GSON.toJson(videos))
        } else {
            response.echo("[]")
        }
    }
}