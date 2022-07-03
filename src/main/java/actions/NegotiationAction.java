package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.NegotiationView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.NegotiationService;

public class NegotiationAction extends ActionBase {
    private NegotiationService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new NegotiationService();

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

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<NegotiationView> negotiations = service.getAllPerPage(page);

        //全日報データの件数を取得
        long negotiationsCount = service.countAll();

        putRequestScope(AttributeConst.Negotiations, negotiations); //取得した日報データ
        putRequestScope(AttributeConst.Negotiation_COUNT, negotiationsCount); //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_Negotiation_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        NegotiationView nv = new NegotiationView();
        nv.setNegotiationDate(LocalDate.now());
        putRequestScope(AttributeConst.Negotiation, nv); //日付のみ設定済みの商談インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_Negotiation_NEW);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst.Negotiation_DATE) == null
                    || getRequestParam(AttributeConst.Negotiation_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.Negotiation_DATE));
            }

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値をもとに日報情報のインスタンスを作成する
            NegotiationView nv = new NegotiationView(
                    null,
                    ev, //ログインしている従業員を、日報作成者として登録する
                    getRequestParam(AttributeConst.Negotiation_COMPAY),
                    day,
                    getRequestParam(AttributeConst.Negotiation_TITLE),
                    getRequestParam(AttributeConst.Negotiation_CONTENT),
                    null,
                    null);

            //商談情報登録
            List<String> errors = service.create(nv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.Negotiation, nv);//入力された日報情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_Negotiation_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_Negotiation, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に商談データを取得する
        NegotiationView nv = service.findOne(toNumber(getRequestParam(AttributeConst.Negotiation_ID)));

        if (nv == null) {
            //該当の商談データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.Negotiation, nv); //取得した日報データ

            //詳細画面を表示
            forward(ForwardConst.FW_Negotiation_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に商談データを取得する
        NegotiationView nv = service.findOne(toNumber(getRequestParam(AttributeConst.Negotiation_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (nv == null || ev.getId() != nv.getEmployee().getId()) {
            //該当の日報データが存在しない、または
            //ログインしている従業員が商談の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.Negotiation, nv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_Negotiation_EDIT);
        }

    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に商談データを取得する
            NegotiationView nv = service.findOne(toNumber(getRequestParam(AttributeConst.Negotiation_ID)));

            //入力された商談内容を設定する
            nv.setNegotiationDate(toLocalDate(getRequestParam(AttributeConst.Negotiation_DATE)));
            nv.setCompany(getRequestParam(AttributeConst.Negotiation_COMPAY));
            nv.setTitle(getRequestParam(AttributeConst.Negotiation_TITLE));
            nv.setContent(getRequestParam(AttributeConst.Negotiation_CONTENT));

            //日報データを更新する
            List<String> errors = service.update(nv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.Negotiation, nv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_Negotiation_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_Negotiation, ForwardConst.CMD_INDEX);

            }
        }
    }

}
