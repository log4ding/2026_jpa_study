package homework.chapter_5.problem1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 문제 1-1: FK 저장 확인
 *
 * 목표: 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장되는지 확인
 */
class Problem1_1Test {

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
    @DisplayName("문제 1-1: member.setTeam(team) 호출 시 FK가 저장되는지 확인")
    void FK_저장_확인() {
        // 1. Team "개발팀" 저장
        Team team = new Team("개발팀");
        em.persist(team);
        System.out.println("1. Team '개발팀' 저장 완료");

          /* insert for
        homework.chapter_5.problem1.Member */
//        insert
//                into
//        CH5_P1_MEMBER (name, TEAM_ID, MEMBER_ID)
//        values
//                (?, ?, default)

        // 2. Member "홍길동" 저장
        Member member = new Member("홍길동");
        em.persist(member);
        System.out.println("2. Member '홍길동' 저장 완료");

        // 3. member.setTeam(team) 호출 - 연관관계 주인이 FK 설정
        member.setTeam(team);
        System.out.println("3. member.setTeam(team) 호출");

        /* update
        for homework.chapter_5.problem1.Member */
//        update CH5_P1_MEMBER
//        set
//        name=?,
//                TEAM_ID=?
//                        where
//        MEMBER_ID=?

        // 4. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();
        System.out.println("4. flush/clear 완료");


        // 5. member.getTeam()이 "개발팀"인지 확인
        Member foundMember = em.find(Member.class, member.getId());
        System.out.println("5. Member 다시 조회 하면 → member.getTeam() 호출 후 not null , SQL 보기");
//        Hibernate:
//        select
//        m1_0.MEMBER_ID,
//                m1_0.name,
//                t1_0.TEAM_ID,
//                t1_0.name
//        from
//        CH5_P1_MEMBER m1_0
//        left join
//        CH5_P1_TEAM t1_0
//        on t1_0.TEAM_ID=m1_0.TEAM_ID
//        where
//        m1_0.MEMBER_ID=?

        assertNotNull(foundMember.getTeam());
        assertEquals("개발팀", foundMember.getTeam().getName());
    }
}
