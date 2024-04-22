package com.example.qrscanner
import com.google.gson.annotations.SerializedName

data class SearchRes(
    @SerializedName("kind") val kind: String,
    @SerializedName("title") val title: String,
    @SerializedName("htmlTitle") val htmlTitle: String,
    @SerializedName("link") val link: String,
    @SerializedName("displayLink") val displayLink: String,
    @SerializedName("snippet") val snippet: String,
    @SerializedName("htmlSnippet") val htmlSnippet: String,
    @SerializedName("cacheId") val cacheId: String?,
    @SerializedName("formattedUrl") val formattedUrl: String,
    @SerializedName("htmlFormattedUrl") val htmlFormattedUrl: String,
    @SerializedName("pagemap") val pageMap: PageMap?
)

data class PageMap(
    @SerializedName("cse_thumbnail") val cseThumbnail: List<CseThumbnail>?,
    @SerializedName("metatags") val metaTags: List<MetaTag>?,
    @SerializedName("cse_image") val cseImage: List<CseImage>?
)

data class CseThumbnail(
    @SerializedName("src") val src: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String
)

data class MetaTag(
    @SerializedName("og:image") val ogImage: String,
    @SerializedName("next-head-count") val nextHeadCount: String,
    @SerializedName("fb:app_id") val fbAppId: String,
    @SerializedName("og:type") val ogType: String,
    @SerializedName("og:site_name") val ogSiteName: String,
    @SerializedName("viewport") val viewport: String,
    @SerializedName("og:title") val ogTitle: String,
    @SerializedName("og:url") val ogUrl: String,
    @SerializedName("og:description") val ogDescription: String
)

data class CseImage(
    @SerializedName("src") val src: String
)
