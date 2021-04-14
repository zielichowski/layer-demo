package com.zielichowski.layer.magazine.publishing.article.model

import com.zielichowski.layer.magazine.publishing.common.SuggestionId

data class ArticleSuggestion(
    val suggestionId: SuggestionId,
    val suggestedChanges: String,
    val state: SuggestionState = SuggestionState.OPEN
)

enum class SuggestionState{
    OPEN,
    APPLIED,
    RESOLVED
}