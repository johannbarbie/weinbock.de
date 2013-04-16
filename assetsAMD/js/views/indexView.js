
define(['underscoreM', 'marionette', 'templates', 'jquery','flexslider'], function(_, Marionette, templates, $, flexslider) {
    'use strict';
    var IndexView = Marionette.ItemView.extend({
        template: _.template(templates.index),
        className: "index",
        onShow:function () {
            $('.flexslider').flexslider({
    			animation: "fade",
                controlNav: true,
                directionNav: true,
                slideshow: false,
                slideshowSpeed: 0,
                animationLoop: true,
                animationSpeed: 600,
                prevText: "<",
                nextText: ">",
                start: function (slider) {
                    if (slider.controlNav !== undefined) {
                        slider.addClass("with-controlnav");
                    }
                }
  			});
	    }
    });
    return IndexView;
});