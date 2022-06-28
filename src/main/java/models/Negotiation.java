package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商談状況データのDTOモデル
 *
 */
@Table(name = "negotiations")
@NamedQueries({
    @NamedQuery(
            name =  "negotiation.getAll",
            query = "SELECT n FROM Negotiation AS n ORDER BY n.id DESC"),
    @NamedQuery(
            name = "negotiation.count",
            query = "SELECT COUNT(n) FROM Negotiation AS n"),
    @NamedQuery(
            name = "negotiation.getAllMine",
            query = "SELECT n FROM Negotiation AS n WHERE n.company = : company ORDER BY n.id DESC"),
    @NamedQuery(
            name = "negotiation.countAllMine",
            query =  "SELECT COUNT(n) FROM Negotiation AS n WHERE n.company = : company")
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Negotiation {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 日報を登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;

    /**
     * 会社
     */
    @Column(name = "company", nullable = false)
    private String Company;

    /**
     * いつの日報かを示す日付
     */
    @Column(name = "negotiations_date", nullable = false)
    private LocalDate NegotiationDate;

    /**
     * 商談のタイトル
     */
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /**
     * 商談の内容
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 登録日時
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
