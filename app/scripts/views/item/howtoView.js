define([
	'backbone',
	'hbs!tmpl/item/howtoView_tmpl',
	'holderjs'
],
function( Backbone, HowtoTmpl, Holder ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Howto ItemView');
		},
		
		template: HowtoTmpl,
        

		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {},

		onShow: function() {
			Holder.run();
		}
	});

});
