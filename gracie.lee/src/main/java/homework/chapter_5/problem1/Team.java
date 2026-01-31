package homework.chapter_5.problem1;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 숙제 1: 다대일 양방향 - Team 엔티티
 *
 * 문제 1-1 ~ 1-4에서 공통으로 사용
 */
@Entity(name = "Ch5P1Team")
@Table(name = "CH5_P1_TEAM")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")  // 읽기 전용 (FK 관리 안 함)
    private List<Member> members = new ArrayList<>();

    protected Team() {
    }

    public Team(String name) {
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

    public List<Member> getMembers() {
        return members;
    }
}
