package com.practice.design.pattern.repository;

import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Donation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DonationRepository extends ReactiveMongoRepository<Donation, String> {
}
