package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CompanyView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.CompanyService;

public class CompanyAction extends ActionBase {
    private CompanyService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new CompanyService();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<CompanyView> companys = service.getPerPage(page);

        //全ての従業員データの件数を取得
        long companyCount = service.countAll();

        putRequestScope(AttributeConst.COMPANYS, companys); //取得した従業員データ
        putRequestScope(AttributeConst.COMPANYS_COUNT, companyCount); //全ての従業員データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_Company_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.COMPANY, new CompanyView()); //空の従業員インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_Company_NEW);
    }


}
