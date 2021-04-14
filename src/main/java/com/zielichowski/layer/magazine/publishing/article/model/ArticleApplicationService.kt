package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.*

internal class ArticleApplicationService(
    private val articleRepository: ArticleRepository,
    private val articleEvents: ArticleEvents
) {
    fun respondToSuggestion(respondToSuggestionCommand: RespondToSuggestionCommand) {
        val article = ArticleFactory.from(articleRepository.findById(respondToSuggestionCommand.articleId))

        val articleAfterChanges =
            article.applyChanges(respondToSuggestionCommand.suggestionId, respondToSuggestionCommand.journalistId)

        articleEvents.emit(
            RespondedToSuggestionEvent(
                respondToSuggestionCommand.articleId,
                respondToSuggestionCommand.suggestionId,
                respondToSuggestionCommand.journalistId
            )
        )
        articleRepository.save(articleAfterChanges.toDto())
    }

    fun resolveSuggestion(resolveSuggestionCommand: ResolveSuggestionCommand) {
        val article = ArticleFactory.from(articleRepository.findById(resolveSuggestionCommand.articleId))
        val articleWithResolvedSuggestion = article.resolveSuggestion(resolveSuggestionCommand.suggestionId)

        articleEvents.emit(
            SuggestionResolvedEvent(
                resolveSuggestionCommand.articleId,
                resolveSuggestionCommand.suggestionId
            )
        )
        articleRepository.save(articleWithResolvedSuggestion.toDto())
    }

    fun publishArticle(publishArticleCommand: PublishArticleCommand) {
        val article = ArticleFactory.from(articleRepository.findById(publishArticleCommand.articleId))

        val publishedArticle = ArticlePublisher.publishArticle(article)
        articleEvents.emit(
            ArticlePublishedEvent(
                publishArticleCommand.articleId,
            )
        )
        articleRepository.save(publishedArticle.toDto())
    }
}

internal class ArticlePublisher {
    companion object {
        fun publishArticle(article: Article) = when (article) {
            is Publishable -> article.publish()
            else -> throw ArticlePublisherException("Unable to publish article")
        }
    }
}

internal class ArticleFactory {
    companion object {
        fun from(articleDto: ArticleDto): Article = when (articleDto.articleState) {
            ArticleState.DRAFT -> DraftArticle(
                articleDto.articleId,
                articleDto.assignedJournalist,
                articleDto.articleContent,
                articleDto.assignedCopywriter!!,
                articleDto.assignedTopics,
                articleDto.suggestedChanges!!
            )
            ArticleState.PUBLISHED -> PublishedArticle(
                articleDto.articleId,
                articleDto.assignedJournalist,
                articleDto.articleContent,
                articleDto.assignedTopics
            )
        }
    }
}