package com.zielichowski.layer.magazine.publishing.article.api

import com.zielichowski.layer.magazine.publishing.article.model.ArticleSuggestion
import com.zielichowski.layer.magazine.publishing.common.*

data class ArticleId(val id: String)

data class ArticleContent(
    val text: Text,
    val title: Title,
    val heading: Heading
)

enum class ArticleState {
    DRAFT,
    PUBLISHED
}

data class ArticleDto(
    val articleId: ArticleId,
    val assignedJournalist: JournalistId,
    val articleContent: ArticleContent,
    val assignedCopywriter: CopywriterId?,
    val assignedTopics: List<TopicId>,
    val suggestedChanges: List<ArticleSuggestion>?,
    val articleState: ArticleState
)

data class RespondToSuggestionCommand(
    val articleId: ArticleId,
    val suggestionId: SuggestionId,
    val journalistId: JournalistId
)

data class RespondedToSuggestionEvent(
    val articleId: ArticleId,
    val suggestionId: SuggestionId,
    val journalistId: JournalistId
)

data class ResolveSuggestionCommand(
    val articleId: ArticleId,
    val suggestionId: SuggestionId
)

data class SuggestionResolvedEvent(
    val articleId: ArticleId,
    val suggestionId: SuggestionId
)

data class PublishArticleCommand(
    val articleId: ArticleId
)

data class ArticlePublishedEvent(
    val articleId: ArticleId
)


class UnableToSuggestChangesException(message: String) : RuntimeException(message)
class UnableToApplyChangesException(message: String) : RuntimeException(message)
class UnableToPublishDraftArticleException(message: String) : RuntimeException(message)
class PublishedArticleException(message: String) : RuntimeException(message)
class ArticlePublisherException(message: String) : RuntimeException(message)