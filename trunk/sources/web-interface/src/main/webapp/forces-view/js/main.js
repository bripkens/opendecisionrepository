var email = null,
    password = null,
    projectOverviewUrl = '/web-service/rest/user/projects/';
    projectDetailUrl = '/web-service/rest/projects/',
    updateUrl = function(id){return '/web-service/rest/projects/' + id + '/ratings'};

var RATINGS = [
    {name : 'excluded', icon : 'X'},
    {name : 'verynegative', icon : '--'},
    {name : 'negative', icon : '-'},
    {name : 'neutral', icon : ''},
    {name : 'positive', icon : '+'},
    {name : 'verypositive', icon : '++'},
    {name : 'required', icon : 'R'}
];

var focusedElement = null;
var numberOfConcerns = -1;
var numberOfDecisions = -1;

var projectId = null;

/*
 * ###########################################################
 * Init
 * ###########################################################
 */
$(function() {
    var loginDialog = $('#loginDialog');
    loginDialog.modal({
        show : true,
        backdrop : 'static'
    });
    
    var selectProjectDialog = $('#selectProjectDialog');
    selectProjectDialog.modal({
        show : false,
        backdrop : 'static'
    });
    
    var loadingDialog = $('#loadingDialog');
    loadingDialog.modal({
        show : false,
        backdrop : 'static'
    });
    
    $('#loginButton').click(function() {
        var email = $('#emailIn').val(),
            password = $('#passwordIn').val();

        $.ajaxSetup({
            'username' : email,
            'password' : password,
            'dataType' : 'json',
            'contentType' : 'application/xml'
        });
        
        $.ajax({
            url : projectOverviewUrl,
            'contentType' : 'application/xml',
            success : function(data) {
                var select = $('#projectIn');
                for(var i = 0; i < data.list.length; i++) {
                    select.append($(document.createElement('option'))
                        .text(data.list[i].name)
                        .val(data.list[i].id));
                }
                
                loginDialog.modal('hide');
                selectProjectDialog.modal('show');
            }
        });
    });
    
    $('#selectProjectButton').click(function() {
        projectId = $('#projectIn').val();
        $.ajax({
            url : projectDetailUrl + projectId,
            success : function(data) {
                fillTable(data);
                
                $('table td').mouseenter(function() {
                    var id = $(this).data('columnId');
                    $('td.column' + id).addClass('highlight');
                });

                $('table td').mouseleave(function() {
                    var id = $(this).data('columnId');
                    $('td.column' + id).removeClass('highlight');
                });

                $('table td').click(function() {
                    var columnId = $(this).data('columnId'),
                        rowId = $(this).data('rowId');

                    if (columnId > 0 && rowId > 0) {
                        $('.permHighlight').removeClass('permHighlight');
                        $('.row' + rowId).add('.column' + columnId).addClass('permHighlight');
                        focusedElement = $(this);
                    }
                });
                
                selectProjectDialog.modal('hide');
            }
        });
    });
    
    $(document.documentElement).keydown(handleKeyboardInput);
    
    $('#doSync').click(function() {
        var rateableCells = $('table td.updated');
        var totalAmount = rateableCells.length,
            synchedCells = 0;
            
        if (totalAmount > 0) {
            loadingDialog.modal('show');
        }
        
        rateableCells.each(function() {
            var element = $(this),
                rating = RATINGS[$(this).data('rating')].name.toUpperCase(),
                row = $(this).data('rowId'),
                column = $(this).data('columnId'),
                concernId = $('.column0.row' + row).data('concernId'),
                decisionId = $('.row0.column' + column).data('decisionId');
                
            var data = '<rating><concernId>' + concernId +
                '</concernId><decisionId>' + decisionId +
                '</decisionId><effect>' + rating + '</effect></rating>';
            
            $.ajax({
                url : updateUrl(projectId),
                type : 'POST',
                data : data,
                success : function() {
                    element.removeClass('updated');
                    synchedCells++;
                    if (synchedCells == totalAmount) {
                        loadingDialog.modal('hide');
                    }
                }
            });
        });
    });
    
    var decisionsOut = $('.topBar'),
            concernsOut = $('.leftBar');
    
    $(window).scroll(function(e) {
        var top = $(window).scrollTop(),
            left = $(window).scrollLeft(),
            minTop = concernsOut.data('minTop');
        decisionsOut.css('left', (left * -1) + 'px');
        concernsOut.css('top', (minTop - top) + 'px');
    });
});

/*
 * ###########################################################
 * Initial table fillment
 * ###########################################################
 */
var fillTable = function(data) {
    var table = $('table');
    var decisionsOut = $('.topBar'),
        concernsOut = $('.leftBar'),
        cornerOverlay = $('#cornerOverlay');

    var emptyFirstCell = $(document.createElement('li'));
    decisionsOut.append(emptyFirstCell);
        
    numberOfConcerns = data.concerns.length;
    numberOfDecisions = data.decisions.length;
    
    var headerRow = $(document.createElement('tr'));
    headerRow.append($(document.createElement('td'))
        .addClass('column0')
        .addClass('row0')
        .data('columnId', 0)
        .data('rowId', 0));
    
    table.append(headerRow);
    
    data.decisions.sort(function(a,b) {
        return a.name.toLowerCase().localeCompare(b.name.toLowerCase());
    });
    
    data.concerns.sort(function(a,b) {
        return a.externalId.toLowerCase().localeCompare(b.externalId.toLowerCase());
    });
    
    for(var i = 0; i < data.decisions.length; i++) {
        headerRow.append($(document.createElement('td'))
            .addClass('column' + (i+1))
            .addClass('row0')
            .data('columnId', i+1)
            .data('rowId', 0)
            .data('decisionId', data.decisions[i].id)
            .append($(document.createElement('span'))
                .text(data.decisions[i].history[0].state)
                .addClass('decisionState'))
            .append(document.createElement('br'))
            .append($(document.createElement('span'))
                .text(data.decisions[i].name)));
    }
    
    // looping an additional time because the table is now constructed completely
    decisionsOut.width(headerRow.width()+1);
    decisionsOut.height(headerRow.height()+1);
    decisionsOut.data('minTop', 0);
    decisionsOut.data('minLeft', 0);
    for(var i = 0; i < data.decisions.length; i++) {
        var cell = $('.row0.column' + (i+1));
        
        decisionsOut.append($(document.createElement('li'))
            .addClass('column' + (i+1))
            .addClass('row0')
            .data('columnId', i+1)
            .data('rowId', 0)
            .data('decisionId', data.decisions[i].id)
            .width(cell.outerWidth()-11) // for border and padding
            .height(cell.outerHeight()-8) // for border
            .append($(document.createElement('span'))
                .text(data.decisions[i].history[0].state)
                .addClass('decisionState'))
            .append(document.createElement('br'))
            .append($(document.createElement('span'))
                .text(data.decisions[i].name)));
    }
    
    for(var i = 0; i < data.concerns.length; i++) {
        var concernId = data.concerns[i].id;
        
        concernCell = $(document.createElement('td'))
                .text(data.concerns[i].name)
                .addClass('column0')
                .addClass('row' + (i+1))
                .data('columnId', 0)
                .data('rowId', 0)
                .data('concernId', concernId);
        
        var row = $(document.createElement('tr'))
            .append(concernCell);
        table.append(row);
            
        for(var j = 0; j < data.decisions.length; j++) {
            var decisionId = data.decisions[j].id,
                rating = getRating(data, decisionId, concernId);
            
            row.append($(document.createElement('td'))
                .addClass('column' + (j+1))
                .addClass('row' + (i+1))
                .addClass('rateableColumn')
                .data('columnId', j+1)
                .data('rowId', i+1)
                .data('rating', rating.index)
                .text(rating.label));
        }
    }
    
    var firstEmptyCellInTable = $('.row0.column0');
    emptyFirstCell.width(firstEmptyCellInTable.outerWidth() - 10); // for padding
    emptyFirstCell.height(firstEmptyCellInTable.outerHeight());
    
    var minTop = firstEmptyCellInTable.outerHeight() + 1;
    concernsOut.data('minTop', minTop);
    concernsOut.data('minLeft', 0);
    concernsOut.css('top', minTop);
    for(var i = 0; i < data.concerns.length; i++) {
        var cell = $('.column0.row' + (i+1));
        concernsOut.append($(document.createElement('li'))
                .text(data.concerns[i].name)
                .addClass('column0')
                .addClass('row' + (i+1))
                .width(cell.outerWidth()-9)
                .height(cell.outerHeight()-10)
                .data('columnId', 0)
                .data('rowId', 0)
                .data('concernId', data.concerns[i].id));
    }
    
    cornerOverlay.width(firstEmptyCellInTable.outerWidth()+1);
    cornerOverlay.height(firstEmptyCellInTable.outerHeight()+1);
    
    $('.row0, .column0').not('li').css('visibility', 'hidden');
    $('.rateableColumn').mouseenter(highlight);
};

var getRating = function(data, decisionId, concernId) {
    for(var i = 0; i < data.ratings.length; i++) {
        var rating = data.ratings[i];
        
        if (rating.concernId === concernId &&
            rating.decisionId === decisionId) {
            
            for(var j = 0; j < RATINGS.length; j++) {
                if (RATINGS[j].name.toUpperCase() === rating.effect) {
                    return {
                        index : j,
                        label : RATINGS[j].icon
                    };
                }
            }
        }
    }
    
    return { // defaults to neutral
        index : 3,
        label : ''
    };
};

/*
 * ###########################################################
 * Handle keyboard input
 * ###########################################################
 */
var handleKeyboardInput = function(event) {
    if (focusedElement === null) {
        return true;
    }
    
    
    if (!event.ctrlKey && [38, 40, 37, 39].indexOf(event.keyCode) != -1) {
        return navigate(focusedElement, event.keyCode);
    } else if ((event.ctrlKey && event.keyCode == 38) || event.keyCode == 187) {
        increaseEffect(focusedElement);
        return false;
    } else if ((event.ctrlKey && event.keyCode == 40) || event.keyCode == 189) {
        decreaseEffect(focusedElement);
        return false;
    }
    
    return true;
};

var increaseEffect = function(element) {
    var currentRating = element.data('rating') + 1;
    if (currentRating < RATINGS.length) {
        element.data('rating', currentRating)
            .addClass('updated')
            .text(RATINGS[currentRating].icon);
    }
};

var decreaseEffect = function(element) {
    var currentRating = element.data('rating') - 1;
    if (currentRating >= 0) {
        element.data('rating', currentRating)
            .addClass('updated')
            .text(RATINGS[currentRating].icon);
    }
};

var navigate = function(element, keyCode) {
    var row = element.data('rowId'),
        column = element.data('columnId');
    
    if (keyCode == 38) {
        row = Math.max(1, row-1);
    } else if (keyCode == 40) {
        row = Math.min(numberOfConcerns, row+1);
    } else if (keyCode == 37) {
        column = Math.max(1, column-1);
    } else if (keyCode == 39) {
        column = Math.min(numberOfDecisions, column+1);
    } else {
        return true;
    }
    
    $('.permHighlight').removeClass('permHighlight');
    $('.row' + row).add('.column' + column).addClass('permHighlight');
    focusedElement = $('.row' + row + '.column' + column);
    
    var offset = focusedElement.offset();
    
    $('html,body').stop(true, false).animate({
        scrollTop : offset.top - $(window).height() / 2,
        scrollLeft : offset.left - $(window).width() / 2
    });
    
    return false;
};

var highlight = function() {
    var row = $(this).data('rowId'),
        column = $(this).data('columnId');

    $('.highlight').removeClass('highlight');
    $('.row' + row + ', .column' + column).addClass('highlight');
};