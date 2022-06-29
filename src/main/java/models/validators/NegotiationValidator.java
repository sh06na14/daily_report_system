package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.NegotiationView;
import constants.MessageConst;

public interface NegotiationValidator {
    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(NegotiationView nv) {
        List<String> errors = new ArrayList<String>();

        //会社のチェック
        String companyError = validateCompany(nv.getCompany());
        if (!companyError.equals("")) {
            errors.add(companyError);
        }

        //タイトルのチェック
        String titleError = validateTitle(nv.getTitle());
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(nv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }

        /**
     * 会社名に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param company タイトル
     * @return エラーメッセージ
     */
    private static String validateCompany(String company) {
        if (company == null || company.equals("")) {
            return MessageConst.E_NOCONPANY.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param title タイトル
     * @return エラーメッセージ
     */
    private static String validateTitle(String title) {
        if (title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content) {
        if (content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

}
