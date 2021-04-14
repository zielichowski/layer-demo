package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticleContent
import com.zielichowski.layer.magazine.publishing.article.api.ArticleId
import com.zielichowski.layer.magazine.publishing.article.api.PublishedArticleException
import com.zielichowski.layer.magazine.publishing.common.CopywriterId
import com.zielichowski.layer.magazine.publishing.common.Heading
import com.zielichowski.layer.magazine.publishing.common.JournalistId
import com.zielichowski.layer.magazine.publishing.common.SuggestionId
import com.zielichowski.layer.magazine.publishing.common.Text
import com.zielichowski.layer.magazine.publishing.common.Title
import com.zielichowski.layer.magazine.publishing.common.TopicId
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class PublishedArticleTest extends Specification {

    @Shared
    def journalistId = new JournalistId(UUID.randomUUID().toString())
    @Shared
    def articleId = new ArticleId(UUID.randomUUID().toString())
    @Shared
    def topics = [new TopicId("1")]

    @Subject
    def publishedArticle = new PublishedArticle(articleId, journalistId, testArticleContent(), topics)

    def "Should not allow to add suggestions"() {
        given: "Copywriter"
        def copywriterId = new CopywriterId("1")

        and: "Suggestion to article"
        def suggestion = new ArticleSuggestion(new SuggestionId("1"), "Some suggested changes", SuggestionState.OPEN)

        when: "Suggesting to published article"
        publishedArticle.suggestChanges(copywriterId, suggestion)

        then: "Exception is thrown"
        thrown(PublishedArticleException)
    }

    def "Should not allow submit to topic"() {
        given: "Topic"
        def topicId = new TopicId("1")

        when: "Adding topic to published article"
        publishedArticle.submitToTopic(topicId)

        then: "Exception is thrown"
        thrown(PublishedArticleException)
    }

    def "Should not allow resolve suggestions"() {
        given: "Suggestion"
        def suggestionId = new SuggestionId("1")

        when: "Resolving suggestion"
        publishedArticle.resolveSuggestion(suggestionId)

        then: "Exception is thrown"
        thrown(PublishedArticleException)
    }

    def "Should not allow resolve changes"() {
        given: "Suggestion"
        def suggestionId = new SuggestionId("1")

        and: "Journalist"
        def journalistId = new JournalistId("1")

        when: "Applying changes"
        publishedArticle.applyChanges(suggestionId, journalistId)

        then: "Exception is thrown"
        thrown(PublishedArticleException)
    }

    private static ArticleContent testArticleContent() {
        def articleText = new Text("Some article text")
        def articleTitle = new Title("Some title")
        def articleHeading = new Heading("Some heading")

        def articleContent = new ArticleContent(articleText, articleTitle, articleHeading)
        articleContent
    }
}
