package com.clip.data.block.service;

import com.clip.data.block.entity.Block;
import com.clip.data.block.repository.BlockRepository;
import com.clip.data.member.entity.Member;
import com.clip.data.member.service.MemberService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockMemberService {
    private final BlockRepository blockRepository;
    private final MemberService memberService;

    public void saveBlockMember(Long fromMemberPk, Long toMemberPk) {
        if (fromMemberPk.equals(toMemberPk)) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다.");
        }

        if (blockRepository.existsByFromMemberPkAndToMemberPk(fromMemberPk, toMemberPk)) {
            throw new EntityExistsException("이미 차단한 사용자입니다.");
        }

        Member fromMember = memberService.findMember(fromMemberPk);
        Member toMember = memberService.findMember(toMemberPk);

        blockRepository.save(Block.builder()
                .toMember(toMember)
                .fromMember(fromMember).build());
    }

    public void deleteBlockMember(Long fromMemberPk, Long toMemberId) {
        Member fromMember = memberService.findMember(fromMemberPk);
        Member toMember = memberService.findMember(toMemberId);
        blockRepository.deleteBlockMember(fromMember, toMember);
    }

    public List<Long> findAllBlockMemberPks(Long memberPk) {
        return blockRepository.findAllBlockToPk(memberPk);
    }

    public void deleteAllBlockMember(Long memberPK) {
        blockRepository.deleteAllBlockMember(memberPK);
    }
}
