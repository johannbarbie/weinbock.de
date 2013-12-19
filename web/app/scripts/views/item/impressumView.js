define([
	'backbone',
	'hbs!tmpl/item/impressumView_tmpl'
],
function( Backbone, ImpressumTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize an Impressum ItemView');
		},
		
		template: ImpressumTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {}
		
	});

});
