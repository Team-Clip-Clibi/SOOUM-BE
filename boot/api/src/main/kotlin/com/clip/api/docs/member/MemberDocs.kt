package com.clip.api.docs.member

import com.clip.api.member.controller.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Member", description = "Member API")
interface MemberDocs {

    @Operation(summary = "FCM Token Update API", description = """
        - FCM Update API
        - FCM 토큰이 만료되면 새로운 FCM 토큰으로 업데이트합니다.
    """)
    fun updateFCMToken(fcmRequest: FCMRequest, userId: Long): ResponseEntity<Void>

    @Operation(summary = "가입 가능 여부 확인 API", description = """
        - 가입 가능 여부 확인 API
        - 정지된 계정인지, 탈퇴한 계정인지, 이미 가입된 이메일인지 확인합니다.
        - 정지된 계정, 탈퇴한 계정인 경우 언제 다시 가입 가능한지에 대한 정보도 함께 제공합니다.
    """)
    fun checkAvailableSignUp(checkAvailableRequest: CheckAvailableRequest): ResponseEntity<CheckAvailableResponse>

    @Operation(summary = "닉네임 유효성 검사 API", description = """
        - 닉네임 유효성 검사 API
        - 닉네임이 유효한지 검사합니다.
        - 닉네임은 공백이 아니어야 하며, 욕설 필터링을 통과해야 하고, 금지된 닉네임 목록에 포함되지 않아야 합니다.
    """)
    fun validateNickname(nicknameDto: NicknameDto): ResponseEntity<NicknameValidateResponse>

    @Operation(summary = "닉네임 업데이트 API", description = """
        - 닉네임 업데이트 API
        - 사용자가 닉네임을 변경할 수 있도록 합니다.
    """)
    fun updateNickname(nicknameDto: NicknameDto, id: Long): ResponseEntity<Void>

    @Operation(summary = "프로필 이미지 업데이트 API", description = """
        - 프로필 이미지 업데이트 API
        - 사용자가 프로필 이미지를 변경할 수 있도록 합니다.
    """)
    fun updateProfileImage(profileImageDto: ProfileImageDto, id: Long): ResponseEntity<Void>

    @Operation(summary = "닉네임 생성 API", description = """
        - 닉네임 생성 API
        - 사용자가 랜덤으로 생성된 닉네임을 받을 수 있도록 합니다.
    """)
    fun generateNickname(): ResponseEntity<NicknameDto>

    @Operation(summary = "글 작성 차단 여부 조회 API", description = """
        - 차단되지 않은 유저의 expiredAt은 null 입니다.
    """)
    fun getPostingPermissions(userId: Long): ResponseEntity<PostingPermissionDto>

    @Operation(summary = "나의 프로필 정보 조회 API", description = """
        - 나의 프로필 정보(방문자, 게시글 수, 팔로워 수, 팔로잉 수, 닉네임, 프로필 이미지 등) 조회 API
    """)
    fun getMyProfileSummaryInfo(userId: Long): ResponseEntity<MyProfileInfoResponse>

    @Operation(summary = "상대 프로필 정보 조회 API", description = """
        - profileOwner의 프로필 정보(방문자, 게시글 수, 팔로워 수, 팔로잉 수, 닉네임, 프로필 이미지 등) 조회 API
    """)
    fun getUserProfileSummaryInfo(profileOwnerId: Long, userId: Long): ResponseEntity<UserProfileInfoResponse>

    @Operation(summary = "프로필 업데이트 API", description = """
        - 프로필 화면에서의 프로필 업데이트 시 사용되는 API입니다.
        - 닉네임과 프로필 이미지를 업데이트 할 수 있습니다.
        - 닉네임은 null일 경우 변경하지 않는 것으로 간주합니다.
        - 프로필 이미지는 null일 경우 기존 이미지는 삭제되며 기본 이미지로 변경됩니다.
        - 닉네임만 변경하고 프로필 이미지는 기존과 동일한 이미지 사용을 원하면 이전 화면(프로필 화면)을 조회할 때 사용되는 API의 응답에서 profileImgName 값을 가져와서 요청 바디에 담아 보내면 됩니다.
    """)
    fun updateMyProfileInfo(profileInfoRequest: ProfileInfoRequest, userId: Long): ResponseEntity<Void>
}