package kr.co.Ref1.dto;

import kr.co.Ref1.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String uid;
    private String name;
    private String birth;
    private int age;
    private String addr;

    // Entity 변환 메서드
    public User toEntity(){
        return User.builder()
                .uid(uid).name(name).birth(birth).age(age).addr(addr).build();
    }
}
