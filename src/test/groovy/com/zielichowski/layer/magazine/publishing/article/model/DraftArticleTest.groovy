package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticleContent
import com.zielichowski.layer.magazine.publishing.article.api.ArticleId
import com.zielichowski.layer.magazine.publishing.article.api.UnableToApplyChangesException
import com.zielichowski.layer.magazine.publishing.article.api.UnableToPublishDraftArticleException
import com.zielichowski.layer.magazine.publishing.article.api.UnableToSuggestChangesException
import com.zielichowski.layer.magazine.publishing.common.*
import spock.lang.Shared
import spock.lang.Specification

class DraftArticleTest extends Specification {

    @Shared
    def journalistId = new JournalistId(UUID.randomUUID().toString())
    @Shared
    def articleId = new ArticleId(UUID.randomUUID().toString())
    @Shared
    def copywriterId = new CopywriterId(UUID.randomUUID().toString())
    @Shared
    def suggestionId = new SuggestionId(UUID.randomUUID().toString())
    @Shared
    def emptyTopicList = new ArrayList()
    @Shared
    def emptySuggestionList = new ArrayList()


    def "Should submit draft article to a given topic"() {
        given: "Topic in magazine"
        def topicId = new TopicId(UUID.randomUUID().toString())

        and: "Article content"
        ArticleContent articleContent = testArticleContent()

        and: "Empty draft article"
        def draftArticle = emptyDraftArticle()

        and: "Expected result"
        def expectedArticle = new DraftArticle(articleId, journalistId, articleContent, copywriterId, List.of(topicId), emptySuggestionList)

        when: "Draft article is submitted to topic"
        def resultArticle = draftArticle.submitToTopic(topicId)

        then: "Article is linked to the topic"
        expectedArticle == resultArticle
    }

    def "Should suggest changes to draft article"() {
        given: "Article"
        def draftArticle = emptyDraftArticle()

        and: "Suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.OPEN)

        and: "Expected results"
        def expectedResult = new DraftArticle(articleId, journalistId, testArticleContent(), copywriterId, emptyTopicList, List.of(suggestion))

        when: "Copywriter suggesting changes"
        def resultArticle = draftArticle.suggestChanges(copywriterId, suggestion)

        then: "Suggested changes are added"
        expectedResult == resultArticle
    }

    def "Should not allow to suggest changes to unassigned copywriter to draft article"() {
        given: "Article"
        def draftArticle = emptyDraftArticle()

        and: "Suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.OPEN)

        and: "Expected results"
        def expectedResult = new DraftArticle(articleId, journalistId, testArticleContent(), copywriterId, emptyTopicList, List.of(suggestion))

        and: "Random copywriter"
        def randomCopywriter = new CopywriterId(UUID.randomUUID().toString())

        when: "Copywriter suggesting changes"
        def resultArticle = draftArticle.suggestChanges(randomCopywriter, suggestion)

        then: "Exception is thrown"
        thrown(UnableToSuggestChangesException)
    }

    def "Should apply suggest changes to draft article"() {
        given: "Suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.OPEN)

        and: "Article with suggestions"
        def draftArticle = new DraftArticle(articleId, journalistId, testArticleContent(), copywriterId, emptyTopicList, List.of(suggestion))

        and: "Expected article content"
        def expectedArticleContent = new ArticleContent(new Text("Some suggested changes"), new Title("Some title"), new Heading("Some heading"))

        and: "Expected suggestion"
        def expectedSuggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.APPLIED)

        and: "Expected article"
        def expectedResult = new DraftArticle(articleId, journalistId, expectedArticleContent, copywriterId, emptyTopicList, List.of(expectedSuggestion))

        when: "Journalist applies suggested changes"
        def resultArticle = draftArticle.applyChanges(suggestion.suggestionId, journalistId)

        then: "Suggested changes are added"
        expectedResult == resultArticle
    }

    def "Should not allow to apply suggest changes unassigned journalist to draft article"() {
        given: "Suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.OPEN)

        and: "Article with suggestions"
        def draftArticle = new DraftArticle(articleId, journalistId, testArticleContent(), copywriterId, emptyTopicList, List.of(suggestion))

        and: "Expected article content"
        def expectedArticleContent = new ArticleContent(new Text("Some suggested changes"), new Title("Some title"), new Heading("Some heading"))

        and: "Expected suggestion"
        def expectedSuggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.APPLIED)

        and: "Expected article"
        def expectedResult = new DraftArticle(articleId, journalistId, expectedArticleContent, copywriterId, emptyTopicList, List.of(expectedSuggestion))

        and: "Random journalist"
        def randomJournalist = new JournalistId(UUID.randomUUID().toString())

        when: "Journalist applies suggested changes"
        def resultArticle = draftArticle.applyChanges(suggestion.suggestionId, randomJournalist)

        then: "Exception is thrown"
        thrown(UnableToApplyChangesException)
    }


    def "Should resolve suggestion in draft article"() {
        given: "Applied suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.APPLIED)

        and: "Article content"
        def articleContent = new ArticleContent(new Text(suggestion.suggestedChanges), new Title("Some title"), new Heading("Some heading"))

        and: "Article with applied suggestions"
        def draftArticle = new DraftArticle(articleId, journalistId, articleContent, copywriterId, emptyTopicList, List.of(suggestion))

        and: "Expected resolved suggestion"
        def resolvedSuggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.RESOLVED)

        and: "Expected results"
        def expectedResult = new DraftArticle(articleId, journalistId, articleContent, copywriterId, emptyTopicList, List.of(resolvedSuggestion))

        when: "Resolving suggestion"
        def result = draftArticle.resolveSuggestion(suggestion.suggestionId)

        then: "Suggested changes are resolved"
        expectedResult == result
    }

    def "Should publish draft article when all suggestions are resolved"() {
        given: "Suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.RESOLVED)

        and: "Article content"
        def articleContent = new ArticleContent(new Text(suggestion.suggestedChanges), new Title("Some title"), new Heading("Some heading"))

        and: "Article with applied suggestions"
        def draftArticle = new DraftArticle(articleId, journalistId, articleContent, copywriterId, emptyTopicList, List.of(suggestion))

        and: "Expected results"
        def expectedResult = new PublishedArticle(articleId, journalistId, articleContent, emptyTopicList)

        when: "Publish article"
        def result = draftArticle.publish()

        then: "Article is published"
        expectedResult == result
    }

    def "Should not publish draft article when not all suggestions are resolved"() {
        given: "Resolved suggestion to article"
        def suggestion = new ArticleSuggestion(suggestionId, "Some suggested changes", SuggestionState.RESOLVED)

        and: "Open suggestion to article"
        def openSuggestion = new ArticleSuggestion(new SuggestionId("1"), "Some suggested changes", SuggestionState.OPEN)

        and: "Article content"
        def articleContent = new ArticleContent(new Text(suggestion.suggestedChanges), new Title("Some title"), new Heading("Some heading"))

        and: "Article with applied suggestions"
        def draftArticle = new DraftArticle(articleId, journalistId, articleContent, copywriterId, emptyTopicList, List.of(suggestion, openSuggestion))

        when: "Publish article"
        def result = draftArticle.publish()

        then: "Exception is thrown"
        thrown(UnableToPublishDraftArticleException)
    }


    private DraftArticle emptyDraftArticle() {
        new DraftArticle(articleId, journalistId, testArticleContent(), copywriterId, emptyTopicList, emptySuggestionList)
    }

    private static ArticleContent testArticleContent() {
        def articleText = new Text("Some article text")
        def articleTitle = new Title("Some title")
        def articleHeading = new Heading("Some heading")

        def articleContent = new ArticleContent(articleText, articleTitle, articleHeading)
        articleContent
    }


}
