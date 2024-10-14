package qreol.project.userservice.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {

    private Long id;
    private String name;
    private String description;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
