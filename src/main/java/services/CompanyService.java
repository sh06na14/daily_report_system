package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.CompanyConverter;
import actions.views.CompanyView;
import constants.JpaConst;
import models.Company;
import models.validators.CompanyValidator;

public class CompanyService extends ServiceBase {
    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、EmployeeViewのリストで返却する
     * @param page ページ数
     * @return 表示するデータのリスト
     */
    public List<CompanyView> getPerPage(int page) {
        List<Company> companys = em.createNamedQuery("company.getAll", Company.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return CompanyConverter.toViewList(companys);
    }

    /**
     * 従業員テーブルのデータの件数を取得し、返却する
     * @return 従業員テーブルのデータの件数
     */
    public long countAll() {
        long companyCount = (long) em.createNamedQuery("company.count", Long.class)
                .getSingleResult();

        return companyCount;
    }

    /**
     * 社員番号、パスワードを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param code 社員番号
     * @param plainPass パスワード文字列
     * @param pepper pepper文字列
     * @return 取得データのインスタンス 取得できない場合null
     */
    public CompanyView findOne(String code, String company) {
        Company c = null;
        try {

            //社員番号とハッシュ化済パスワードを条件に未削除の従業員を1件取得する
            c = em.createNamedQuery("company.getByCodeAndPass", Company.class)
                    .setParameter("code", code)
                    .setParameter("company", company)
                    .getSingleResult();

        } catch (NoResultException ex) {
        }

        return CompanyConverter.toView(c);

    }

    /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public CompanyView findOne(int id) {
        Company c = findOneInternal(id);
        return CompanyConverter.toView(c);
    }

    /**
     * 社員番号を条件に該当するデータの件数を取得し、返却する
     * @param code 社員番号
     * @return 該当するデータの件数
     */
    public long countByCode(String code) {

        //指定した社員番号を保持する従業員の件数を取得する
        long companys_count = (long) em.createNamedQuery("company.countRegisteredByCode", Long.class)
                .setParameter("code", code)
                .getSingleResult();
        return companys_count;
    }

    /**
     * 画面から入力された従業員の登録内容を元にデータを1件作成し、従業員テーブルに登録する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
    public List<String> create(CompanyView cv) {

        //登録日時、更新日時は現在時刻を設定する
        LocalDateTime now = LocalDateTime.now();
        cv.setCreatedAt(now);

        //登録内容のバリデーションを行う
        List<String> errors = CompanyValidator.validate(this, cv, true, true);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            createInternal(cv);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された従業員の更新内容を元にデータを1件作成し、従業員テーブルを更新する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(CompanyView cv) {

        //idを条件に登録済みの従業員情報を取得する
        CompanyView savedCompany = findOne(cv.getId());

        boolean validateCode = false;
        if (!savedCompany.getCode().equals(cv.getCode())) {
            //社員番号を更新する場合

            //社員番号についてのバリデーションを行う
            validateCode = true;
            //変更後の社員番号を設定する
            savedCompany.setCode(cv.getCode());
        }

        boolean validateCompany = false;
        if (!savedCompany.getCompany().equals(cv.getCompany())) {
            //社員番号を更新する場合

            //社員番号についてのバリデーションを行う
            validateCompany = true;
            //変更後の社員番号を設定する
            savedCompany.setCode(cv.getCompany());
        }

        //更新内容についてバリデーションを行う
        List<String> errors = CompanyValidator.validate(this, savedCompany, validateCode, validateCompany);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {
            updateInternal(savedCompany);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 社員番号とパスワードを条件に検索し、データが取得できるかどうかで認証結果を返却する
     * @param code 社員番号
     * @param plainPass パスワード
     * @param pepper pepper文字列
     * @return 認証結果を返却す(成功:true 失敗:false)
     */
    public Boolean validateLogin(String code, String company) {

        boolean isValidCompany = false;
        if (code != null && !code.equals("") && company != null && !company.equals("")) {
            CompanyView cv = findOne(code, company);

            if (cv != null && cv.getId() != null) {

                //データが取得できた場合、認証成功
                isValidCompany = true;
            }
        }

        //認証結果を返却する
        return isValidCompany;
    }

    /**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Company findOneInternal(int id) {
        Company c = em.find(Company.class, id);

        return c;
    }

    /**
     * 従業員データを1件登録する
     * @param ev 従業員データ
     * @return 登録結果(成功:true 失敗:false)
     */
    private void createInternal(CompanyView cv) {

        em.getTransaction().begin();
        em.persist(CompanyConverter.toModel(cv));
        em.getTransaction().commit();

    }

    /**
     * 従業員データを更新する
     * @param ev 画面から入力された従業員の登録内容
     */
    private void updateInternal(CompanyView cv) {

        em.getTransaction().begin();
        Company c = findOneInternal(cv.getId());
        CompanyConverter.copyViewToModel(c, cv);
        em.getTransaction().commit();

    }



}
