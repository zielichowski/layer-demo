package com.zielichowski.layer.magazine.publishing.article.infrastructure.persistence

import com.zielichowski.layer.magazine.publishing.article.api.ArticleContent
import com.zielichowski.layer.magazine.publishing.article.api.ArticleState
import com.zielichowski.layer.magazine.publishing.article.model.ArticleSuggestion


/*
  Usually we use here hibernate with annotations like @Entity, @Id, @OneToMany...
  This is simplified version without any framework.
 */
data class ArticleEntity(
    var articleId: String,
    var assignedJournalist: String,
    var articleContent: ArticleContent,
    var assignedCopywriter: String?,
    var assignedTopics: List<String>,
    var suggestedChanges: List<ArticleSuggestion>?,
    var articleState: ArticleState
)

