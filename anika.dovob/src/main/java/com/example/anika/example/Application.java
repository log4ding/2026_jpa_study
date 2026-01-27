package com.example.anika.example;

import com.example.anika.example.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final EntityManagerFactory emf;

    public Application(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        EntityManager em = emf.createEntityManager(); // 엔티티 매니저 생성
        EntityTransaction tx = em.getTransaction(); // 트랜잭션 기능 획득

        try {
            tx.begin(); // 트랜잭션 시작

            // 비즈니스 로직
            Member member = new Member();
            member.setName("아니카");
            member.setCity("경기도");
            member.setStreet("안양시");
            member.setZipcode("12345");
            em.persist(member);

            // 회원 조회
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("회원 이름: " + findMember.getName());

            tx.commit(); // 트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); // 트랜잭션 롤백
        } finally {
            em.close(); // 엔티티 매니저 종료
        }
    }
}
