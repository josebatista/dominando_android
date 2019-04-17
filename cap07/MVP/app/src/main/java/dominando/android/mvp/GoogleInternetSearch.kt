package dominando.android.mvp

class GoogleInternetSearch : InternetSearch {

    override fun search(term: String, listener: SearchResultListener) {
        //faça a busca
        //devolva os resultados
        val results = emptyList<SearchResult>()
        listener.onSearchResult(results)
    }
}