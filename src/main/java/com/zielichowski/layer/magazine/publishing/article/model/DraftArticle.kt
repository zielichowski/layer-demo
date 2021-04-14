package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.*
import com.zielichowski.layer.magazine.publishing.common.*

internal class DraftArticle(
    private val articleId: ArticleId,
    private val assignedJournalist: JournalistId,
    private val articleContent: ArticleContent,
    private val assignedCopywriter: CopywriterId,
    private val assignedTopics: List<TopicId> = listOf(),
    private val suggestedChanges: List<ArticleSuggestion> = listOf()
) : Article, Publishable {

    override fun submitToTopic(topicId: TopicId): DraftArticle {
        return DraftArticle(
            articleId,
            assignedJournalist,
            articleContent,
            assignedCopywriter,
            assignedTopics.plus(topicId),
            suggestedChanges
        )
    }

    override fun suggestChanges(copywriterId: CopywriterId, articleSuggestion: ArticleSuggestion): DraftArticle {
        if (copywriterId != this.assignedCopywriter) {
            throw UnableToSuggestChangesException("Only copywriter assigned to article can suggest changes!")
        }
        return DraftArticle(
            articleId,
            assignedJournalist,
            articleContent,
            assignedCopywriter,
            assignedTopics,
            suggestedChanges.plus(articleSuggestion)
        )
    }

    override fun applyChanges(suggestionId: SuggestionId, journalistId: JournalistId): DraftArticle {
        if (journalistId != assignedJournalist) {
            throw UnableToApplyChangesException("Only journalist assigned to article can apply changes!")
        }
        val suggestion = suggestedChanges.first { it.suggestionId == suggestionId }
        val articleContent =
            ArticleContent(Text(suggestion.suggestedChanges), articleContent.title, articleContent.heading)

        val updatedSuggestions = suggestedChanges
            .filter { it.suggestionId == suggestionId }
            .map { ArticleSuggestion(it.suggestionId, it.suggestedChanges, SuggestionState.APPLIED) }

        return DraftArticle(
            articleId,
            assignedJournalist,
            articleContent,
            assignedCopywriter,
            assignedTopics,
            updatedSuggestions
        )
    }

    override fun resolveSuggestion(suggestionId: SuggestionId): Article {
        val updatedSuggestions = suggestedChanges
            .filter { it.suggestionId == suggestionId }
            .map { ArticleSuggestion(it.suggestionId, it.suggestedChanges, SuggestionState.RESOLVED) }

        return DraftArticle(
            articleId,
            assignedJournalist,
            articleContent,
            assignedCopywriter,
            assignedTopics,
            updatedSuggestions
        )
    }

    override fun publish(): Article {
        if (allSuggestionsResolved()) {
            return PublishedArticle(articleId, assignedJournalist, articleContent, assignedTopics)
        } else {
            throw UnableToPublishDraftArticleException("You must resolve all suggestions before publish the article!")
        }
    }

    override fun toDto(): ArticleDto {
        return ArticleDto(
            articleId,
            assignedJournalist,
            articleContent,
            assignedCopywriter,
            assignedTopics,
            suggestedChanges,
            ArticleState.DRAFT
        )
    }

    private fun allSuggestionsResolved(): Boolean {
        return suggestedChanges
            .all { it.state == SuggestionState.RESOLVED }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DraftArticle

        if (articleId != other.articleId) return false
        if (assignedJournalist != other.assignedJournalist) return false
        if (articleContent != other.articleContent) return false
        if (assignedCopywriter != other.assignedCopywriter) return false
        if (assignedTopics != other.assignedTopics) return false
        if (suggestedChanges != other.suggestedChanges) return false

        return true
    }

    override fun hashCode(): Int {
        var result = articleId.hashCode()
        result = 31 * result + assignedJournalist.hashCode()
        result = 31 * result + articleContent.hashCode()
        result = 31 * result + assignedCopywriter.hashCode()
        result = 31 * result + assignedTopics.hashCode()
        result = 31 * result + suggestedChanges.hashCode()
        return result
    }


}