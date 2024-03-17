package kr.co.ref2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.ref2.dto.UserDTO;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    private String uid;
    private String pass;
    private String name;
    private String birth;
    private int age;
    private String addr;
    private String role;

    // toDTO
    public UserDTO toDTO(){
        return UserDTO.builder()
                .uid(uid).pass(pass).name(name).birth(birth)
                .age(age).addr(addr).role(role).build();
    }
}
