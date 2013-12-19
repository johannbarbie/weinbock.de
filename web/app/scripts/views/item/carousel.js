define([
	'backbone',
	'hbs!tmpl/carousel',
	'holderjs'
],
function( Backbone, CarouselTmpl, Holder ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Carousel ItemView');
		},
		
		template: CarouselTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {},

		onShow: function() {
			Holder.run();
		}

	});

});
