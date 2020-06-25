package pesko.orgasms.app.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter

public class Orgasm extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String title;

    @Column
    private String content;

    @Column(unique = true ,name ="img_url")
    private String imgUrl;

    @Column(unique = true,name = "video_url")
    private String videoUrl;

    @ToString.Exclude
    @ManyToMany(mappedBy = "orgasms",fetch = FetchType.EAGER)
    private List<User> users;



}
