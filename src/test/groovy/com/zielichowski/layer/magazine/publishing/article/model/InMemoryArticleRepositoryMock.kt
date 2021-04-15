package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticleDto
import com.zielichowski.layer.magazine.publishing.article.api.ArticleId
import com.zielichowski.layer.magazine.publishing.article.infrastructure.persistence.ArticleEntity
import com.zielichowski.layer.magazine.publishing.common.CopywriterId
import com.zielichowski.layer.magazine.publishing.common.JournalistId
import com.zielichowski.layer.magazine.publishing.common.TopicId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

// It's coincidence that this class in the same as the one in persistence layer
// Sometimes it's faster to write your own mock than use framework
internal class InMemoryArticleRepositoryMock : ArticleRepository {
    private val db: ConcurrentMap<String, ArticleEntity> = ConcurrentHashMap()

    override fun save(articleDto: ArticleDto) {
        val articleEntity =
            ArticleEntity(
                articleDto.articleId.id,
                articleDto.assignedJournalist.id,
                articleDto.articleContent,
                articleDto.assignedCopywriter?.id,
                articleDto.assignedTopics.map { it.id },
                articleDto.suggestedChanges,
                articleDto.articleState
            )
        db[articleDto.articleId.id] = articleEntity
    }

    override fun findById(articleId: ArticleId): ArticleDto {
        val articleEntity = db.getValue(articleId.id)
        return ArticleDto(
            ArticleId(articleEntity.articleId),
            JournalistId(articleEntity.assignedJournalist),
            articleEntity.articleContent,
            articleEntity.assignedCopywriter?.let { CopywriterId(it) },
            articleEntity.assignedTopics.map { TopicId(it) },
            articleEntity.suggestedChanges,
            articleEntity.articleState
        )
    }
}