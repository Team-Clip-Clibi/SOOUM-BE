package com.clip.api.tag.event

import com.clip.data.tag.entity.Tag

data class TagUsageEvent(
    val writerId: Long,
    val cardId: Long,
    val tags: List<Tag>,
)
