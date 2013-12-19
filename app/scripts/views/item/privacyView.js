define([
	'backbone',
	'hbs!tmpl/item/privacyView_tmpl'
],
function( Backbone, PrivacyTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Privacy ItemView');
		},
		
		template: PrivacyTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {}

	});

});
