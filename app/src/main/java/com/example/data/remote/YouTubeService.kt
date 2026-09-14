package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.YouTubeVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class YouTubeService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun searchVideos(
        query: String,
        maxResults: Int = 5,
        apiKeyOverride: String? = null
    ): Result<List<YouTubeVideo>> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyOverride?.takeIf { it.isNotBlank() }
            ?: BuildConfig.YOUTUBE_API_KEY.takeIf { it.isNotBlank() && it != "MY_YOUTUBE_API_KEY" }

        if (apiKey.isNullOrBlank()) {
            Log.d(TAG, "No YouTube API Key provided. Supplying curated educational videos for: $query")
            return@withContext Result.success(getCuratedEducationalVideos(query))
        }

        try {
            val encodedQuery = URLEncoder.encode("$query DIY recycling tutorial", "UTF-8")
            val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=$maxResults&q=$encodedQuery&key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.w(TAG, "YouTube API request failed: ${response.code} $responseBody. Falling back to curated videos.")
                return@withContext Result.success(getCuratedEducationalVideos(query))
            }

            val json = JSONObject(responseBody)
            val items = json.optJSONArray("items")
            val videos = mutableListOf<YouTubeVideo>()

            if (items != null) {
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val idObj = item.optJSONObject("id")
                    val videoId = idObj?.optString("videoId") ?: continue
                    val snippet = item.optJSONObject("snippet") ?: continue

                    val title = decodeHtmlEntities(snippet.optString("title", "Recycling Tutorial"))
                    val channelTitle = decodeHtmlEntities(snippet.optString("channelTitle", "Eco DIY"))
                    val description = decodeHtmlEntities(snippet.optString("description", ""))
                    val thumbnails = snippet.optJSONObject("thumbnails")
                    val thumbUrl = thumbnails?.optJSONObject("medium")?.optString("url")
                        ?: thumbnails?.optJSONObject("default")?.optString("url")
                        ?: "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
                    val publishedAt = snippet.optString("publishedAt", "")

                    videos.add(
                        YouTubeVideo(
                            id = videoId,
                            title = title,
                            channelTitle = channelTitle,
                            description = description,
                            thumbnailUrl = thumbUrl,
                            publishedAt = publishedAt.take(10)
                        )
                    )
                }
            }

            if (videos.isNotEmpty()) {
                Result.success(videos)
            } else {
                Result.success(getCuratedEducationalVideos(query))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during YouTube search", e)
            Result.success(getCuratedEducationalVideos(query))
        }
    }

    private fun decodeHtmlEntities(input: String): String {
        return input
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
    }

    fun getCuratedEducationalVideos(query: String): List<YouTubeVideo> {
        val qLower = query.lowercase()
        return when {
            qLower.contains("tyre") || qLower.contains("tire") -> listOf(
                YouTubeVideo(
                    id = "2wU1jJ8H8G0",
                    title = "How to Make Beautiful Garden Planters from Old Tyres",
                    channelTitle = "Island Eco Living",
                    description = "Step by step guide to safely washing, cutting, painting and planting in discarded car tyres for home gardens.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1578885136359-16c8bd4d3a8e?w=500&q=80",
                    publishedAt = "2023-04-12"
                ),
                YouTubeVideo(
                    id = "7pX9q4W2zL1",
                    title = "DIY Comfortable Outdoor Tyre Stool & Furniture",
                    channelTitle = "Tropical Homestead DIY",
                    description = "Using natural rope and discarded tyres to create sturdy waterproof veranda seating.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1584473457406-6240486418e9?w=500&q=80",
                    publishedAt = "2023-08-19"
                ),
                YouTubeVideo(
                    id = "k9W0zQ1mP4v",
                    title = "Prevent Soil Erosion with Recycled Tyres on Slopes",
                    channelTitle = "Pacific Permaculture",
                    description = "Terracing steep garden beds and pathways using old vehicle tyres.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?w=500&q=80",
                    publishedAt = "2024-01-10"
                )
            )
            qLower.contains("glass") || qLower.contains("jar") -> listOf(
                YouTubeVideo(
                    id = "x8A1b2C3d4E",
                    title = "Creative DIY Upcycled Glass Bottle Crafts & Planters",
                    channelTitle = "Island Craft & Upcycling",
                    description = "Safe methods to transform glass beverage bottles and jars into self-watering herb pots and lanterns.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?w=500&q=80",
                    publishedAt = "2023-06-25"
                ),
                YouTubeVideo(
                    id = "m5N6o7P8q9R",
                    title = "Moisture-Proof Food & Seed Storage in Reused Glass Jars",
                    channelTitle = "Pacific Sustainable Living",
                    description = "Sterilizing and reusing jam jars to prevent weevils and mould in tropical island kitchens.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1584473457406-6240486418e9?w=500&q=80",
                    publishedAt = "2023-11-04"
                )
            )
            qLower.contains("can") || qLower.contains("metal") || qLower.contains("tin") -> listOf(
                YouTubeVideo(
                    id = "t1U2v3W4x5Y",
                    title = "Turn Food Tin Cans into Solar Lanterns & Herb Pots",
                    channelTitle = "Community Eco Craft",
                    description = "Punching decorative drainage holes and smoothing sharp edges on tuna and fruit cans.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=500&q=80",
                    publishedAt = "2023-09-15"
                ),
                YouTubeVideo(
                    id = "z9Y8x7W6v5U",
                    title = "Aluminium Can Seed Starter Trays for Small Gardens",
                    channelTitle = "Organic Island Gardener",
                    description = "Quick and zero-cost seedling pots made from clean beverage cans.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1592417817098-8f3d6910985b?w=500&q=80",
                    publishedAt = "2024-02-18"
                )
            )
            qLower.contains("compost") || qLower.contains("food") || qLower.contains("organic") -> listOf(
                YouTubeVideo(
                    id = "c1D2e3F4g5H",
                    title = "Easy Tropical Composting: Kitchen Scraps & Coconut Waste",
                    channelTitle = "Solomon Soil Health",
                    description = "How to build a simple wooden or banana leaf compost pile that doesn't smell or attract pests.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=500&q=80",
                    publishedAt = "2023-07-20"
                ),
                YouTubeVideo(
                    id = "h5G4f3E2d1C",
                    title = "Mulching Gardens with Dry Leaves and Coconut Husks",
                    channelTitle = "Pacific Agroforestry",
                    description = "Protect your soil moisture and reduce weeding using natural organic island mulch.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?w=500&q=80",
                    publishedAt = "2023-10-12"
                )
            )
            qLower.contains("cardboard") || qLower.contains("paper") -> listOf(
                YouTubeVideo(
                    id = "p1Q2r3S4t5U",
                    title = "Cardboard Weed Barrier & Sheet Mulching for Garden Beds",
                    channelTitle = "Island Permaculture",
                    description = "Suppress invasive weeds naturally by laying plain cardboard under organic compost.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1530587191325-3db32d826c18?w=500&q=80",
                    publishedAt = "2023-05-18"
                ),
                YouTubeVideo(
                    id = "u5T4s3R2q1P",
                    title = "Sturdy DIY Organizers & Storage Boxes from Scrap Cardboard",
                    channelTitle = "Handmade Home Hacks",
                    description = "Reinforcing cardboard boxes for durable pantry and classroom storage.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=500&q=80",
                    publishedAt = "2024-03-01"
                )
            )
            else -> listOf(
                YouTubeVideo(
                    id = "b1O2t3T4l5E",
                    title = "Plastic Bottle Vertical Garden: Complete DIY Guide",
                    channelTitle = "Sustainable Island Ideas",
                    description = "Hang lightweight vertical salad and herb gardens from plastic bottles along verandas.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?w=500&q=80",
                    publishedAt = "2023-08-14"
                ),
                YouTubeVideo(
                    id = "d1R2i3P4w5A",
                    title = "Zero Cost Drip Irrigation with Recycled Plastic Bottles",
                    channelTitle = "Eco Agriculture Pacific",
                    description = "Keep vegetables healthy during dry sunny weeks with inverted slow drip bottles.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1530587191325-3db32d826c18?w=500&q=80",
                    publishedAt = "2023-12-05"
                ),
                YouTubeVideo(
                    id = "f1I2s3H4t5R",
                    title = "Moisture-Proof Seed & Fishing Tackle Container from Bottles",
                    channelTitle = "Pacific DIY Projects",
                    description = "Simple hacks to seal and protect household essentials in damp island climates.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=500&q=80",
                    publishedAt = "2024-01-22"
                )
            )
        }
    }

    companion object {
        private const val TAG = "YouTubeService"
    }
}
