package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerRepositoryDBTest {
  SessionFactory sessionFactoryTest;
  Session sessionTest;
  PlayerRepositoryDB playerRepositoryDBTest;
  Transaction transactionTest;
  @BeforeEach
  void init() {
    sessionFactoryTest = mock(SessionFactory.class);
    sessionTest = mock(Session.class);
    transactionTest = mock(Transaction.class);
    playerRepositoryDBTest =  mock(PlayerRepositoryDB.class);

   // when(sessionFactoryTest.openSession()).thenReturn(sessionTest);
    when(sessionTest.beginTransaction()).thenReturn(transactionTest);

  }
    @Test
    void getAll() {
    List<Player> playerList = new ArrayList<>();
    playerList.add(new Player());


      when(sessionFactoryTest.openSession()).thenReturn(sessionTest);
      NativeQuery <Player> queryMock = mock(NativeQuery.class);

      when(queryMock.setParameter("pageSize", 10)).thenReturn(queryMock);
      when(queryMock.setParameter("offSet", 0)).thenReturn(queryMock);
      when(queryMock.list()).thenReturn(playerList);
      when(sessionTest.
              createNativeQuery("SELECT * FROM rpg.player ORDER BY id LIMIT :pageSize OFFSET :offSet", Player.class))
              .thenReturn(queryMock);

      sessionTest = sessionFactoryTest.openSession();
      NativeQuery<Player> nativeQuery = sessionTest
              .createNativeQuery("SELECT * FROM rpg.player ORDER BY id LIMIT :pageSize OFFSET :offSet", Player.class);

      nativeQuery.setParameter("pageSize", 10);
      nativeQuery.setParameter("offSet", 0);
      List<Player> list = queryMock.list();
      sessionTest.close();


      assertEquals(1, list.size());
    }

    @Test
    void getAllCount() {
      Integer counter = 40;
      when(sessionFactoryTest.openSession()).thenReturn(sessionTest);
      Query<Integer> queryMock = mock(Query.class);
      when(sessionTest.createQuery("SELECT distinct COUNT(*) FROM Player", Integer.class)).thenReturn(queryMock);
      when(queryMock.getFirstResult()).thenReturn(counter);

      sessionTest = sessionFactoryTest.openSession();
      queryMock = sessionTest.createQuery("SELECT distinct COUNT(*) FROM Player", Integer.class);
      int firstResult = queryMock.getFirstResult();


      assertEquals(counter, firstResult);


    }

    @Test
    void save() {
    Player player = new Player();
      when(sessionFactoryTest.openSession()).thenReturn(sessionTest);

      sessionTest = sessionFactoryTest.openSession();
      transactionTest = sessionTest.beginTransaction();
      sessionTest.save(player);
      transactionTest.commit();
      sessionTest.close();

    }

   @Test
    void update() {
        Player player = new Player();
        when(sessionFactoryTest.openSession()).thenReturn(sessionTest);
        sessionTest = sessionFactoryTest.openSession();
        transactionTest.begin();
        sessionTest.update(player);
        transactionTest.commit();
        sessionTest.close();

    }

   @ParameterizedTest
    @ValueSource(longs = 41)
    void findById(Long arg) {

      Player player = new Player();

     when(sessionFactoryTest.openSession()).thenReturn(sessionTest);
     when(sessionTest.get(Player.class, arg)).thenReturn(player);


     sessionTest = sessionFactoryTest.openSession();
     if(sessionTest.get(Player.class, arg) == null)
     assertEquals(player, sessionTest.get(Player.class, arg));
     if(sessionTest.get(Player.class, arg) == null){
         assertEquals(null, sessionTest.get(Player.class, arg));
     }else{
         assertEquals(player, sessionTest.get(Player.class, arg));
     }
     sessionTest.close();

    }

    @Test
    void delete() {
      Player player = new Player();
      when(sessionFactoryTest.openSession()).thenReturn(sessionTest);

      sessionTest = sessionFactoryTest.openSession();
      transactionTest = sessionTest.beginTransaction();
      sessionTest.delete(player);
      sessionTest.close();
      transactionTest.commit();
    }

    @Test
    void beforeStop() {

        if (sessionFactoryTest != null) {
            sessionFactoryTest.close();
        }
    }
}