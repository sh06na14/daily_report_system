package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Company;

public class CompanyConverter {
    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param cv CompanyViewのインスタンス
     * @return Companyのインスタンス
     */
    public static Company toModel(CompanyView cv) {

        return new Company(
                cv.getId(),
                cv.getCode(),
                cv.getCompany(),
                cv.getCreatedAt());

    }
    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param c Companyのインスタンス
     * @return CompanyViewのインスタンス
     */
    public static CompanyView toView(Company c) {

        if(c == null) {
            return null;
        }

        return new CompanyView(
                c.getId(),
                c.getCode(),
                c.getCompany(),
                c.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<CompanyView> toViewList(List<Company> list) {
        List<CompanyView> evs = new ArrayList<>();

        for (Company c : list) {
            evs.add(toView(c));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param e DTOモデル(コピー先)
     * @param ev Viewモデル(コピー元)
     */
    public static void copyViewToModel(Company c, CompanyView cv) {
        c.setId(cv.getId());
        c.setCode(cv.getCode());
        c.setCompany(cv.getCompany());
        c.setCreatedAt(cv.getCreatedAt());

    }


}
