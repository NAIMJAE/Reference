package kr.co.ref2.dto;

import kr.co.ref2.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String uid;
    private String pass;
    private String name;
    private String birth;
    private int age;
    private String addr;
    private String role;

    // toEntity
    public User toEntity(){
         return User.builder()
                 .uid(uid).pass(pass).name(name).birth(birth)
                 .age(age).addr(addr).role(role).build();
    }
}
