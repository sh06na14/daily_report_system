<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<fmt:parseDate value="${negotiation.negotiationDate}" pattern="yyyy-MM-dd" var="negotiationDay" type="date" />
<label for="${AttributeConst.Negotiation_DATE.getValue()}">日付</label><br />
<input type="date" name="${AttributeConst.Negotiation_DATE.getValue()}" value="<fmt:formatDate value='${negotiationDay}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label for="${AttributeConst.Negotiation_COMPAY.getValue()}">会社</label><br />
<input type="text" name="${AttributeConst.Negotiation_COMPAY.getValue()}" value="${negotiation.company}" />
<br /><br />

<label for="${AttributeConst.Negotiation_TITLE.getValue()}">タイトル</label><br />
<input type="text" name="${AttributeConst.Negotiation_TITLE.getValue()}" value="${negotiation.title}" />
<br /><br />

<label for="${AttributeConst.Negotiation_CONTENT.getValue()}">内容</label><br />
<textarea name="${AttributeConst.Negotiation_CONTENT.getValue()}" rows="10" cols="50">${negotiation.content}</textarea>
<br /><br />
<input type="hidden" name="${AttributeConst.Negotiation_ID.getValue()}" value="${negotiation.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>