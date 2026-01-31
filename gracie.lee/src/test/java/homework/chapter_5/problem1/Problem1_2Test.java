package homework.chapter_5.problem1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 문제 1-2: FK 업데이트 확인
 *
 * 목표: 연관관계 변경이 DB에 반영되는지 확인
 */
class Problem1_2Test {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx.isActive()) tx.rollback();
        if (em != null) em.close();
    }

    @Test
    @DisplayName("문제 1-2: 팀을 변경하면 FK가 업데이트되는지 확인")
    void FK_업데이트_확인() {

        // 1. Team "개발팀", "기획팀" 저장
        Team devTeam = new Team("개발팀");
        Team planTeam = new Team("기획팀");
        em.persist(devTeam);
        em.persist(planTeam);
        System.out.println("1. Team '개발팀', '기획팀' 저장 완료");

        // 2. Member "홍길동"을 "개발팀"에 소속시킴
        Member member = new Member("홍길동");
        member.setTeam(devTeam);
        em.persist(member);
        System.out.println("2. Member '홍길동' → '개발팀' 소속");

        // 3. flush/clear
        em.flush();
        em.clear();
        System.out.println("3. flush/clear 완료");

        // 4. Member를 다시 조회해서 member.setTeam(기획팀) 호출
        Member foundMember = em.find(Member.class, member.getId());
        Team foundPlanTeam = em.find(Team.class, planTeam.getId());
        System.out.println("4. 변경 전 팀: " + foundMember.getTeam().getName());

        foundMember.setTeam(foundPlanTeam);  // 팀 변경!
        System.out.println("   member.setTeam(기획팀) 호출");

        // 5. flush/clear 후 Member를 다시 조회
        // - flush(): 쓰기 지연 SQL을 DB에 반영
        // - clear(): 1차 캐시를 비움 → 이후 조회 시 DB에서 새로 SELECT
        // - clear()를 안 하면 1차 캐시에서 가져오므로 실제 DB 반영 여부를 확인할 수 없음
        em.flush();
        em.clear();
        System.out.println("5. flush/clear 완료");

        // 6. member.getTeam()이 "기획팀"으로 변경됐는지 확인
        Member reloadedMember = em.find(Member.class, member.getId());
        System.out.println("6. 변경 후 팀: " + reloadedMember.getTeam().getName());
        assertEquals("기획팀", reloadedMember.getTeam().getName());
    }
}
