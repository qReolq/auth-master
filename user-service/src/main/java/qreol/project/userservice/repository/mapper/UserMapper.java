package qreol.project.userservice.repository.mapper;

import org.springframework.stereotype.Component;
import qreol.project.userservice.model.User;
import qreol.project.userservice.web.dto.UserResponseDto;

@Component
public class UserMapper {

    public UserResponseDto toDto(User entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
