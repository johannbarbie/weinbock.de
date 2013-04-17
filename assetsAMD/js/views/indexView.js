
define(['underscoreM', 'marionette', 'templates'], function(_, Marionette, templates) {
    'use strict';
    var IndexView = Marionette.ItemView.extend({
        template: _.template(templates.index),
        className: "index",
        onShow:function () {
        	$('.carousel').carousel();
	    }
    });
    return IndexView;
});