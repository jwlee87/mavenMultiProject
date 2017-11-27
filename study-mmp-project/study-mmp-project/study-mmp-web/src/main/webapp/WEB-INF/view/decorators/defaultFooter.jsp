<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- [D] FOOTER -->
<footer class="p_footer">

    <div id="calendar_layer" class="calendar_layer" style="display:none">
        <div class="calendar-header">
            <a href="#" class="calendar-rollover calendar-btn-prev"><span class="ico ico_cal_prev"></span></a>
            <strong class="calendar-title calendar-clickable-title"></strong>
            <a href="#" class="calendar-rollover calendar-btn-next"><span class="ico ico_cal_next"></span></a>
        </div>
        <div class="calendar-body">
            <table>
                <thead>
                <tr>
                    <th class="sun">일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th class="sat">토</th>
                </tr>
                </thead>
                <tbody>
                <!-- A row template of mapping week on "day" layer -->
                <tr class="calendar-week">
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                </tr>
                <!--// A row template of mapping week on "day" layer -->
                </tbody>
            </table>
        </div>
        <div class="calendar-body">
            <table>
                <tbody>
                <!-- A row template of mapping month group on "month" layer -->
                <tr class="calendar-month-group">
                    <td class="calendar-month"></td>
                    <td class="calendar-month"></td>
                </tr>
                <!--// A row template of mapping month group on "month" layer -->
                </tbody>
            </table>
        </div>
        <div class="calendar-body">
            <table>
                <tbody>
                <!-- A row template of mapping year group on "year" layer -->
                <tr class="calendar-year-group">
                    <td class="calendar-year"></td>
                    <td class="calendar-year"></td>
                    <td class="calendar-year"></td>
                    <td class="calendar-year"></td>
                    <td class="calendar-year"></td>
                </tr>
                <!--// A row template of mapping year group on "year" layer -->
                </tbody>
            </table>
        </div>
        <div class="calendar-footer"></div>
    </div>

</footer>
