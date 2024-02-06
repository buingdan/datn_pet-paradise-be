package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;

@Repository
public class RoleCustomRepo {
    //custom lấy role user
    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> getRole(User user) {
        if (entityManager == null) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder()
                .append("SELECT r.name FROM users u ")
                .append("JOIN user_role ur ON u.id = ur.user_id ")
                .append("JOIN roles r ON r.id = ur.role_id WHERE 1=1");

        if (user.getEmail() != null) {
            sql.append(" AND email=:email");
        }

        Session session = (Session) entityManager.getDelegate();

        if (session == null) {
            return Collections.emptyList();
        }

        NativeQuery<Role> query=((Session)entityManager.getDelegate()).createNativeQuery(sql.toString());
        if (user.getEmail() != null) {
            query.setParameter("email", user.getEmail());
        }

        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(Role.class));
        return query.list();
    }
}
