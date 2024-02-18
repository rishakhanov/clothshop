package com.example.clothshop.repository;

import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.PersonDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonDiscountRepository extends JpaRepository<PersonDiscount, Long> {

    @Query(value = "SELECT * FROM person_discount pd WHERE pd.person_id = :personId and pd.discount_id =:discountId",
    nativeQuery = true)
    PersonDiscount findPersonDiscountByPersonAndDiscount(
            @Param("personId") long personId, @Param("discountId") long discountId);


    @Query(value = "SELECT * FROM person_discount pd WHERE pd.person_id = :personId",
            nativeQuery = true)
    List<PersonDiscount> findPersonDiscountsByPersonId(@Param("personId") long personId);


}
