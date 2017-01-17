package me.eddiep.bigdata.data.fetch.impl

import me.eddiep.bigdata.data.fetch.Fetcher
import me.eddiep.bigdata.data.video.Video
import me.eddiep.bigdata.data.video.VideoType
import me.eddiep.bigdata.util.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.Future

class FacebookFetcher(val id: String) : Fetcher {
    override fun fetch(): Future<MutableList<Video>> {
        val request = Request.Builder().url("https://graph.facebook.com/v2.8/$id/feed?fields=object_id,link,updated_time&access_token=${Constants.ACCESS_TOKEN}").build()

        val call = Constants.CLIENT.newCall(request)
        val cancelable = Cancelable.from(call)

        val future = EasyFuture<MutableList<Video>>(cancelable)

        call.enqueue(object : Callback {
            override fun onFailure(p0: Call?, p1: IOException?) {
                future.setValue(ArrayList<Video>())
                p1?.printStackTrace()
            }

            override fun onResponse(p0: Call?, p1: Response?) {
                val json = p1?.body()?.string()
                if (json == null) {
                    future.setValue(ArrayList<Video>())
                    System.err.println("Null response for id $id!")
                    return
                }

                if (json.startsWith("{\"error\"")) {
                    future.setValue(ArrayList<Video>())
                    System.err.println("Error response for id $id!")
                    return
                }

                val feed = Constants.GSON.fromJson(json, FBResponse::class.java)

                val list = ArrayList<Video>()
                for (f in feed.data) {
                    if (future.isCancelled)
                        break

                    if (f.objectId != null) {
                        if (f.link != null) {
                            if (f.link.contains("video")) {
                                val video = Video.toVideo(Constants.FB_EMBED_URL + f.objectId, VideoType.WEBM)
                                list.add(video)
                                continue
                            }
                        }
                    } else if (f.link != null) {
                        if (f.link.contains("youtube")) {
                            val video = Video.toVideo(f.link, VideoType.YOUTUBE)
                            list.add(video)
                            continue
                        }
                    }
                }

                future.setValue(list)
            }

        })

        return future
    }

}