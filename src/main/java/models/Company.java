package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 企業データのDTOモデル
 *
 */
@Table(name = "companys")
@NamedQueries({
    @NamedQuery(
            name =  "company.getAll",
            query = "SELECT c FROM Company AS c ORDER BY c.id DESC"),
    @NamedQuery(
            name = "company.count",
            query = "SELECT COUNT(c) FROM Company AS c"),
    @NamedQuery(
            name = "company.getByCodeAndPass",
            query =  "SELECT c FROM Company AS c WHERE c.code = :code AND c.company = :company"),
    @NamedQuery(
            name = "company.countRegisteredByCode",
            query =  "SELECT COUNT(c) FROM Company AS c WHERE c.code = :code"),
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Company {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会社番号
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * 会社
     */
    @Column(name = "company", nullable = false)
    private String company;

    /**
     * 登録日時
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;




}
