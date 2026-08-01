package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.model.OfferLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferLetterRepository extends JpaRepository<OfferLetter, Long> {
    List<OfferLetter> findByOfferStatus(String offerStatus);
}
