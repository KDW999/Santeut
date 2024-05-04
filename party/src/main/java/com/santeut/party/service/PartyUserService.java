package com.santeut.party.service;

public interface PartyUserService {

  void joinUserParty(int userId, int partyId);

  void deleteAllPartyUser(int partyId);

  void withdrawUserFromParty(int userId, Integer partyId);

}
