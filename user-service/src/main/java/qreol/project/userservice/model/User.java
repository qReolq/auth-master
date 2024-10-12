package qreol.project.userservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import qreol.project.userservice.web.validation.flags.OnCreate;
import qreol.project.userservice.web.validation.flags.OnUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username must be not empty", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @Email(message = "Email must be correct", groups = {OnUpdate.class, OnCreate.class})
    @NotEmpty(message = "Email must be not empty", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Email length must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String email;

    @NotEmpty(message = "Password must be not empty", groups = {OnUpdate.class, OnCreate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "create_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

}
