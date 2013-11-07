define([
	'backbone',
	'hbs!tmpl/header'
],
function( Backbone, HeaderTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize a Header ItemView');
		},
		className: 'container',
		template: HeaderTmpl,


		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {},

		/* on render callback */
		onRender: function() {}
	});

});
