define([
	'backbone',
	'hbs!tmpl/item/accountView_tmpl'
],
function( Backbone, AccountTmpl  ) {
    'use strict';

	/* Return a ItemView class definition */
	return Backbone.Marionette.ItemView.extend({

		initialize: function() {
			console.log('initialize an Account ItemView');
			console.dir(this.model.get('email'));
		},

		template: AccountTmpl,
        
		modelEvents: {
            'change': "modelChanged"
        },
        modelChanged: function() {
            this.render();
        },
		/* ui selector cache */
		ui: {},

		/* Ui events hash */
		events: {},

		/* on render callback */
		onRender: function() {}
	});

});
