package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.article.api.ArticleDto
import com.zielichowski.layer.magazine.publishing.article.api.ArticleId

interface ArticleRepository {
    fun save(articleDto: ArticleDto)
    fun findById(articleId: ArticleId) : ArticleDto
}