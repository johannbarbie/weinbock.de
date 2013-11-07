define([
	'backbone',
	'hbs!tmpl/carousel'
],
function( Backbone, CarouselTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log("initialize a Carousel ItemView");
		},
		
    	template: CarouselTmpl,
        

    	/* ui selector cache */
    	ui: {},

		/* Ui events hash */
		events: {},

		/* on render callback */
		onRender: function() {}
	});

});
