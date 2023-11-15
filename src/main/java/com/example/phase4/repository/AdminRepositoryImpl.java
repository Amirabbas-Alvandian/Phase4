package com.example.phase4.repository;

import com.example.phase4.dto.request.FilteredUserRequestDTO;
import com.example.phase4.dto.request.FilteredUsers2;
import com.example.phase4.dto.request.FilteredOrdersRequestDTO;
import com.example.phase4.entity.*;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.enums.Role;
import com.example.phase4.entity.enums.SpecialistScoreMinMaxNull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepositoryImpl {

    private final EntityManager entityManager;
    private final SpecialistRepository specialistRepository;

    public AdminRepositoryImpl(EntityManager entityManager,
                               SpecialistRepository specialistRepository) {
        this.entityManager = entityManager;
        this.specialistRepository = specialistRepository;
    }

    @Transactional
    public List<User> filteredUser(FilteredUserRequestDTO requestDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();


        if (requestDTO.role() == Role.ROLE_CUSTOMER) {
            CriteriaQuery<User> customerQuery = criteriaBuilder.createQuery(User.class);
            Root<User> customerRoot = customerQuery.from(User.class);
            predicates.add(criteriaBuilder.equal(customerRoot.get("role"), requestDTO.role()));
            if (requestDTO.firstName() != null)
                predicates.add(criteriaBuilder.equal(customerRoot.get("firstName"), requestDTO.firstName()));
            if (requestDTO.lastName() != null)
                predicates.add(criteriaBuilder.equal(customerRoot.get("lastName"), requestDTO.lastName()));
            if (requestDTO.email() != null)
                predicates.add(criteriaBuilder.equal(customerRoot.get("email"), requestDTO.email()));

            customerQuery.select(customerRoot).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            return entityManager.createQuery(customerQuery).getResultList();
        }

        CriteriaQuery<Specialist> SpecialistQuery = criteriaBuilder.createQuery(Specialist.class);
        Root<Specialist> SpecialistRoot = SpecialistQuery.from(Specialist.class);


        Subquery<Integer> subquery = SpecialistQuery.subquery(Integer.class);
        Root<Specialist> subRoot = subquery.from(Specialist.class);

        List<Predicate> subPredicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(SpecialistRoot.get("role"), requestDTO.role()));


        subPredicates.add(criteriaBuilder.equal(subRoot.get("role"), Role.ROLE_SPECIALIST));
        if (requestDTO.firstName() != null) {
            predicates.add(criteriaBuilder.equal(SpecialistRoot.get("firstName"), requestDTO.firstName()));
            subPredicates.add(criteriaBuilder.equal(subRoot.get("firstName"), requestDTO.firstName()));
        }
        if (requestDTO.lastName() != null) {
            predicates.add(criteriaBuilder.equal(SpecialistRoot.get("lastName"), requestDTO.lastName()));
            subPredicates.add(criteriaBuilder.equal(subRoot.get("lastName"), requestDTO.lastName()));
        }
        if (requestDTO.email() != null) {
            predicates.add(criteriaBuilder.equal(SpecialistRoot.get("email"), requestDTO.email()));
            subPredicates.add(criteriaBuilder.equal(subRoot.get("email"), requestDTO.email()));
        }

        if (requestDTO.subCategoryId() != 0) {
            Join<Specialist, SubCategory> specialistSubCategoryJoin = SpecialistRoot.join("subCategoryList");
            Join<Specialist, SubCategory> subCategorySpecialistJoin = subRoot.join("subCategoryList");
            predicates.add(criteriaBuilder.equal(specialistSubCategoryJoin.get("id"), requestDTO.subCategoryId()));
            subPredicates.add(criteriaBuilder.equal(subCategorySpecialistJoin.get("id"), requestDTO.subCategoryId()));
        }


        if (requestDTO.specialistScore() == SpecialistScoreMinMaxNull.MAX) {

            System.out.println("MAX");


            subquery.select(criteriaBuilder.max(subRoot.get("score"))).where(
                    criteriaBuilder.and(subPredicates.toArray(Predicate[]::new)));


            predicates.add(criteriaBuilder.and(SpecialistRoot.get("score").in(subquery)));


        } else if (requestDTO.specialistScore() == SpecialistScoreMinMaxNull.MIN) {

            System.out.println("MIN");

            subquery.select(criteriaBuilder.min(subRoot.get("score"))).where(
                    criteriaBuilder.and(subPredicates.toArray(Predicate[]::new)));


            predicates.add(criteriaBuilder.and(SpecialistRoot.get("score").in(subquery)));


        }


        SpecialistQuery.select(SpecialistRoot).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        List<Specialist> resultList = entityManager.createQuery(SpecialistQuery).getResultList();

        System.out.println(resultList);

        return resultList.stream()
                .map(s -> new User(s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getSignUpDate(), s.getRole())).toList();


    }

    @Transactional
    public List<Order> filteredOrders(FilteredOrdersRequestDTO requestDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Order> orderQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderQuery.from(Order.class);
        if (requestDTO.startDate() != null)
            predicates.add(criteriaBuilder.between(orderRoot.get("orderDate")
                    , requestDTO.startDate(), requestDTO.startDate().plusDays(requestDTO.offset())));

        if (requestDTO.subCategoryId() != 0) {
            Join<Order, SubCategory> orderSubCategoryJoin = orderRoot.join("subCategory");
            predicates.add(criteriaBuilder.equal(orderSubCategoryJoin.get("id"), requestDTO.subCategoryId()));
        }

        if (requestDTO.categoryId() != 0) {
            Join<Order, SubCategory> orderSubCategoryJoin = orderRoot.join("subCategory");
            predicates.add(criteriaBuilder.equal(orderSubCategoryJoin.get("category_id"), requestDTO.categoryId()));
        }

        if (requestDTO.orderStatus() != null)
            predicates.add(criteriaBuilder.equal(orderRoot.get("orderStatus"), requestDTO.orderStatus()));


        orderQuery.select(orderRoot).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(orderQuery).getResultList();
    }


    @Transactional
    public List<User> filteredUsers2(FilteredUsers2 requestDTO) {

        if (requestDTO.role() == Role.ROLE_ADMIN)
            throw new IllegalArgumentException("admin is not allowed here");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> subPredicates = new ArrayList<>();


        if (requestDTO.role() == Role.ROLE_CUSTOMER) {
            CriteriaQuery<Customer> customerQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> customerRoot = customerQuery.from(Customer.class);
            predicates.add(criteriaBuilder.equal(customerRoot.get("role"), requestDTO.role()));

            if (requestDTO.signUpDate() != null) {
                predicates.add(criteriaBuilder.equal(customerRoot.get("signUpDate"), requestDTO.signUpDate()));
            }

            if (requestDTO.count() != null) {
                Subquery<Long> subQuery = customerQuery.subquery(Long.class);
                Root<Customer> subRoot = subQuery.from(Customer.class);

                if (requestDTO.signUpDate() != null)
                    subPredicates.add(criteriaBuilder.equal(subRoot.get("signUpDate"), requestDTO.signUpDate()));

                Join<Customer, Order> customerOrderJoin = subRoot.join("orders");

                subQuery.select(customerOrderJoin.get("customer"))
                        .groupBy(customerOrderJoin.get("customer"))
                        .having(criteriaBuilder.equal(
                        criteriaBuilder.count(customerOrderJoin.get("customer")),requestDTO.count())) ;

                predicates.add(criteriaBuilder.and(customerRoot.get("id").in(subQuery)));
            }

            customerQuery.select(customerRoot).where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));

            return entityManager.createQuery(customerQuery).getResultList().stream()
                    .map(s -> new User(s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getSignUpDate(), s.getRole())).toList();
        }

        CriteriaQuery<Specialist> specialistQuery = criteriaBuilder.createQuery(Specialist.class);
        Root<Specialist> specialistRoot = specialistQuery.from(Specialist.class);
        predicates.add(criteriaBuilder.equal(specialistRoot.get("role"), requestDTO.role()));

        if (requestDTO.signUpDate() != null) {
            predicates.add(criteriaBuilder.equal(specialistRoot.get("signUpDate"), requestDTO.signUpDate()));
        }

        if (requestDTO.count() != null) {
            Subquery<Long> subQuery = specialistQuery.subquery(Long.class);
            Root<Specialist> subRoot = subQuery.from(Specialist.class);

            if (requestDTO.signUpDate() != null)
                subPredicates.add(criteriaBuilder.equal(subRoot.get("signUpDate"), requestDTO.signUpDate()));

            Join<Specialist,Offer> specialistOfferJoinCount = subRoot.join("offers");
            subPredicates.add(criteriaBuilder.equal(specialistOfferJoinCount.get("isAccepted"),true));
            subQuery.select(specialistOfferJoinCount.get("specialist")).where(criteriaBuilder.and(subPredicates.toArray(Predicate[]::new)))
                    .groupBy(specialistOfferJoinCount.get("specialist"))
                    .having(criteriaBuilder.equal(criteriaBuilder.count(specialistOfferJoinCount.get("specialist")),requestDTO.count())) ;

            predicates.add(criteriaBuilder.and(specialistRoot.get("id").in(subQuery)));
            //Join<Specialist, Offer> specialistOfferJoin = specialistRoot.join("offers");
            //predicates.add(criteriaBuilder.equal(specialistOfferJoin.get("isAccepted"),true));
            //predicates.add(criteriaBuilder.equal(criteriaBuilder.count(specialistOfferJoin.get("id")), requestDTO.count()));
        }



        specialistQuery.select(specialistRoot).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(specialistQuery).getResultList().stream()
                .map(s -> new User(s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getSignUpDate(), s.getRole())).toList();
    }


}
