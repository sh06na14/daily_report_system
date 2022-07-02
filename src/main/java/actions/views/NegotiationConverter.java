package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Negotiation;

/**
 * 商談データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class NegotiationConverter {
    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param nv NegotiationViewのインスタンス
     * @return Negotiationのインスタンス
     */
    public static Negotiation toModel(NegotiationView nv) {
        return new Negotiation(
                nv.getId(),
                EmployeeConverter.toModel(nv.getEmployee()),
                nv.getCompany(),
                nv.getNegotiationDate(),
                nv.getTitle(),
                nv.getContent(),
                nv.getCreatedAt(),
                nv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param n Negotiationのインスタンス
     * @return NegotiationViewのインスタンス
     */
    public static NegotiationView toView(Negotiation n) {

        if (n == null) {
            return null;
        }

        return new NegotiationView(
                n.getId(),
                EmployeeConverter.toView(n.getEmployee()),
                n.getCompany(),
                n.getNegotiationDate(),
                n.getTitle(),
                n.getContent(),
                n.getCreatedAt(),
                n.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<NegotiationView> toViewList(List<Negotiation> list) {
        List<NegotiationView> evs = new ArrayList<>();

        for (Negotiation n : list) {
            evs.add(toView(n));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Negotiation n, NegotiationView nv) {
        n.setId(nv.getId());
        n.setEmployee(EmployeeConverter.toModel(nv.getEmployee()));
        n.setCompany(nv.getCompany());
        n.setNegotiationDate(nv.getNegotiationDate());
        n.setTitle(nv.getTitle());
        n.setContent(nv.getContent());
        n.setCreatedAt(nv.getCreatedAt());
        n.setUpdatedAt(nv.getUpdatedAt());

    }


}
