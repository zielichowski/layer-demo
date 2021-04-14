package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticleDto
import com.zielichowski.layer.magazine.publishing.common.CopywriterId
import com.zielichowski.layer.magazine.publishing.common.JournalistId
import com.zielichowski.layer.magazine.publishing.common.SuggestionId
import com.zielichowski.layer.magazine.publishing.common.TopicId

interface Article {
    fun submitToTopic(topicId: TopicId) : Article
    fun suggestChanges(copywriterId: CopywriterId, articleSuggestion: ArticleSuggestion): Article
    fun applyChanges(suggestionId: SuggestionId, journalistId: JournalistId): Article
    fun resolveSuggestion(suggestionId: SuggestionId): Article
    fun toDto(): ArticleDto
}