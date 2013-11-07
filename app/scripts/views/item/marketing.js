define([
	'backbone',
	'hbs!tmpl/marketing'
],
function( Backbone, MarketingTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log("initialize a Marketing ItemView");
		},
		
    	template: MarketingTmpl,
        

    	/* ui selector cache */
    	ui: {},

		/* Ui events hash */
		events: {},

		/* on render callback */
		onRender: function() {}
	});

});
