package kr.co.Ref1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.Ref1.dto.UserDTO;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    private String uid;
    private String name;
    private String birth;
    private int age;
    private String addr;

    // DTO 변환 메서드
    public UserDTO toDTO(){
        return UserDTO.builder()
                .uid(uid).name(name).birth(birth).age(age).addr(addr).build();
    }
}