package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.NegotiationConverter;
import actions.views.NegotiationView;
import constants.JpaConst;
import models.Negotiation;
import models.validators.NegotiationValidator;

public class NegotiationService extends ServiceBase {
    /**
     * 指定した従業員が作成した商談データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<NegotiationView> getMinePerPage(EmployeeView employee, int page) {

        List<Negotiation> negotiations = em.createNamedQuery("negotiation.getAllMine", Negotiation.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return NegotiationConverter.toViewList(negotiations);
    }

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery("negotiation.countAllMine", Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、NegotiationViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<NegotiationView> getAllPerPage(int page) {

        List<Negotiation> negotiations = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Negotiation.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return NegotiationConverter.toViewList(negotiations);
    }

    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long negotiations_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return negotiations_count;
    }

    /**
     * idを条件に取得したデータをNegotiationViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public NegotiationView findOne(int id) {
        return NegotiationConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、商談テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(NegotiationView nv) {
        List<String> errors = NegotiationValidator.validate(nv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            nv.setCreatedAt(ldt);
            nv.setUpdatedAt(ldt);
            createInternal(nv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に、商談データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(NegotiationView nv) {

        //バリデーションを行う
        List<String> errors = NegotiationValidator.validate(nv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            nv.setUpdatedAt(ldt);

            updateInternal(nv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Negotiation findOneInternal(int id) {
        return em.find(Negotiation.class, id);
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(NegotiationView nv) {

        em.getTransaction().begin();
        em.persist(NegotiationConverter.toModel(nv));
        em.getTransaction().commit();

    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(NegotiationView nv) {

        em.getTransaction().begin();
        Negotiation n = findOneInternal(nv.getId());
        NegotiationConverter.copyViewToModel(n, nv);
        em.getTransaction().commit();

    }


}
