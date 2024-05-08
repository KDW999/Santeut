package com.santeut.party.service;

import com.santeut.party.common.exception.AccessDeniedException;
import com.santeut.party.common.exception.AlreadyJoinedException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import com.santeut.party.feign.UserInfoAccessUtil;
import com.santeut.party.repository.PartyRepository;
import com.santeut.party.repository.PartyUserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyUserServiceImpl implements PartyUserService {

  private final PartyRepository partyRepository;
  private final PartyUserRepository partyUserRepository;
  private final UserInfoAccessUtil userInfoAccessUtil;

  @Override
  @Transactional
  public void joinUserParty(int userId, int partyId) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다"));
    if(partyUserRepository.existsByUserIdAndPartyId(userId, partyId)) {
      throw new AlreadyJoinedException("이미 가입한 소모임입니다");
    }
    if(party.addParticipant()) {
      partyUserRepository.save(PartyUser.of(userId, partyId, party.getMountainId()));
    } else {
      throw new AccessDeniedException("소모임 인원이 이미 모두 찼습니다");
    }
  }

  @Override
  @Transactional
  public void deleteAllPartyUser(int partyId, char status) {
    partyUserRepository.findAllByPartyId(partyId)
        .forEach(partyUser -> partyUser.setStatus(status));
  }

  @Override
  @Transactional
  public void withdrawUserFromParty(int userId, Integer partyId) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다"));
    PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(party.getPartyId(), userId)
        .orElseThrow(() -> new AccessDeniedException("해당 소모임에 가입하지 않았습니다"));
    if (party.getUserId() == userId) {
      // 소모임장이 탈퇴할 경우 다른 회원들도 자동으로 소모임에서 나감
      party.deleteParty('I');
      deleteAllPartyUser(partyId, 'I');
    } else {
      // 소모임 나감
      partyUserRepository.delete(partyUser);
      party.minusParticipant();
    }
  }

  @Override
  public Page<PartyInfoResponseDto> findMyParty(int userId, boolean includeEnd, LocalDate date,
      Pageable pageable) {
    Page<PartyWithPartyUserIdResponse> myParties = partyRepository.findMyPartyWithSearchCondition(
        includeEnd, date, userId, pageable);
    return myParties.map(p -> {
      String owner = userInfoAccessUtil.getUserInfo(p.getParty().getUserId()).getUserNickname();
      return PartyInfoResponseDto.of(owner, p.getPartyUserId(), p.getParty(), true);
    });
  }
}
