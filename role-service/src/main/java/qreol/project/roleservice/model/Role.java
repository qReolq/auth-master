package qreol.project.roleservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import qreol.project.roleservice.web.validation.flags.OnCreate;
import qreol.project.roleservice.web.validation.flags.OnUpdate;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name must be not empty", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @Length(max = 255, message = "Description length must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String description;

}
