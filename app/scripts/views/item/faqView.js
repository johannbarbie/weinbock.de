define([
	'backbone',
	'hbs!tmpl/item/faqView_tmpl'
],
function( Backbone, FaqTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize an Impressum ItemView');
		},
		
		template: FaqTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {}
		
	});

});
