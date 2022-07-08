package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.CompanyView;
import constants.MessageConst;
import services.CompanyService;

public class CompanyValidator {
    /**
     * 従業員インスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param cv CompanyViewのインスタンス
     * @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @param companyCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーのリスト
     */
    public static List<String> validate(
            CompanyService service, CompanyView cv, Boolean codeDuplicateCheckFlag, Boolean companyCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //社員番号のチェック
        String codeError = validateCode(service, cv.getCode(), codeDuplicateCheckFlag);
        if (!codeError.equals("")) {
            errors.add(codeError);
        }

        //パスワードのチェック
        String passError = validateCompany(cv.getCompany(), companyCheckFlag);
        if (!passError.equals("")) {
            errors.add(passError);
        }

        return errors;
    }

    /**
     * 社員番号の入力チェックを行い、エラーメッセージを返却
     * @param service EmployeeServiceのインスタンス
     * @param code 社員番号
     * @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validateCode(CompanyService service, String code, Boolean codeDuplicateCheckFlag) {

        //入力値がなければエラーメッセージを返却
        if (code == null || code.equals("")) {
            return MessageConst.E_NOEMP_CODE.getMessage();
        }

        if (codeDuplicateCheckFlag) {
            //社員番号の重複チェックを実施

            long companysCount = isDuplicateCompany(service, code);

            //同一社員番号が既に登録されている場合はエラーメッセージを返却
            if (companysCount > 0) {
                return MessageConst.E_EMP_CODE_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * @param service CompanyServiceのインスタンス
     * @param code 番号
     * @return 会社テーブルに登録されている同一社員番号のデータの件数
     */
    private static long isDuplicateCompany(CompanyService service, String code) {

        long companysCount = service.countByCode(code);
        return companysCount;
    }

    /**
     * companyの入力チェックを行い、エラーメッセージを返却
     * @param company
     * @param companyCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validateCompany(String company, Boolean CompanyCheckFlag) {

        //入力チェックを実施 かつ 入力値がなければエラーメッセージを返却
        if (CompanyCheckFlag && (company == null || company.equals(""))) {
            return MessageConst.E_NOCONPANY.getMessage();
        }

        //エラーがない場合は空文字を返却
        return "";
    }


}
