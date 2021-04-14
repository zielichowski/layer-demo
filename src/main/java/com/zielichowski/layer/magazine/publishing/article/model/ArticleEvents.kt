package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticlePublishedEvent
import com.zielichowski.layer.magazine.publishing.article.api.RespondedToSuggestionEvent
import com.zielichowski.layer.magazine.publishing.article.api.SuggestionResolvedEvent

interface ArticleEvents {
    fun emit(respondedToSuggestionEvent: RespondedToSuggestionEvent)
    fun emit(suggestionResolvedEvent: SuggestionResolvedEvent)
    fun emit(articlePublishedEvent: ArticlePublishedEvent)
}

