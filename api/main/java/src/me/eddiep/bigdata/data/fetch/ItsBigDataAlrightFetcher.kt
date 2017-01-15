package me.eddiep.bigdata.data.fetch

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

class ItsBigDataAlrightFetcher : Fetcher {
    override fun fetch(): Future<MutableList<Video>> {
        val request = Request.Builder().url("https://graph.facebook.com/v2.8/${Constants.GROUP_ID}/feed?fields=object_id,link,updated_time&access_token=${Constants.ACCESS_TOKEN}").build()

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

                val feed = Constants.GSON.fromJson(json, FBResponse::class.java)

                val list = ArrayList<Video>()
                for (f in feed.data) {
                    if (future.isCancelled)
                        break

                    if (f.objectId != null) {
                        if (f.link != null) {
                            if (f.link.contains("video")) {
                                val video = Video.toVideo(Constants.FB_EMBED_URL.replace("{ID}", f.objectId), VideoType.WEBM)
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