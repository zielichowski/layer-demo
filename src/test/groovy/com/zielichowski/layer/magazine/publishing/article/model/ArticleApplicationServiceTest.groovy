package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.*
import com.zielichowski.layer.magazine.publishing.article.infrastructure.persistence.InMemoryArticleRepository
import com.zielichowski.layer.magazine.publishing.common.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class ArticleApplicationServiceTest extends Specification {

    @Shared
    def journalistId = new JournalistId(UUID.randomUUID().toString())
    @Shared
    def articleId = new ArticleId(UUID.randomUUID().toString())
    @Shared
    def suggestionId = new SuggestionId(UUID.randomUUID().toString())
    @Shared
    def copywriterId = new CopywriterId(UUID.randomUUID().toString())
    @Shared
    def topicId = new TopicId(UUID.randomUUID().toString())
    @Shared
    def repo = new InMemoryArticleRepository()

    def events = Mock(ArticleEvents)

    @Subject
    def articleApplicationService = new ArticleApplicationService(repo, events)

    def setupSpec() {
    }

    def "Should responded to suggestion"() {
        given: "Initial state of aggregate"
        repo.save(
                new ArticleDto(articleId, journalistId, testArticleContent(), copywriterId,
                        List.of(topicId),
                        List.of(new ArticleSuggestion(suggestionId, "Some changes", SuggestionState.OPEN)),
                        ArticleState.DRAFT))

        and: "Respond to suggestion command"
        def command = new RespondToSuggestionCommand(articleId, suggestionId, journalistId)

        and: "Expected event"
        def event = new RespondedToSuggestionEvent(articleId, suggestionId, journalistId)

        when: "Service performs use case"
        articleApplicationService.respondToSuggestion(command)

        then: "Entity is updated correctly"
        repo.findById(articleId).suggestedChanges.find { it.suggestionId == suggestionId }.state == SuggestionState.APPLIED

        and: "Event is emitted"
        1 * events.emit(event)
    }

    def "Should resolve suggestion"() {
        given: "Initial state of aggregate"
        repo.save(
                new ArticleDto(articleId, journalistId, testArticleContent(), copywriterId,
                        List.of(topicId),
                        List.of(new ArticleSuggestion(suggestionId, "Some changes", SuggestionState.APPLIED)),
                        ArticleState.DRAFT))

        and: "Resolve suggestion command"
        def command = new ResolveSuggestionCommand(articleId, suggestionId)

        and: "Expected event"
        def event = new SuggestionResolvedEvent(articleId, suggestionId)

        when: "Service performs use case"
        articleApplicationService.resolveSuggestion(command)

        then: "Entity is updated correctly"
        repo.findById(articleId).suggestedChanges.find { it.suggestionId == suggestionId }.state == SuggestionState.RESOLVED

        and: "Event is emitted"
        1 * events.emit(event)
    }

    def "Should publish article"() {
        given: "Initial state of aggregate"
        repo.save(
                new ArticleDto(articleId, journalistId, testArticleContent(), copywriterId,
                        List.of(topicId),
                        List.of(new ArticleSuggestion(suggestionId, "Some changes", SuggestionState.RESOLVED)),
                        ArticleState.DRAFT))

        and: "Publish article"
        def command = new PublishArticleCommand(articleId)

        and: "Expected event"
        def event = new ArticlePublishedEvent(articleId)

        when: "Service performs use case"
        articleApplicationService.publishArticle(command)

        then: "Entity is updated correctly"
        repo.findById(articleId).articleState == ArticleState.PUBLISHED

        and: "Event is emitted"
        1 * events.emit(event)
    }

    private static ArticleContent testArticleContent() {
        def articleText = new Text("Some article text")
        def articleTitle = new Title("Some title")
        def articleHeading = new Heading("Some heading")

        def articleContent = new ArticleContent(articleText, articleTitle, articleHeading)
        articleContent
    }

}
