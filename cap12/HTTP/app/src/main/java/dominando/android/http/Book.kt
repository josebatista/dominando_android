package dominando.android.http

data class Book(
    var title: String = "",
    var category: String = "",
    var author: String = "",
    var year: Int = 0,
    var pages: Int = 0,
    var coverUrl: String = ""
) {
    override fun toString() = title
}