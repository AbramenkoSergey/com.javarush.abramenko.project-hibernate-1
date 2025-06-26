package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PlayerRepositoryDB(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Player> nativeQuery = session.createNativeQuery(
                    "SELECT * FROM rpg.player ORDER BY id LIMIT :pageSize OFFSET :ofSet",
                    Player.class);
            nativeQuery.setParameter("pageSize", pageSize);
            nativeQuery.setParameter("ofSet", pageSize * pageNumber);
            return nativeQuery.getResultList();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> findAll = session.createNamedQuery("getAllFromPlayer", Long.class);
            return findAll.uniqueResult().intValue();
        }
    }


    @Override
    public Player save(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player update(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Player player = session.get(Player.class, id);
            return Optional.of(player);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void beforeStop() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}