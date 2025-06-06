package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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
    playerRepositoryDBTest =  Mockito.spy(new PlayerRepositoryDB());

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


      assertEquals(1, list.size());
    }

    @Test
    void getAllCount() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void beforeStop() {
    }
}