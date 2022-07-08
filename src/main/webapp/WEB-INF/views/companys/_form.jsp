<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="Company" value="${ForwardConst.ACT_Company.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="${AttributeConst.COMPANY_CODE.getValue()}">社員番号</label><br />
<input type="text" name="${AttributeConst.COMPANY_CODE.getValue()}" id="${AttributeConst.COMPANY_CODE.getValue()}" value="${company.code}" />
<br /><br />

<label for="${AttributeConst.COMPANY_COMPANY.getValue()}">会社</label><br />
<input type="text" name="${AttributeConst.COMPANY_COMPANY.getValue()}" id="${AttributeConst.COMPANY_COMPANY.getValue()}" value="${company.company}" />
<br /><br />

<input type="hidden" name="${AttributeConst.COMPANY_COMPANY.getValue()}" value="${company.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>