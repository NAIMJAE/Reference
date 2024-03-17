package kr.co.ref2.security;

import kr.co.ref2.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Slf4j
@Getter
@Setter
@ToString
@Builder
public class MyUserDetails implements UserDetails {
    // User Entity
    private User user;

    // getAuthorities -> 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities 시작");
        // 계정이 갖는 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        log.info("getAuthorities authorities : " + authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부(true : 만료 안됨 / false : 만료)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠김 여부 (true : 잠김 안됨 / false : 잠김)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부 (true : 만료 안됨 / false : 만료)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 (true : 활성화 / false : 비활성화)
        return true;
    }
}
