package com.clip.api.card.service.aspect

import com.clip.data.abtest.service.TempAbHomeAdminCardUserGroupService
import com.clip.data.card.service.FeedCardService
import com.clip.data.member.entity.Role
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

//AB_TEST HomeAdminCardUserTest
@Component
@Aspect
class CardDetailAspect(
    private val tempAbHomeAdminCardUserGroupService: TempAbHomeAdminCardUserGroupService,
    private val feedCardService: FeedCardService,
) {

    @Around("execution(* com.clip.api.card.service.CardUseCase.getFeedCardDetail(..))")
    fun abHomeAdminCardUserGroupAround(pjp: ProceedingJoinPoint): Any {
        val args = pjp.args  // (latitude, longitude, cardId, userId)
        val cardId = args[2] as Long
        val userId = args[3] as Long

        val shouldIncreaseClickCount =
            feedCardService.isExistFeedCard(cardId) &&
                    feedCardService.findFeedCard(cardId).writer.role == Role.ADMIN &&
                    tempAbHomeAdminCardUserGroupService.findTempAbHomeAdminCardUserGroup(userId).isPresent

        if (shouldIncreaseClickCount) {
            tempAbHomeAdminCardUserGroupService.increaseClickCount(userId)
        }

        return pjp.proceed()
    }
}