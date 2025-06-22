package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

//import javax.annotation.PreDestroy;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {


    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {



        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
        init();


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
            Query<Long> getAllFromPlayer = session.createNamedQuery("getAllFromPlayer", Long.class);

            return getAllFromPlayer.uniqueResult().intValue();
        }

    }

    @Override
    public Player save(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return player;
    }

    @Override
    public Player update(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
        return player;
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
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

    private void init() {
        String initSQL = "INSERT INTO rpg.player (name, title, race, profession, birthday, level, banned)\n" +
                "VALUES ('Ниус', 'Приходящий Без Шума', 6, 1, '2009-06-09 00:44:40.000000', 33, false),\n" +
                "       ('Никрашш', 'НайтВульф', 4, 0, '2006-07-09 08:50:40.000000', 58, false),\n" +
                "       ('Эззэссэль', 'шипящая', 1, 3, '2009-05-25 00:43:20.000000', 3, true),\n" +
                "       ('Бэлан', 'Тсе Раа', 1, 1, '2009-05-03 22:20:40.000000', 29, true),\n" +
                "       ('Элеонора', 'Бабушка', 0, 2, '2008-06-22 20:16:40.000000', 35, true),\n" +
                "       ('Эман', 'Ухастый Летун', 2, 2, '2008-06-29 23:46:00.000000', 56, false),\n" +
                "       ('Талан', 'Рожденный в Бронксе', 3, 1, '2008-08-04 15:35:00.000000', 36, false),\n" +
                "       ('Арилан', 'Благотворитель', 2, 2, '2008-07-14 10:53:20.000000', 34, false),\n" +
                "       ('Деракт', 'Эльфёнок Красное Ухо', 2, 1, '2008-07-03 03:08:40.000000', 55, false),\n" +
                "       ('Архилл', 'Смертоносный', 3, 4, '2009-05-24 07:56:40.000000', 38, false),\n" +
                "       ('Эндарион', 'Маленький эльфенок', 2, 7, '2008-08-24 23:52:00.000000', 45, false),\n" +
                "       ('Фаэрвин', 'Темный Идеолог', 0, 5, '2008-07-25 09:28:00.000000', 12, false),\n" +
                "       ('Харидин', 'Бедуин', 5, 0, '2008-05-22 14:10:00.000000', 47, false),\n" +
                "       ('Джур', 'БоРец с жАжДой', 4, 7, '2008-05-27 06:58:00.000000', 23, false),\n" +
                "       ('Грон', 'оин обреченный на бой', 3, 4, '2008-08-10 23:46:00.000000', 58, false),\n" +
                "       ('Морвиел', 'Копье Калимы', 2, 3, '2009-06-11 08:02:40.000000', 31, false),\n" +
                "       ('Ннуфис', 'ДиамантоваЯ', 0, 1, '2008-06-27 11:46:00.000000', 56, false),\n" +
                "       ('Ырх', 'Троль гнет ель', 5, 0, '2009-06-11 05:38:40.000000', 51, true),\n" +
                "       ('Блэйк', 'Серый Воин', 0, 1, '2009-05-17 01:26:40.000000', 54, false),\n" +
                "       ('Нэсс', 'Бусинка', 5, 0, '2008-07-14 10:53:20.000000', 35, true),\n" +
                "       ('Ферин', 'Воитель', 5, 0, '2008-07-20 22:26:40.000000', 48, false),\n" +
                "       ('Солках', 'Ученик Магии', 2, 2, '2008-05-27 02:10:00.000000', 54, false),\n" +
                "       ('Сцинк', 'Титан Войны', 3, 0, '2008-07-17 20:29:20.000000', 41, true),\n" +
                "       ('Айша', 'Искусительница', 0, 3, '2008-08-03 14:10:00.000000', 45, false),\n" +
                "       ('Тант', 'Черт закAтай вату', 1, 4, '2008-06-29 15:06:40.000000', 25, false),\n" +
                "       ('Трениган', 'Великий Волшебник', 2, 2, '2008-07-14 10:20:00.000000', 42, false),\n" +
                "       ('Вуджер', 'Печальный', 5, 5, '2008-05-07 15:35:20.000000', 42, false),\n" +
                "       ('Камираж', 'БAнкир', 1, 3, '2008-07-19 10:53:20.000000', 39, true),\n" +
                "       ('Ларкин', 'СвЯтой', 6, 3, '2008-08-26 11:52:00.000000', 46, false),\n" +
                "       ('Зандир', 'Темновидец', 2, 0, '2008-06-30 21:22:00.000000', 23, false),\n" +
                "       ('Балгор', 'пещерный Урук', 4, 5, '2008-05-24 05:59:20.000000', 18, false),\n" +
                "       ('Регарн', 'юбитель ОЛивье', 3, 0, '2007-12-15 06:24:40.000000', 53, false),\n" +
                "       ('Анжелли', 'Молодой Боец', 1, 0, '2009-07-18 07:06:40.000000', 33, false),\n" +
                "       ('Джерис', 'Имперский Воин', 4, 0, '2008-07-21 11:52:00.000000', 58, false),\n" +
                "       ('Жэкс', 'Ярочкино Солнышко', 3, 0, '2008-07-01 05:32:40.000000', 3, false),\n" +
                "       ('Филуэль', 'Химик и Карпускулярник.', 2, 0, '2009-06-08 10:20:40.000000', 30, false),\n" +
                "       ('Яра', 'Прельстивая', 0, 3, '2009-05-03 03:08:20.000000', 52, false),\n" +
                "       ('Иллинас', 'Иероглиф', 6, 0, '2009-04-27 15:08:40.000000', 47, false),\n" +
                "       ('Ардонг', 'Вспышк A', 0, 0, '2008-07-31 23:46:00.000000', 21, false),\n" +
                "       ('Аттирис', 'и.о.Карвандоса', 2, 2, '2009-06-15 10:26:40.000000', 34, true)\n" +
                ";";
        if (getAllCount() == 0) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                NativeQuery nativeQuery = session.createNativeQuery(initSQL);
                nativeQuery.executeUpdate();
                transaction.commit();

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

}