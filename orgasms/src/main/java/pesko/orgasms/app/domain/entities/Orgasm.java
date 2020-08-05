package pesko.orgasms.app.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Entity
@Table
@Getter
@Setter
public class Orgasm extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String title;


    @Column(unique = true,name = "video_url")
    private String videoUrl;

    @Column
    private boolean pending;

//    @ToString.Exclude
//    @ManyToMany
//    @JoinTable(name = "users_orgasms",joinColumns = @JoinColumn(name = "orgasm_id",referencedColumnName = "id"),
//            inverseJoinColumns =@JoinColumn(name = "user_id" ,referencedColumnName = "id") )

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    private User user;

    @ElementCollection(fetch=FetchType.EAGER)
   private Map<String,Boolean>likeDislike = new HashMap<>();



}
