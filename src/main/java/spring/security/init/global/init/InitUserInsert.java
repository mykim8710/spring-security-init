package spring.security.init.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spring.security.init.entity.User;
import spring.security.init.entity.UserType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class InitUserInsert {
    private final InitUserService initUserService;

    @PostConstruct
    public void init() {
        initUserService.init();
    }

    @Component
    static class InitUserService {
        @PersistenceContext
        EntityManager em;

        @Autowired
        PasswordEncoder pe;

        @Transactional
        public void init() {
            for (int i = 1; i <= 10; i++) {
                String username = i % 2 == 0 ? "admin" : "user";
                UserType type = i % 2 == 0 ? UserType.ADMIN : UserType.USER;

                User user = User.builder()
                                    .username(username +i)
                                    .password(pe.encode("1"))
                                    .type(type)
                                    .build();

                em.persist(user);
            }

        }
    }
}
