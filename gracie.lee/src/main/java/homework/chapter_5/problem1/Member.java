package homework.chapter_5.problem1;

import jakarta.persistence.*;

/**
 * 숙제 1: 다대일 양방향 - Member 엔티티 (연관관계 주인)
 *
 * 문제 1-1 ~ 1-4에서 공통으로 사용
 */
@Entity(name = "Ch5P1Member")
@Table(name = "CH5_P1_MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @ManyToOne  // 연관관계 주인 (FK 관리)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
