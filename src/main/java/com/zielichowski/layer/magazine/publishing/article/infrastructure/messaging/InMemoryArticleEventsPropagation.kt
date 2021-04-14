package com.zielichowski.layer.magazine.publishing.article.infrastructure.messaging

import com.zielichowski.layer.magazine.publishing.article.api.ArticlePublishedEvent
import com.zielichowski.layer.magazine.publishing.article.api.RespondedToSuggestionEvent
import com.zielichowski.layer.magazine.publishing.article.api.SuggestionResolvedEvent
import com.zielichowski.layer.magazine.publishing.article.model.ArticleEvents

internal class InMemoryArticleEventsPropagation : ArticleEvents {

    override fun emit(respondedToSuggestionEvent: RespondedToSuggestionEvent) {
        //Propagating event to others contexts or query models
        print("Emitted event $respondedToSuggestionEvent")
    }

    override fun emit(suggestionResolvedEvent: SuggestionResolvedEvent) {
        //Propagating event to others contexts or query models
        print("Emitted event $suggestionResolvedEvent")
    }

    override fun emit(articlePublishedEvent: ArticlePublishedEvent) {
        //Propagating event to others contexts or query models
        print("Emitted event $articlePublishedEvent")
    }
}
