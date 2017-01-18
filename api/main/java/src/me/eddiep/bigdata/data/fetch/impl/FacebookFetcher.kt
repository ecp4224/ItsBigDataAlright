package me.eddiep.bigdata.data.fetch.impl

import me.eddiep.bigdata.data.Reaction
import me.eddiep.bigdata.data.facebook.impl.FBFeedResponse
import me.eddiep.bigdata.data.facebook.impl.FBReactionResponse
import me.eddiep.bigdata.data.facebook.impl.FeedItem
import me.eddiep.bigdata.data.fetch.Fetcher
import me.eddiep.bigdata.data.video.Video
import me.eddiep.bigdata.data.video.VideoType
import me.eddiep.bigdata.util.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.Future

class FacebookFetcher(val id: String) : Fetcher {
    private var nextFetchURL = "https://graph.facebook.com/v2.8/$id/feed?fields=object_id,link,updated_time&access_token=${Constants.ACCESS_TOKEN}"

    override fun fetch(): Future<MutableList<Video>> {
        return executeURL(URL(nextFetchURL), null)
    }

    private fun executeURL(fetch: URL, nullFuture: EasyFuture<MutableList<Video>>?) : EasyFuture<MutableList<Video>> {
        val request = Request.Builder().url(fetch).build()

        val call = Constants.CLIENT.newCall(request)

        val future = if (nullFuture != null) nullFuture else  {
            val cancelable = Cancelable.from(call)
            EasyFuture(cancelable)
        }


        call.enqueue(object : Callback {
            override fun onFailure(p0: Call?, p1: IOException?) {
                future.end(ArrayList<Video>())
                p1?.printStackTrace()
            }

            override fun onResponse(p0: Call?, p1: Response?) {
                val json = p1?.body()?.string()
                if (json == null) {
                    future.end(ArrayList<Video>())
                    System.err.println("Null response for id $id!")
                    return
                }

                if (json.startsWith("{\"error\"")) {
                    future.end(ArrayList<Video>())
                    System.err.println("Error response for id $id!")
                    return
                }

                val feed = Constants.GSON.fromJson(json, FBFeedResponse::class.java)

                val list = if (future.savedValue != null) future.savedValue else ArrayList<Video>()
                for (f in feed.data) {
                    if (future.isCancelled)
                        break

                    val reacts = reactionsFor(f)

                    if (f.objectId != null) {
                        if (f.link != null) {
                            if (f.link.contains("video")) {
                                val video = Video.toVideo(Constants.FB_EMBED_URL + f.objectId, VideoType.WEBM, reacts)
                                list.add(video)
                                continue
                            }
                        }
                    } else if (f.link != null) {
                        if (f.link.contains("youtube")) {
                            val video = Video.toVideo(f.link, VideoType.YOUTUBE, reacts)
                            list.add(video)
                            continue
                        } else if (f.link.contains("facebook")) {
                            val slashList = f.link.split("/")
                            var id = ""
                            var i = 0
                            do {
                                i++
                                id = slashList[slashList.size - i]
                            } while (id.trim().equals(""))

                            val video = Video.toVideo(Constants.FB_EMBED_URL + id, VideoType.WEBM, reacts)
                            list.add(video)
                            continue
                        }
                    }
                }

                if (feed.paging != null && feed.paging.next != null) {
                    future.saveValue(list)
                    nextFetchURL = feed.paging.next
                    executeURL(URL(feed.paging.next), future)
                } else {
                    future.end(list)
                }
            }

        })

        return future
    }

    private fun reactionsFor(item: FeedItem): Reaction {
        val request = Request.Builder().url("https://graph.facebook.com/v2.8/${item.id}/reactions?access_token=${Constants.ACCESS_TOKEN}").build()

        val response = Constants.CLIENT.newCall(request).execute()

        val json = response.body().string()

        val why = Constants.GSON.fromJson(json, FBReactionResponse::class.java)

        var likes = 0
        var love = 0
        var sad = 0
        var angry = 0
        var wow = 0
        var haha = 0
        for (pls in why.data) {
            when (pls.type) {
                "LIKE" -> likes++
                "WOW" -> wow++
                "LOVE" -> love++
                "HAHA" -> haha++
                "SAD" -> sad++
                "ANGRY" -> angry++
            }
        }

        return Reaction(likes, angry, wow, haha, love, sad)
    }
}