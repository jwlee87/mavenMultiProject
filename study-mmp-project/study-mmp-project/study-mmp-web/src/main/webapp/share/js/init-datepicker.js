var startDatepicker;
var endDatepicker;
var ruleDatepicker;
var unlimitedStartDateHash;
var todayDateHash;
var calendar; 

$(function(){
    "use strict";
    todayDateHash = getTodayHash();
    unlimitedStartDateHash = getUnlimitedStartDateHash();

    var startDate1 = getStartDate(0, 6, 0);
    var endDate1 = getUnlimitedDateHash();
    var startDate2 = getTodayHash();
    var endDate2 = getUnlimitedDateHash();

    if ($('#calendar_layer').length ==0 ) {
    	return;
    } else {
	    calendar = new tui.component.Calendar({
	        element: '#calendar_layer',
	        titleFormat: 'yyyy.mm',
	        todayFormat: 'yyyy.mm.dd(D)',
	        yearTitleFormat: 'yyyy년',
	        monthTitles: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	        dayTitles: ['일', '월', '화', '수', '목', '금', '토']        
	    });
    }
    
    if ($('.datepicker-start').length !=0 ) {
	    startDatepicker = new tui.component.DatePicker({
	        element: '.datepicker-start',
	        dateForm: 'yyyy-mm-dd',
	        date : startDate1,
	        selectableRanges: [
	            [getUnlimitedStartDateHash(), getUnlimitedDateHash()]
	        ],
	        pos: {left: 1, top: 28, zIndex: 100}
	    }, calendar);
	    
	    startDatepicker.on('pick', function() {
	        var pickedDate = startDatepicker.getDateHash();
	        var startTime = getDateTimeFromDateHash(pickedDate);
	        var endTime = getDateTimeFromDateHash(endDatepicker.getDateHash());
	        if (startTime >= endTime)
	        	endDatepicker.setDate(pickedDate.year, pickedDate.month, pickedDate.date);
	    });
    }

    if ($('.datepicker-end').length !=0 ) {
	    endDatepicker = new tui.component.DatePicker({
	        element: '.datepicker-end',
	        dateFormat: 'yyyy-mm-dd',
	        date: startDate2,
	        selectableRanges: [
	            [getUnlimitedStartDateHash(), getUnlimitedDateHash()]
	        ],
	        pos: {left: 0, top: 28, zIndex: 100}
	    }, calendar);

	    endDatepicker.on('pick', function() {
	        var pickedDate = endDatepicker.getDateHash();
	        var endTime = getDateTimeFromDateHash(pickedDate);
	        var startTime = getDateTimeFromDateHash(startDatepicker.getDateHash());
	        if (startTime >= endTime)
	        	startDatepicker.setDate(pickedDate.year, pickedDate.month, pickedDate.date );
	    });
    }

    if ($('.datepicker-rule').length !=0 ) {
        ruleDatepicker = new tui.component.DatePicker({
            element: '.datepicker-rule',
            dateForm: 'yyyy-mm-dd',
            date : getTomorrowHash(),
            selectableRanges: [
                [getTomorrowHash(), getAfter90DayHash()]
            ],
            showAlways : true,
            pos: {left: 1, top: 1, zIndex: 100}
        }, calendar);

        $('#calendar_layer').parent('div').css('position', 'relative');
    }
});

function getTodayHash() {
    var today = new Date();
    return {
        year: today.getFullYear(),
        month: today.getMonth() + 1,
        date: today.getDate()
    };
}

function getTomorrowHash() {
    var today = new Date();
    today.setDate(today.getDate() + 1);
    return {
        year: today.getFullYear(),
        month: today.getMonth() + 1,
        date: today.getDate()
    };
}

function getDateHash(date) {
	return {
		year: date.getFullYear(),
		month: date.getMonth() + 1,
		date: date.getDate()
	}
}

function getAfter90DayHash() {
    var today = new Date();
    today.setDate(today.getDate() + 90);
    return {
        year: today.getFullYear(),
        month: today.getMonth() + 1,
        date: today.getDate()
    };
}

function getRelativeDateHash(dateObj, date) {
    var relativeDate = new Date(dateObj.year, dateObj.month - 1, dateObj.date + date);

    return {
        year: relativeDate.getFullYear(),
        month: relativeDate.getMonth() + 1,
        date: relativeDate.getDate()
    };
}

function getUnlimitedDateHash() {
    return {
        year: 2099,
        month: 12,
        date: 31
    };
}

function getStartDate(year, month, day) {
	var today = new Date();
	var startDate =  new Date(today.getFullYear() - year, today.getMonth() - month, today.getDate() - day);
	return {
		year : startDate.getFullYear(),
		month : startDate.getMonth() + 1,
		date : startDate.getDate()
	};
}

function getEndDate(year, month, day) {
	var today = new Date();
	var startDate =  new Date(today.getFullYear() + year, today.getMonth() + month, today.getDate() + day);
	return {
		year : startDate.getFullYear(),
		month : startDate.getMonth() + 1,
		date : startDate.getDate()
	};
}

function getDateFromDateHash(dateHash) {
	return new Date(dateHash.year, dateHash.month-1, dateHash.date);
}

function getDateTimeFromDateHash(dateHash) {
	var date= new Date(dateHash.year, dateHash.month, dateHash.date);
	return date.getTime();
}

function getUnlimitedStartDateHash() {
	return {
		year : 2016,
		month: 1,
		date : 1
	}; 
}

function getBirthdayStartDateHash() {
	return {
		year : 1900,
		month: 1,
		date : 1
	}; 
}
