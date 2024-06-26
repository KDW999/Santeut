package com.santeut.party.repository;

import com.santeut.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer>, PartyRepositoryCustom {

    Optional<Party> findByPartyId(int partyId);

}
