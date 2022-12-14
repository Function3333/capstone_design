package capstone_back.repository;

import capstone_back.domain.Board;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardRepository {
    @PersistenceContext
    EntityManager em;


    /*저장*/
    public Long save(Board board) {
        em.persist(board);
        return board.getId();
    }
    /*Id로 찾아오기*/
    public Board findById(Long id) {
        return em.find(Board.class, id);
    }

    /*Board 테이블 모든 데이터 가져오기*/
    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    /*카테고리 Id로 찾아오기*/
    public List<Board> findByTotalCategory(String category_id){
        String strQueryCategory = category_id + "%";

        return em.createQuery("select b from Board b where b.category_id like :strQueryCategory")
                .setParameter("strQueryCategory", strQueryCategory)
                .getResultList();
    }
    public List<Board> findByCategoryId(String category_id) {
        return em.createQuery("select b from Board b where b.category_id =: category_id")
                .setParameter("category_id", category_id)
                .getResultList();
    }
    
    public List<Board> findByUserId(Long user_id) {
        return em.createQuery("select b  from Board b where b.account.id =: user_id")
                .setParameter("user_id", user_id)
                .getResultList();
    }

    /*삭제*/
    public void delete(Long id) {
        Board Board = findById(id);
        em.remove(Board);
    }

}
