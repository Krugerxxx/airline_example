package app.services.interfaces;

import app.entities.search.SearchResult;


public interface SearchResultService {

    void saveSearchResult(SearchResult searchResult);
    SearchResult findById(long id);
}
