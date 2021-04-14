package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.*
import com.zielichowski.layer.magazine.publishing.common.CopywriterId
import com.zielichowski.layer.magazine.publishing.common.JournalistId
import com.zielichowski.layer.magazine.publishing.common.SuggestionId
import com.zielichowski.layer.magazine.publishing.common.TopicId

internal class PublishedArticle(
    private val articleId: ArticleId,
    private val author: JournalistId,
    private val articleContent: ArticleContent,
    private val assignedTopics: List<TopicId>,
) : Article {
    override fun resolveSuggestion(suggestionId: SuggestionId): Article {
        throw PublishedArticleException("Cannot resolve suggestions in already published article!")
    }

    override fun submitToTopic(topicId: TopicId): Article {
        throw PublishedArticleException("Cannot submit to topic already published article!")
    }

    override fun suggestChanges(copywriterId: CopywriterId, articleSuggestion: ArticleSuggestion): Article {
        throw PublishedArticleException("Cannot suggest changes to already published article!")
    }

    override fun applyChanges(suggestionId: SuggestionId, journalistId: JournalistId): Article {
        throw PublishedArticleException("Cannot apply changes to already published article!")
    }

    override fun toDto(): ArticleDto {
        return ArticleDto(
            articleId,
            author,
            articleContent,
            null,
            assignedTopics,
            null,
            ArticleState.PUBLISHED
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublishedArticle

        if (articleId != other.articleId) return false
        if (author != other.author) return false
        if (articleContent != other.articleContent) return false
        if (assignedTopics != other.assignedTopics) return false

        return true
    }

    override fun hashCode(): Int {
        var result = articleId.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + articleContent.hashCode()
        result = 31 * result + assignedTopics.hashCode()
        return result
    }


}