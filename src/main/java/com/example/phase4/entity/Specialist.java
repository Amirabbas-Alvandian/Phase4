package com.example.phase4.entity;


import com.example.phase4.entity.enums.Role;
import com.example.phase4.entity.enums.SpecialistStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Specialist extends User{

/*    @Id
    @SequenceGenerator(
            name = "specialist_sequence",
            sequenceName = "specialist_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "specialist_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;*/

    @Enumerated(EnumType.STRING)
    private SpecialistStatus status;

/*    @OneToOne
    //@JoinColumn(name = "user_id",referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User user;*/

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @OneToMany(mappedBy = "specialist")
    private List<Offer> offers;

    //@NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "specialist_sub_Categories",uniqueConstraints = {@UniqueConstraint(columnNames = {"specialists_id", "sub_category_list_id"})})
    private List<SubCategory> subCategoryList;

    private double score;


    public Specialist(String firstName, String lastName, String email, String password, byte[] image, List<SubCategory> subCategory) {
        super(firstName, lastName, email, password, Role.ROLE_SPECIALIST);
        this.status = SpecialistStatus.NEW;
        this.image = image;
        this.subCategoryList = subCategory;
        this.score = 0;
    }

    public Specialist(String firstName,
                    String LastName,
                    String email,
                    String password) {

        super(firstName, LastName, email, password , Role.ROLE_SPECIALIST);
    }


    @Override
    public String toString() {
        return "Specialist{" +
                "status=" + status +
                ", score=" + score +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", signUpDate=" + signUpDate +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Specialist that = (Specialist) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
